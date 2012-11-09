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
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiGeneratorDoclet;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Norman Fomferra
 */
public class ModuleGenerator implements GeneratorContext {

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
    public String getFunctionName(FunctionGenerator generator) {
        return functionNames.get(generator.getApiMethod());
    }

    public List<FunctionGenerator> getFunctionGenerators(ApiClass apiClass) {
        return functionGenerators.get(apiClass);
    }

    static String getCTypeName(Type type) {
        String name = getComponentCTypeName(type);
        return name + type.dimension().replace("[]", "*");
    }

    static String getComponentCClassName(Type type) {
        return type.typeName().replace('.', '_');
    }

    static String getComponentCClassVarName(Type type) {
        return String.format(CLASS_VAR_NAME_PATTERN, getComponentCClassName(type));
    }

    static String getComponentCTypeName(Type type) {
        if (type.isPrimitive()) {
            if (type.typeName().equals("long")) {
                return "dlong";
            } else {
                return type.typeName();
            }
        } else {
            if (isString(type)) {
                return "char*";
            } else {
                return type.typeName().replace('.', '_');
            }
        }
    }

    public static String getFunctionBaseName(ApiInfo apiInfo, ApiMethod apiMethod) {
        String targetTypeName = getComponentCClassName(apiMethod.getEnclosingClass().getType());
        String methodCName = apiInfo.getConfig().getMethodCName(apiMethod.getEnclosingClass().getType().qualifiedTypeName(),
                                                                apiMethod.getJavaName(),
                                                                apiMethod.getJavaSignature());
        if (methodCName.equals("<init>")) {
            methodCName = String.format("new%s", targetTypeName);
        }
        return String.format("%s_%s", targetTypeName, methodCName);
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
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    writer.printf("\t%s\n", getFunctionName(generator));
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
                writer.write(String.format("typedef void* %s;\n", getComponentCClassName(apiClass.getType())));
            }
            writer.write("\n");

