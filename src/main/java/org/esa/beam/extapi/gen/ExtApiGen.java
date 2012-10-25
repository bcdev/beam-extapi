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

import com.sun.javadoc.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Norman Fomferra
 */
public class ExtApiGen extends Doclet {

    public static final String BEAM_CAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_CAPI_NAME = "beam_capi";

    private RootDoc rootDoc;
    private ArrayList<ApiClass> apiClasses;
    private Set<ApiClass> usedClasses;
    private Set<String> wrappedClassNames;
    private Map<ApiMethod, String> externalFunctionNames;

    public static void main(String[] args) {

        com.sun.tools.javadoc.Main.main(new String[]{
                "-doclet", ExtApiGen.class.getName(),
                "-sourcepath", "" +
                "../beam/beam/beam-core/src/main/java;" +
                "../beam/beam/beam-gpf/src/main/java",
                "org.esa.beam.framework.datamodel",
                "org.esa.beam.framework.dataio",
                "org.esa.beam.framework.gpf",
                "org.esa.beam.util",
        });
    }

    private ExtApiGen(RootDoc rootDoc) throws IOException {
        this.rootDoc = rootDoc;
        apiClasses = new ArrayList<ApiClass>(100);
        usedClasses = new HashSet<ApiClass>();

        final Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("ExtApiClasses.txt"));
        wrappedClassNames = properties.stringPropertyNames();
    }

    private void start() throws Exception {
        collectAll();
        writeHeader();
        writeSource();
        writeWinDef();
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
            for (ApiClass apiClass : apiClasses) {
                for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                    writer.printf("\t%s\n", getExternalFunctionName(apiMethod));
                }
            }
        } finally {
            writer.close();
        }
    }

    String getExternalFunctionName(ApiMethod apiMethod) {
        return externalFunctionNames.get(apiMethod);
    }

    private void printStats() {
        int numClasses = 0;
        int numMethods = 0;
        for (ApiClass apiClass : apiClasses) {
            numClasses++;
            numMethods += apiClass.getApiMethods().size();
        }
        System.out.printf("#Classes: %d, #Methods: %d\n", numClasses, numMethods);
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
            for (ApiClass apiClass : apiClasses) {
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
            for (ApiClass apiClass : apiClasses) {
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
            for (ApiClass apiClass : apiClasses) {
                for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                    generateApiMethod(apiMethod, writer);
                }
            }
        } finally {
            writer.close();
        }
    }

    private String getExternalClassVarName(ApiClass apiClass) {
        return getExternalTypeName(apiClass) + "_class";
    }

    private void generateApiMethod(ApiMethod apiMethod, PrintWriter writer) throws IOException {

        generateFunctionSignature(apiMethod, writer);

        boolean returnsVoidValue = isVoid(apiMethod.getMethodDoc().returnType());
        boolean returnsStringValue = isString(apiMethod.getMethodDoc().returnType());
        boolean returnsPrimitive = apiMethod.getMethodDoc().returnType().isPrimitive();
        boolean methodIsStatic = apiMethod.getMethodDoc().isStatic();

        String externalClassVarName = getExternalClassVarName(apiMethod.getApiClass());

        writer.printf("\n{\n");
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
        if (returnsVoidValue) {
            writer.printf("    if (beam_init_vm() != 0) return;\n");
        } else {
            writer.printf("    if (beam_init_vm() != 0) return _result;\n");
        }
        writer.printf("\n");
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

        for (Parameter parameter : apiMethod.getMethodDoc().parameters()) {
            if (isString(parameter.type())) {
                writer.printf("    %s = (*jenv)->NewStringUTF(jenv, %s);\n",
                              getWrappedParameterName(parameter), parameter.name());
            }
        }

        StringBuilder argList = new StringBuilder();
        if (!methodIsStatic) {
            argList.append(", _self");
        }
        for (Parameter parameter : apiMethod.getMethodDoc().parameters()) {
            argList.append(", ");
            argList.append(getWrappedParameterName(parameter));
        }

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
                writer.printf("    _result = beam_allocate_string(resultString);\n");
            } else if (!returnsPrimitive) {
                writer.printf("    _result = _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;\n");
            }
        }

        if (!returnsVoidValue) {
            writer.printf("    return _result;\n");
        }

        writer.printf("}\n");
        writer.printf("\n");
    }

    private String getExternalClassName(ApiMethod apiMethod) {
        return getExternalTypeName(apiMethod.getApiClass());
    }

    private static String getWrappedParameterName(Parameter parameter) {
        if (isString(parameter.type())) {
            return parameter.name() + "String";
        }
        return parameter.name();
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

    private void writeHeader() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".h")));
        try {
            generateFileInfo(writer);

            writer.write("\n");
            writer.write("/* Wrapped API classes */\n");
            for (ApiClass apiClass : apiClasses) {
                writer.write(String.format("typedef void* %s;\n", getExternalTypeName(apiClass)));
            }
            writer.write("\n");

            writer.write("\n");
            writer.write("typedef unsigned char boolean;\n");
            writer.write("typedef long long dlong;\n");
            writer.write("\n");

            writer.write("int beam_init_vm();\n");

            writer.write("\n");
            writer.write("/* Non-API classes used in the API */\n");
            for (ApiClass usedClass : usedClasses) {
                if (!apiClasses.contains(usedClass) && !isString(usedClass.getType())) {
                    writer.write(String.format("typedef void* %s;\n", getExternalTypeName(usedClass)));
                }
            }
            writer.write("\n");

            /////////////////////////////////////////////////////////////////////////////////////
            // Generate function declarations
            //
            for (ApiClass apiClass : apiClasses) {
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

    private void generateFileInfo(PrintWriter writer) {
        writer.write(String.format("/*\n" +
                                           " * DO NOT EDIT THIS FILE, IT IS MACHINE-GENERATED\n" +
                                           " * File created at %s using %s\n" +
                                           " */\n", new Date(), getClass().getName()));
        writer.write("\n");
    }

    private void generateFunctionDeclaration(PrintWriter writer, ApiMethod apiMethod) {
        generateFunctionSignature(apiMethod, writer);
        writer.write(";\n");
    }

    private void collectAll() throws ClassNotFoundException, NoSuchMethodException {
        final ClassDoc[] classDocs = rootDoc.classes();

        Map<String, List<ApiMethod>> sameExternalFunctionNames = new HashMap<String, List<ApiMethod>>(1000);

        for (ClassDoc classDoc : classDocs) {

            final MethodDoc[] methodDocs = classDoc.methods();

            if (classDoc.isPublic() && wrappedClassNames.contains(classDoc.qualifiedTypeName())) {

                ApiClass apiClass = new ApiClass(classDoc);
                apiClasses.add(apiClass);

                for (MethodDoc methodDoc : methodDocs) {
                    if (methodDoc.isPublic()) {

                        Tag[] deprecatedTags = methodDoc.tags("deprecated");
                        if (deprecatedTags.length == 0) {

                            final Type retType = methodDoc.returnType();

                            ApiMethod apiMethod = new ApiMethod(apiClass, methodDoc);
                            apiClass.addApiMethod(apiMethod);

                            List<ApiMethod> apiMethods = sameExternalFunctionNames.get(getExternalBaseName(apiMethod));
                            if (apiMethods == null) {
                                apiMethods = new ArrayList<ApiMethod>(4);
                                sameExternalFunctionNames.put(getExternalBaseName(apiMethod), apiMethods);
                            }
                            apiMethods.add(apiMethod);

                            if (!retType.isPrimitive()) {
                                usedClasses.add(new ApiClass(retType));
                            }

                            final Parameter[] parameters = methodDoc.parameters();
                            for (Parameter parameter : parameters) {
                                final Type type = parameter.type();
                                if (!type.isPrimitive()) {
                                    usedClasses.add(new ApiClass(type));
                                }
                            }
                        } else {
                            System.out.printf("Ignored deprecated method: %s.%s\n", classDoc.qualifiedTypeName(), methodDoc.name());
                        }
                    }
                }

            } else {
                System.out.printf("Ignored non-API class: %s\n", classDoc.qualifiedTypeName());
            }
        }
        Collections.sort(this.apiClasses);

        externalFunctionNames = new HashMap<ApiMethod, String>(apiClasses.size() * 100);
        for (ApiClass apiClass : apiClasses) {
            for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                List<ApiMethod> apiMethods = sameExternalFunctionNames.get(getExternalBaseName(apiMethod));
                if (apiMethods.size() > 1) {
                    for (int i = 0; i < apiMethods.size(); i++) {
                        externalFunctionNames.put(apiMethods.get(i), getExternalBaseName(apiMethods.get(i)) + (i + 1));
                    }
                } else {
                    externalFunctionNames.put(apiMethods.get(0), getExternalBaseName(apiMethods.get(0)));
                }
            }
        }
    }

    private String getExternalBaseName(ApiMethod apiMethod) {
        return getExternalClassName(apiMethod) + "_" + apiMethod.getJavaName();
    }

    private String getExternalTypeName(ApiClass apiClass) {
        return getExternalTypeName(apiClass.getType(), true);
    }

    private String getExternalTypeName(Type type, boolean isParam) {
        if (type.isPrimitive()) {
            final String typeName = type.typeName();
            if (typeName.equals("byte")) {
                return "char";
            } else if (typeName.equals("long")) {
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

    private static boolean isVoid(Type type) {
        return type.qualifiedTypeName().equals("void");
    }


    private static boolean isString(Type type) {
        return type.qualifiedTypeName().equals("java.lang.String");
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean start(RootDoc root) {
        try {
            new ExtApiGen(root).start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static int optionLength(String optionName) {
        return 0;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean validOptions(String[][] options,
                                       DocErrorReporter docErrorReporter) {
        for (int i = 0; i < options.length; i++) {
            for (int j = 0; j < options[i].length; j++) {
                docErrorReporter.printWarning("options[" + i + "][" + j + "] = " + options[i][j]);
            }
        }
        return true;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }
}
