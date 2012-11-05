/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;

import java.io.*;
import java.util.*;

/**
 * @author Norman Fomferra
 */
public  class ModuleGenerator implements GeneratorContext {

    public static final String BEAM_CAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_CAPI_NAME = "beam_capi";

    public static final String SELF_VAR_NAME = "_self";
    public static final String METHOD_VAR_NAME = "_method";
    public static final String RESULT_VAR_NAME = "_result";
    public static final String CLASS_VAR_NAME_PATTERN = "class%s";

    private final ApiInfo apiInfo;
    private final Map<ApiMethod, String> functionNames;
    private final Map<ApiClass, List<FunctionGenerator>> functionGenerators;

    public ModuleGenerator(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
        this.functionNames = createFunctionNames(apiInfo);
        this.functionGenerators = createFunctionGenerators(apiInfo);
    }

    public Set<ApiClass> getApiClasses() {
        return apiInfo.getApiClasses();
    }

    @Override
    public String getFunctionName(FunctionGenerator callable) {
        return functionNames.get(callable.getApiMethod());
    }

    public List<FunctionGenerator> getFunctionGenerators(ApiClass apiClass) {
        return functionGenerators.get(apiClass);
    }

    static String getCTypeName(Type type) {
        String name = getTargetComponentTypeName(type, false);
        return name + type.dimension().replace("[]", "*");
    }

    static String getCClassVarName(Type type) {
        return String.format(CLASS_VAR_NAME_PATTERN, getTargetClassName(type));
    }

    public static String getFunctionBaseName(ApiMethod apiMethod) {
        String targetTypeName = getCTypeName(apiMethod.getEnclosingClass().getType());
        if (apiMethod.getMemberDoc().isConstructor()) {
            return String.format("%s_new%s", targetTypeName, targetTypeName);
        } else {
            return String.format("%s_%s", targetTypeName, apiMethod.getJavaName());
        }
    }

    public void run() throws Exception {
        writeWinDef();
        writeHeader();
        writeSource();
        printStats();
    }