            writer.write("\n");
            writer.write("/* Non-API classes used in the API */\n");
            writer.write("typedef void* String;\n");
            for (ApiClass usedApiClass : apiInfo.getUsedNonApiClasses()) {
                if (!isString(usedApiClass.getType())) {
                    writer.write(String.format("typedef void* %s;\n", getComponentCClassName(usedApiClass.getType())));
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
                writer.printf("/* Functions for class %s */\n", getComponentCClassName(apiClass.getType()));
                writer.write("\n");
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    writer.printf("" +
                                          "/**\n" +
                                          " * %s\n" +
                                          " */\n", generator.getMemberDoc().getRawCommentText());
                    generateFunctionDeclaration(writer, generator);
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
                                           getComponentCClassVarName(apiClass.getType())));
            }
            writer.printf("/* Other Java classes used in the API. */\n");
            writer.write(String.format("static jclass %s;\n",
                                       String.format(CLASS_VAR_NAME_PATTERN, "String")));
            for (ApiClass usedApiClass : apiInfo.getUsedNonApiClasses()) {
                writer.write(String.format("static jclass %s;\n",
                                           getComponentCClassVarName(usedApiClass.getType())));
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
                              getComponentCClassVarName(apiClass.getType()),
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
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    generateFunctionDefinition(generator, writer);
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

    void generateFunctionDeclaration(PrintWriter writer, FunctionGenerator generator) {
        writer.printf("%s;\n", generator.generateFunctionSignature(this));
    }

    private void generateFunctionDefinition(FunctionGenerator generator, PrintWriter writer) throws IOException {
        writer.printf("%s\n", generator.generateFunctionSignature(this));
        writer.print("{\n");
        writeLocalMethodVarDecl(writer);
        for (ParameterGenerator parameterGenerator : generator.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generateLocalVarDecl(this));
        }
        writeCode(writer, generator.generateLocalVarDecl(this));
        writeInitVmCode(writer, generator);
        writeInitMethodCode(writer, generator);
        for (ParameterGenerator parameterGenerator : generator.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generatePreCallCode(this));
        }
        writeCode(writer, generator.generatePreCallCode(this));
        writeCode(writer, generator.generateCallCode(this));
        writeCode(writer, generator.generatePostCallCode(this));
        for (ParameterGenerator parameterGenerator : generator.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generatePostCallCode(this));
        }
        writeCode(writer, generator.generateReturnCode(this));
        writer.print("}\n");
        writer.print("\n");
    }

    private void writeInitVmCode(PrintWriter writer, FunctionGenerator generator) {
        writer.printf("\n");
        if (isVoid(generator.getReturnType())) {
            writer.printf("    if (beam_init_api() != 0) return;\n");
        } else {
            writer.printf("    if (beam_init_api() != 0) return _result;\n");
        }
        writer.printf("\n");
    }

    private void writeLocalMethodVarDecl(PrintWriter writer) {
        writer.printf("    static jmethodID %s = NULL;\n", METHOD_VAR_NAME);
    }

    private void writeInitMethodCode(PrintWriter writer, FunctionGenerator generator) {
        writer.printf("    if (%s == NULL) {\n", METHOD_VAR_NAME);
        writer.printf("        %s = (*jenv)->%s(jenv, %s, \"%s\", \"%s\");\n",
                      METHOD_VAR_NAME,
                      generator.getMemberDoc().isStatic() ? "GetStaticMethodID" : "GetMethodID",
                      getComponentCClassVarName(generator.getEnclosingClass().getType()),
                      generator.getApiMethod().getJavaName(),
                      generator.getApiMethod().getJavaSignature());
        if (isVoid(generator.getReturnType())) {
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


    private static Map<ApiMethod, String> createFunctionNames(ApiInfo apiInfo) {
        Map<String, Set<ApiMethod>> sameTargetFunctionNames = collectApiMethodsWithSameFunctionName(apiInfo);

        ///////////////////////////////////////////
        final Set<String> keySet = sameTargetFunctionNames.keySet();
        final String[] keys = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keys);
        ApiClass lastEnclosingClass = null;
        for (String key : keys) {
            final Set<ApiMethod> apiMethods = sameTargetFunctionNames.get(key);
            if (apiMethods.size() > 1) {
                for (ApiMethod apiMethod : apiMethods) {
                    final ApiClass enclosingClass = apiMethod.getEnclosingClass();
                    boolean classChange =  !enclosingClass.equals(lastEnclosingClass);
                    lastEnclosingClass = enclosingClass;

                    if (classChange) {
                        System.out.printf("</class>\n");
                        System.out.printf("<class name=\"%s\">\n", enclosingClass.getJavaName());
                    }

                    System.out.printf("    <method name=\"%s\" sig=\"%s\" renameTo=\"%s...\"/>\n",
                                      apiMethod.getJavaName().equals("<init>") ? "&lt;init&gt;" : apiMethod.getJavaName(),
                                      apiMethod.getJavaSignature(),
                                      apiMethod.getJavaName().equals("<init>") ? "new" : apiMethod.getJavaName());

                }
            }
        }
        ///////////////////////////////////////////

        return createFunctionNames(apiInfo, sameTargetFunctionNames);
    }

    private static Map<ApiMethod, String> createFunctionNames(ApiInfo apiInfo, Map<String, Set<ApiMethod>> sameTargetFunctionNames) {
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        Map<ApiMethod, String> targetFunctionNames = new HashMap<ApiMethod, String>(apiClasses.size() * 100);
        for (ApiClass apiClass : apiClasses) {
            for (ApiMethod apiMethod : apiInfo.getMethodsOf(apiClass)) {
                String functionBaseName = getFunctionBaseName(apiInfo, apiMethod);
                Set<ApiMethod> apiMethods = sameTargetFunctionNames.get(functionBaseName);
                if (apiMethods.size() > 1) {
                    int index = 1;
                    for (ApiMethod m : apiMethods) {
                        final String renameTo = getFunctionBaseName(apiInfo, m) + index;
                        targetFunctionNames.put(m, renameTo);
                        index++;
                    }
                } else if (apiMethods.size() == 1) {
                    final ApiMethod m = apiMethods.iterator().next();
                    targetFunctionNames.put(m, getFunctionBaseName(apiInfo, m));
                }
            }
        }
        return targetFunctionNames;
    }

    private static Map<String, Set<ApiMethod>> collectApiMethodsWithSameFunctionName(ApiInfo apiInfo) {
        Map<String, Set<ApiMethod>> sameTargetFunctionNames = new HashMap<String, Set<ApiMethod>>(1000);
        for (ApiClass apiClass : apiInfo.getApiClasses()) {
            for (ApiMethod apiMethod : apiInfo.getMethodsOf(apiClass)) {
                String functionBaseName = getFunctionBaseName(apiInfo, apiMethod);
                Set<ApiMethod> apiMethods = sameTargetFunctionNames.get(functionBaseName);
                if (apiMethods == null) {
                    apiMethods = new TreeSet<ApiMethod>();
                    sameTargetFunctionNames.put(functionBaseName, apiMethods);
                }
                apiMethods.add(apiMethod);
            }
        }
        return sameTargetFunctionNames;
    }

    private Map<ApiClass, List<FunctionGenerator>> createFunctionGenerators(ApiInfo apiInfo) {
        GeneratorFactory factory = new GeneratorFactory(apiInfo);
        Map<ApiClass, List<FunctionGenerator>> map = new HashMap<ApiClass, List<FunctionGenerator>>();
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        for (ApiClass apiClass : apiClasses) {
            List<ApiMethod> apiMethods = apiInfo.getMethodsOf(apiClass);
            List<FunctionGenerator> functionGenerators = new ArrayList<FunctionGenerator>();
            for (ApiMethod apiMethod : apiMethods) {
                try {
                    FunctionGenerator functionGenerator = factory.createFunctionGenerator(apiMethod);
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
