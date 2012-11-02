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

package org.esa.beam.extapi.gen;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * @author Norman Fomferra
 */
class Generator implements GeneratorContext {

    public static final String BEAM_CAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_CAPI_NAME = "beam_capi";

    public static final String SELF_VAR_NAME = "_self";
    public static final String METHOD_VAR_NAME = "_method";
    public static final String RESULT_VAR_NAME = "_result";
    public static final String CLASS_VAR_NAME_PATTERN = "class%s";

    final private GeneratorInfo generatorInfo;

    public Generator(GeneratorInfo generatorInfo) {
        this.generatorInfo = generatorInfo;
    }

    static String getTargetTypeName(Type type) {
        String name = getTargetComponentTypeName(type, false);
        return name + type.dimension().replace("[]", "*");
    }

    static String getTypeSignature(ExecutableMemberDoc memberDoc) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Parameter parameter : memberDoc.parameters()) {
            sb.append(getTypeSignature(parameter.type()));
        }
        sb.append(')');
        if (memberDoc instanceof MethodDoc) {
            sb.append(getTypeSignature(((MethodDoc) memberDoc).returnType()));
        } else {
            sb.append('V');
        }
        return sb.toString();
    }

    private static String getTypeSignature(Type type) {
        String comp;
        if (type.isPrimitive()) {
            if ("boolean".equals(type.typeName())) {
                comp = "Z";
            } else if ("byte".equals(type.typeName())) {
                comp = "B";
            } else if ("char".equals(type.typeName())) {
                comp = "Constructor";
            } else if ("short".equals(type.typeName())) {
                comp = "S";
            } else if ("int".equals(type.typeName())) {
                comp = "I";
            } else if ("long".equals(type.typeName())) {
                comp = "J";
            } else if ("float".equals(type.typeName())) {
                comp = "F";
            } else if ("double".equals(type.typeName())) {
                comp = "D";
            } else if ("void".equals(type.typeName())) {
                comp = "V";
            } else {
                throw new IllegalStateException();
            }
        } else {
            comp = "L" + type.qualifiedTypeName().replace('.', '/') + ";";
        }
        if (!type.dimension().isEmpty()) {
            return type.dimension().replace("]", "") + comp;
        }
        return comp;
    }

    static String getTargetClassVarName(Type type) {
        return String.format(CLASS_VAR_NAME_PATTERN, getTargetClassName(type));
    }

    @Override
    public String getTargetFunctionName(CodeGenCallable callable) {
        return generatorInfo.getExternalFunctionName(callable);
    }

    public List<ApiClass> getApiClasses() {
        return generatorInfo.getApiClasses();
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
                                  "\tbeam_destroy_jvm\n");
            for (ApiClass apiClass : getApiClasses()) {
                for (CodeGenCallable callable : apiClass.getCallableList()) {
                    writer.printf("\t%s\n", getTargetFunctionName(callable));
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
            writeResource(writer, "Generator-stubs.h");
            writer.write("\n");

            writer.write("\n");
            writer.write("/* Wrapped API classes */\n");
            for (ApiClass apiClass : getApiClasses()) {
                writer.write(String.format("typedef void* %s;\n", apiClass.getTargetComponentTypeName()));
            }
            writer.write("\n");

            writer.write("\n");
            writer.write("/* Non-API classes used in the API */\n");
            for (ApiClass usedApiClass : generatorInfo.getUsedApiClasses()) {
                if (!getApiClasses().contains(usedApiClass) && !isString(usedApiClass.getType())) {
                    writer.write(String.format("typedef void* %s;\n", usedApiClass.getTargetComponentTypeName()));
                }
            }
            writer.write("\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // Generate function declarations
            //
            for (ApiClass apiClass : getApiClasses()) {
                writer.write("\n");
                writer.printf("/* Functions for class %s */\n", apiClass.getTargetComponentTypeName());
                writer.write("\n");
                for (CodeGenCallable callable : apiClass.getCallableList()) {
                    writer.printf("" +
                                          "/**\n" +
                                          " * %s\n" +
                                          " */\n", callable.getMemberDoc().commentText());
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
            writeResource(writer, "Generator-stubs-1.c");
            writer.printf("\n");

            writer.printf("/* Java API classes. */\n");
            for (ApiClass apiClass : getApiClasses()) {
                writer.write(String.format("static jclass %s;\n",
                                           getTargetClassVarName(apiClass.getType())));
            }
            writer.printf("/* Other Java classes used in the API. */\n");
            writer.write(String.format("static jclass %s;\n",
                                       String.format(CLASS_VAR_NAME_PATTERN, "String")));
            for (ApiClass usedApiClass : generatorInfo.getUsedApiClasses()) {
                writer.write(String.format("static jclass %s;\n",
                                           getTargetClassVarName(usedApiClass.getType())));
            }
            writer.write("\n");

            writer.printf("\n");
            writeResource(writer, "Generator-stubs-2.c");
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
                              getTargetClassVarName(apiClass.getType()),
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
                for (CodeGenCallable callable : apiClass.getCallableList()) {
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

    void generateFunctionDeclaration(PrintWriter writer, CodeGenCallable callable) {
        writer.printf("%s;\n", callable.generateFunctionSignature(this));
    }

    private void generateFunctionDefinition(CodeGenCallable callable, PrintWriter writer) throws IOException {
        writer.printf("%s\n", callable.generateFunctionSignature(this));
        writer.print("{\n");
        writeLocalMethodVarDecl(writer);
        for (CodeGenParameter codeGenParameter : callable.getParameters()) {
            writeCode(writer, codeGenParameter.generateLocalVarDecl(this));
        }
        writeCode(writer, callable.generateLocalVarDecl(this));
        writeInitVmCode(writer, callable);
        writeInitMethodCode(writer, callable);
        for (CodeGenParameter codeGenParameter : callable.getParameters()) {
            writeCode(writer, codeGenParameter.generatePreCallCode(this));
        }
        writeCode(writer, callable.generatePreCallCode(this));
        writeCode(writer, callable.generateCallCode(this));
        writeCode(writer, callable.generatePostCallCode(this));
        for (CodeGenParameter codeGenParameter : callable.getParameters()) {
            writeCode(writer, codeGenParameter.generatePostCallCode(this));
        }
        writeCode(writer, callable.generateReturnCode(this));
        writer.print("}\n");
        writer.print("\n");
    }

    private void writeInitVmCode(PrintWriter writer, CodeGenCallable callable) {
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

    private void writeInitMethodCode(PrintWriter writer, CodeGenCallable callable) {
        writer.printf("\n");
        writer.printf("    if (%s == NULL) {\n", METHOD_VAR_NAME);
        writer.printf("        %s = (*jenv)->%s(jenv, %s, \"%s\", \"%s\");\n",
                      METHOD_VAR_NAME,
                      callable.getMemberDoc().isStatic() ? "GetStaticMethodID" : "GetMethodID",
                      callable.getTargetEnclosingClassVarName(),
                      callable.getJavaName(),
                      callable.getJavaSignature());
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
                                           " */\n", new Date(), GeneratorDoclet.class.getName()));
        writer.write("\n");
    }


    private void printStats() {
        int numClasses = 0;
        int numMethods = 0;
        for (ApiClass apiClass : getApiClasses()) {
            numClasses++;
            numMethods += apiClass.getCallableList().size();
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

    // todo - code duplication in CodeGenParameter
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


}