    private void writeWinDef() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".def")));
        try {
            writer.printf("LIBRARY \"%s\"\n\n", BEAM_CAPI_NAME);
            writer.printf("EXPORTS\n" +
                                  "\tbeam_is_jvm_created\n" +
                                  "\tbeam_create_jvm\n" +
                                  "\tbeam_create_jvm_with_defaults\n" +
                                  "\tbeam_destroy_jvm\n" +
                                  "\tString_newString\n");
            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator callable : getFunctionGenerators(apiClass)) {
                    writer.printf("\t%s\n", getFunctionName(callable));
                }
            }
        } finally {
            writer.close();
        }
    }

    private void writeHeader() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".h")));
        try {
            generateFileInfo(writer);

            writer.write("\n");
            writeResource(writer, "ModuleGenerator-stubs-1.h");
            writer.write("\n");

            writer.write("\n");
            writer.write("/* Wrapped API classes */\n");
            for (ApiClass apiClass : getApiClasses()) {
                writer.write(String.format("typedef void* %s;\n", getTargetComponentTypeName(apiClass.getType(), true)));
            }
            writer.write("\n");

            writer.write("\n");
            writer.write("/* Non-API classes used in the API */\n");
            writer.write("typedef void* String;\n");
            for (ApiClass usedApiClass : apiInfo.getUsedNonApiClasses()) {
                if (!isString(usedApiClass.getType())) {
                    writer.write(String.format("typedef void* %s;\n", getTargetComponentTypeName(usedApiClass.getType(), true)));
                }
            }
            writer.write("\n");

            writer.write("\n");
            writeResource(writer, "ModuleGenerator-stubs-2.h");
            writer.write("\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // Generate function declarations
            //
            for (ApiClass apiClass : getApiClasses()) {
                writer.write("\n");
                writer.printf("/* Functions for class %s */\n", getTargetComponentTypeName(apiClass.getType(), true));
                writer.write("\n");
                for (FunctionGenerator callable : getFunctionGenerators(apiClass)) {
                    writer.printf("" +
                                          "/**\n" +
                                          " * %s\n" +
                                          " */\n", callable.getMemberDoc().getRawCommentText());
                    generateFunctionDeclaration(writer, callable);
                }
            }
        } finally {
            writer.close();
        }
    }

    private void writeSource() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".c")));
        try {
            generateFileInfo(writer);
            writer.printf("#include <stdlib.h>\n");
            writer.printf("#include <string.h>\n");
            writer.printf("#include \"%s\"\n", BEAM_CAPI_NAME + ".h");
            writer.printf("#include \"jni.h\"\n");

            writer.printf("\n");
            writeResource(writer, "ModuleGenerator-stubs-1.c");
            writer.printf("\n");

            writer.printf("/* Java API classes. */\n");
            for (ApiClass apiClass : getApiClasses()) {
                writer.write(String.format("static jclass %s;\n",
                                           getCClassVarName(apiClass.getType())));
            }
            writer.printf("/* Other Java classes used in the API. */\n");
            writer.write(String.format("static jclass %s;\n",
                                       String.format(CLASS_VAR_NAME_PATTERN, "String")));
            for (ApiClass usedApiClass : apiInfo.getUsedNonApiClasses()) {
                writer.write(String.format("static jclass %s;\n",
                                           getCClassVarName(usedApiClass.getType())));
            }
            writer.write("\n");

            writer.printf("\n");
            writeResource(writer, "ModuleGenerator-stubs-2.c");
            writer.printf("\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // beam_init_api()
            //
            writer.write("int beam_init_api()\n");
            writer.write("{\n");
            writer.printf("" +
                                  "    if (api_init != 0) {\n" +
                                  "        return 0;\n" +
                                  "    }\n");
            writer.printf("" +
                                  "    if (!beam_is_jvm_created() && !beam_create_jvm_with_defaults()) {\n" +
                                  "        return 1;\n" +
                                  "    }\n");

            int errCode = 1000;
            writeClassDef(writer,
                          String.format(CLASS_VAR_NAME_PATTERN, "String"),
                          "java/lang/String",
                          errCode);

            errCode++;
            for (ApiClass apiClass : getApiClasses()) {
                writeClassDef(writer,
                              getCClassVarName(apiClass.getType()),
                              apiClass.getResourceName(),
                              errCode);
                errCode++;
            }
            writer.write("    api_init = 1;\n");
            writer.write("    return 0;\n");
            writer.write("}\n\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // Generate function code
            //
            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator callable : getFunctionGenerators(apiClass)) {
                    generateFunctionDefinition(callable, writer);
                }
            }
        } finally {
            writer.close();
        }
    }

    private String writeResource(Writer writer, String resourceName) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceName)));
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }
        } finally {
            bufferedReader.close();
        }
        return null;
    }

    private void writeClassDef(PrintWriter writer, String classVarName, String classResourceName, int errCode) {
        writer.write(String.format("    %s = (*jenv)->FindClass(jenv, \"%s\");\n",
                                   classVarName, classResourceName));
        writer.write(String.format("    if (%s == NULL) return %d;\n",
                                   classVarName, errCode));
        writer.write("\n");
    }

    void generateFunctionDeclaration(PrintWriter writer, FunctionGenerator callable) {
        writer.printf("%s;\n", callable.generateFunctionSignature(this));
    }

    private void generateFunctionDefinition(FunctionGenerator callable, PrintWriter writer) throws IOException {
        writer.printf("%s\n", callable.generateFunctionSignature(this));
        writer.print("{\n");
        writeLocalMethodVarDecl(writer);
        for (ParameterGenerator parameterGenerator : callable.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generateLocalVarDecl(this));
        }
        writeCode(writer, callable.generateLocalVarDecl(this));
        writeInitVmCode(writer, callable);
        writeInitMethodCode(writer, callable);
        for (ParameterGenerator parameterGenerator : callable.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generatePreCallCode(this));
        }
        writeCode(writer, callable.generatePreCallCode(this));
        writeCode(writer, callable.generateCallCode(this));
        writeCode(writer, callable.generatePostCallCode(this));
        for (ParameterGenerator parameterGenerator : callable.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generatePostCallCode(this));
        }
        writeCode(writer, callable.generateReturnCode(this));
        writer.print("}\n");
        writer.print("\n");
    }

    private void writeInitVmCode(PrintWriter writer, FunctionGenerator callable) {
        writer.printf("\n");
        if (isVoid(callable.getReturnType())) {
            writer.printf("    if (beam_init_api() != 0) return;\n");
        } else {
            writer.printf("    if (beam_init_api() != 0) return _result;\n");
        }
        writer.printf("\n");
    }

    private void writeLocalMethodVarDecl(PrintWriter writer) {
        writer.printf("    static jmethodID %s = NULL;\n", METHOD_VAR_NAME);
    }

    private void writeInitMethodCode(PrintWriter writer, FunctionGenerator callable) {
        writer.printf("    if (%s == NULL) {\n", METHOD_VAR_NAME);
        writer.printf("        %s = (*jenv)->%s(jenv, %s, \"%s\", \"%s\");\n",
                      METHOD_VAR_NAME,
                      callable.getMemberDoc().isStatic() ? "GetStaticMethodID" : "GetMethodID",
                      callable.getTargetEnclosingClassVarName(),
                      callable.getApiMethod().getJavaName(),
                      callable.getApiMethod().getJavaSignature());
        if (isVoid(callable.getReturnType())) {
            writer.printf("        if (%s == NULL) return;\n", METHOD_VAR_NAME);
        } else {
            writer.printf("        if (%s == NULL) return _result;\n", METHOD_VAR_NAME);
        }
        writer.printf("    }\n");
        writer.printf("\n");
    }

    private void writeCode(PrintWriter writer, String code1) {
        String[] callCode = generateLines(code1);
        for (String line : callCode) {
            writer.printf("    %s\n", line);
        }
    }

    private void generateFileInfo(PrintWriter writer) {
        writer.write(String.format("/*\n" +
                                           " * DO NOT EDIT THIS FILE, IT IS MACHINE-GENERATED\n" +
                                           " * File created at %s using %s\n" +
                                           " */\n", new Date(), ApiGeneratorDoclet.class.getName()));
        writer.write("\n");
    }


    private void printStats() {
        int numClasses = 0;
        int numMethods = 0;
        for (ApiClass apiClass : getApiClasses()) {
            numClasses++;
            numMethods += getFunctionGenerators(apiClass).size();
        }
        System.out.printf("#Classes: %d, #Methods: %d\n", numClasses, numMethods);
    }

    String[] generateLines(String code) {
        if (code == null || code.length() == 0) {
            return new String[0];
        }
        return code.split("\n");
    }

    static boolean isVoid(Type type) {
        return type.qualifiedTypeName().equals("void");
    }

    static boolean isString(Type type) {
        return type.qualifiedTypeName().equals("java.lang.String");
    }

    static boolean isPrimitiveArray(Type type) {
        return type.dimension().equals("[]") && type.isPrimitive();
    }

    static boolean isStringArray(Type type) {
        return type.dimension().equals("[]") && isString(type);
    }

    static boolean isObjectArray(Type type) {
        return type.dimension().equals("[]") && !type.isPrimitive();
    }

    // todo - code duplication in ParameterGenerator
    static String getTargetComponentTypeName(Type type, boolean isParam) {
        if (type.isPrimitive()) {
            final String typeName = type.typeName();
            if (typeName.equals("long")) {
                return "dlong";
            } else {
                return typeName;
            }
        } else {
            if (isString(type)) {
                return isParam ? "const char*" : "char*";
            } else {
                return getTargetClassName(type);
            }
        }
    }

    static String getTargetClassName(Type type) {
        return type.typeName().replace('.', '_');
    }


    private static Map<ApiMethod, String> createFunctionNames(ApiInfo apiInfo) {
        Map<String, List<ApiMethod>> sameTargetFunctionNames = collectApiMethodsWithSameFunctionName(apiInfo);
        return createFunctionNames(apiInfo, sameTargetFunctionNames);
    }

    private static Map<ApiMethod, String> createFunctionNames(ApiInfo apiInfo, Map<String, List<ApiMethod>> sameTargetFunctionNames) {
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        Map<ApiMethod, String> targetFunctionNames = new HashMap<ApiMethod, String>(apiClasses.size() * 100);
        for (ApiClass apiClass : apiClasses) {
            for (ApiMethod apiMethod : apiInfo.getMethodsOf(apiClass)) {
                String functionBaseName = getFunctionBaseName(apiMethod);
                List<ApiMethod> apiMethods = sameTargetFunctionNames.get(functionBaseName);
                if (apiMethods.size() > 1) {
                    for (int i = 0; i < apiMethods.size(); i++) {
                        targetFunctionNames.put(apiMethods.get(i), getFunctionBaseName(apiMethods.get(i)) + (i + 1));
                    }
                } else {
                    targetFunctionNames.put(apiMethods.get(0), getFunctionBaseName(apiMethods.get(0)));
                }
            }
        }
        return targetFunctionNames;
    }

    private static Map<String, List<ApiMethod>> collectApiMethodsWithSameFunctionName(ApiInfo apiInfo) {
        Map<String, List<ApiMethod>> sameTargetFunctionNames = new HashMap<String, List<ApiMethod>>(1000);
        for (ApiClass apiClass : apiInfo.getApiClasses()) {
            for (ApiMethod apiMethod : apiInfo.getMethodsOf(apiClass)) {
                String functionBaseName = getFunctionBaseName(apiMethod);
                List<ApiMethod> apiMethods = sameTargetFunctionNames.get(functionBaseName);
                if (apiMethods == null) {
                    apiMethods = new ArrayList<ApiMethod>(4);
                    sameTargetFunctionNames.put(functionBaseName, apiMethods);
                }
                apiMethods.add(apiMethod);
            }
        }
        return sameTargetFunctionNames;
    }

    private Map<ApiClass, List<FunctionGenerator>> createFunctionGenerators(ApiInfo apiInfo) {
        Map<ApiClass, List<FunctionGenerator>> map = new HashMap<ApiClass, List<FunctionGenerator>>();
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        for (ApiClass apiClass : apiClasses) {
            List<ApiMethod> apiMethods = apiInfo.getMethodsOf(apiClass);
            List<FunctionGenerator> functionGenerators = new ArrayList<FunctionGenerator>();
            for (ApiMethod apiMethod : apiMethods) {
                try {
                    FunctionGenerator functionGenerator = GeneratorFactory.createCodeGenCallable(apiMethod);
                    functionGenerators.add(functionGenerator);
                } catch (GeneratorException e) {
                    System.out.printf("error: %s\n", e.getMessage());
                }
            }
            map.put(apiClass, functionGenerators);
        }
        return map;
    }

}
