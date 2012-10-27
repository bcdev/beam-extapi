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

import com.sun.javadoc.Doclet;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * @author Norman Fomferra
 */
class Generator extends Doclet {

    public static final String BEAM_CAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_CAPI_NAME = "beam_capi";

    final private GeneratorInfo generatorInfo;

    public Generator(GeneratorInfo generatorInfo) {
        this.generatorInfo = generatorInfo;
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
                for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                    writer.printf("\t%s\n", getExternalFunctionName(apiMethod));
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
            writer.write("/* Wrapped API classes */\n");
            for (ApiClass apiClass : getApiClasses()) {
                writer.write(String.format("typedef void* %s;\n", getExternalTypeName(apiClass)));
            }
            writer.write("\n");

            writer.write("\n");
            writer.write("typedef char byte;\n");
            writer.write("typedef unsigned char boolean;\n");
            writer.write("typedef long long dlong;\n");
            writer.write("\n");

            writer.write("int beam_init_vm();\n");

            writer.write("\n");
            writer.write("/* Non-API classes used in the API */\n");
            for (ApiClass usedClass : generatorInfo.getUsedClasses()) {
                if (!getApiClasses().contains(usedClass) && !isString(usedClass.getType())) {
                    writer.write(String.format("typedef void* %s;\n", getExternalTypeName(usedClass)));
                }
            }
            writer.write("\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // Generate function declarations
            //
            for (ApiClass apiClass : getApiClasses()) {
                writer.write("\n");
                writer.printf("/* Functions for class %s */\n", getExternalTypeName(apiClass));
                writer.write("\n");
                for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                    generateFunctionDeclaration(writer, apiMethod);
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
            for (ApiClass apiClass : getApiClasses()) {
                String classVarName = getExternalClassVarName(apiClass);
                writer.write(String.format("static jclass %s;\n", classVarName));
            }
            writer.write("\n");

            writer.write("static JavaVM* jvm = NULL;\n");
            writer.write("static JNIEnv* jenv = NULL;\n");

            writer.write("\n");
            writer.write("char* beam_allocate_string(jstring str)\n" +
                                 "{\n" +
                                 "    int len = (*jenv)->GetStringUTFLength(jenv, str);\n" +
                                 "    char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);\n" +
                                 "    char* result = (char*) malloc((len + 1) * sizeof (char));\n" +
                                 "    if (result != NULL) strcpy(result, chars);\n" +
                                 "    (*jenv)->ReleaseStringUTFChars(jenv, str, chars);\n" +
                                 "    return result;\n" +
                                 "}\n" +
                                 "\n");
            writer.write("\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // beam_init_api()
            //
            writer.write("int beam_init_api()\n{\n");

            writer.printf("    if (beam_init_vm() != 0) return 1;\n");

            int errCode = 1000;
            for (ApiClass apiClass : getApiClasses()) {
                String classVarName = getExternalClassVarName(apiClass);
                writer.write(String.format("    %s = (*jenv)->FindClass(jenv, \"%s\");\n",
                                           classVarName, apiClass.getResourceName()));
                writer.write(String.format("    if (%s == NULL) return %d;\n",
                                           classVarName, errCode));
                writer.write("\n");
                errCode++;
            }
            writer.write("    return 0;\n");
            writer.write("}\n\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // Generate function code
            //
            for (ApiClass apiClass : getApiClasses()) {
                for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                    generateFunctionDefinition(apiMethod, writer);
                }
            }
        } finally {
            writer.close();
        }
    }

    void generateFunctionDeclaration(PrintWriter writer, ApiMethod apiMethod) {
        generateFunctionSignature(apiMethod, writer);
        writer.write(";\n");
    }

    private void generateFunctionDefinition(ApiMethod apiMethod, PrintWriter writer) throws IOException {

        final boolean returnsVoidValue = isVoid(apiMethod.getMethodDoc().returnType());
        final boolean returnsStringValue = isString(apiMethod.getMethodDoc().returnType());
        final boolean returnsPrimitive = apiMethod.getMethodDoc().returnType().isPrimitive();
        final boolean methodIsStatic = apiMethod.getMethodDoc().isStatic();
        final String externalClassVarName = getExternalClassVarName(apiMethod.getApiClass());

        generateFunctionSignature(apiMethod, writer);

        writer.printf("\n{\n");

        // declare local variables: converted strings, the return value and the (static) method object to be called.
        //
        for (Parameter parameter : apiMethod.getMethodDoc().parameters()) {
            if (isString(parameter.type())) {
                writer.printf("    jstring %s = NULL;\n", getWrappedParameterName(parameter));
            }
        }
        if (!returnsVoidValue) {
            String s = getExternalTypeName(apiMethod.getMethodDoc().returnType(), false);
            writer.printf("    %s _result = (%s) 0;\n", s, s);
            if (returnsStringValue) {
                writer.printf("    jstring resultString = NULL;\n");
            }
        }
        writer.printf("    static jmethodID method = NULL;\n");
        writer.printf("\n");

        // make sure beam_init_vm() has been called
        //
        if (returnsVoidValue) {
            writer.printf("    if (beam_init_vm() != 0) return;\n");
        } else {
            writer.printf("    if (beam_init_vm() != 0) return _result;\n");
        }
        writer.printf("\n");

        // make sure 'method' variable is set
        //
        writer.printf("    if (method == NULL) {\n");
        writer.printf("        method = (*jenv)->%s(jenv, %s, \"%s\", \"%s\");\n",
                      methodIsStatic ? "GetStaticMethodID" : "GetMethodID",
                      externalClassVarName,
                      apiMethod.getJavaName(),
                      apiMethod.getJavaSignature());
        if (returnsVoidValue) {
            writer.printf("        if (method == NULL) return;\n");
        } else {
            writer.printf("        if (method == NULL) return _result;\n");
        }
        writer.printf("    }\n");
        writer.printf("\n");

        // convert 'const char*' C string parameters to Java strings
        //
        for (Parameter parameter : apiMethod.getMethodDoc().parameters()) {
            if (isString(parameter.type())) {
                writer.printf("    %s = (*jenv)->NewStringUTF(jenv, %s);\n",
                              getWrappedParameterName(parameter), parameter.name());
            }
        }

        // construct list of method call arguments
        //
        StringBuilder argList = new StringBuilder();
        if (!methodIsStatic) {
            argList.append(", _self");
        }
        for (Parameter parameter : apiMethod.getMethodDoc().parameters()) {
            argList.append(", ");
            argList.append(getWrappedParameterName(parameter));
        }

        // generate actual method call
        //
        if (returnsVoidValue) {
            writer.printf("    (*jenv)->%s(jenv, %s, method%s);\n",
                          methodIsStatic ? "CallStaticVoidMethod" : "CallVoidMethod",
                          externalClassVarName,
                          argList);
        } else {
            String typeName;
            if (returnsPrimitive) {
                String s = apiMethod.getMethodDoc().returnType().typeName();
                typeName = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            } else {
                typeName = "Object";
            }
            writer.printf("    %s = (*jenv)->%s(jenv, %s, method%s);\n",
                          returnsStringValue ? "resultString" : "_result",
                          String.format(methodIsStatic ? "CallStatic%sMethod" : "Call%sMethod", typeName),
                          externalClassVarName,
                          argList);

            if (returnsStringValue) {
                // Strings are returned as newly allocated pointers to heap space!
                writer.printf("    _result = beam_allocate_string(resultString);\n");
            } else if (!returnsPrimitive) {
                // Increase global ref counter of returned object, so that JVM's GC doesn't throw it away while it
                // is used externally outside of the JVM.
                writer.printf("    _result = _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;\n");
            }
        }

        // generate function end
        //
        if (!returnsVoidValue) {
            writer.printf("    return _result;\n");
        }
        writer.printf("}\n");
        writer.printf("\n");
    }

    private void generateFunctionSignature(ApiMethod apiMethod, PrintWriter writer) {
        StringBuilder parameterList = new StringBuilder();

        if (!apiMethod.getMethodDoc().isStatic()) {
            parameterList.append(getExternalClassName(apiMethod));
            parameterList.append(" _self");
        }
        Parameter[] parameters = apiMethod.getMethodDoc().parameters();
        for (Parameter parameter : parameters) {
            if (parameterList.length() > 0) {
                parameterList.append(", ");
            }
            parameterList.append(getExternalTypeName(parameter.type(), true));
            parameterList.append(" ");
            parameterList.append(parameter.name());
        }

        writer.printf("%s %s(%s)",
                      getExternalTypeName(apiMethod.getMethodDoc().returnType(), false),
                      getExternalFunctionName(apiMethod),
                      parameterList);
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
            numMethods += apiClass.getApiMethods().size();
        }
        System.out.printf("#Classes: %d, #Methods: %d\n", numClasses, numMethods);
    }

    static String getWrappedParameterName(Parameter parameter) {
        if (isString(parameter.type())) {
            return parameter.name() + "String";
        }
        return parameter.name();
    }

    static boolean isVoid(Type type) {
        return type.qualifiedTypeName().equals("void");
    }

    static boolean isString(Type type) {
        return type.qualifiedTypeName().equals("java.lang.String");
    }

    String getExternalFunctionName(ApiMethod apiMethod) {
        return generatorInfo.getExternalFunctionName(apiMethod);
    }

    static String getExternalClassVarName(ApiClass apiClass) {
        return getExternalTypeName(apiClass) + "_class";
    }

    static String getExternalTypeName(ApiClass apiClass) {
        return getExternalTypeName(apiClass.getType(), true);
    }

    static String getExternalClassName(ApiMethod apiMethod) {
        return getExternalTypeName(apiMethod.getApiClass());
    }

    static String getExternalBaseName(ApiMethod apiMethod) {
        return getExternalClassName(apiMethod) + "_" + apiMethod.getJavaName();
    }

    static String getExternalTypeName(Type type, boolean isParam) {
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
                return type.typeName().replace('.', '_');
            }
        }
    }
}
