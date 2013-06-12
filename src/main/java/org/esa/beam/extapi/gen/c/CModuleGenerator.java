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

import com.sun.javadoc.FieldDoc;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiConstant;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.FunctionGenerator;
import org.esa.beam.extapi.gen.FunctionWriter;
import org.esa.beam.extapi.gen.JavadocHelpers;
import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.TargetCFile;
import org.esa.beam.extapi.gen.TargetCHeaderFile;
import org.esa.beam.extapi.gen.TargetFile;
import org.esa.beam.extapi.gen.TemplateEval;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Norman Fomferra
 */
public class CModuleGenerator extends ModuleGenerator {

    // TODO: move the following constants into ApiGeneratorDoclet-config.xml (nf, 29.04.2013)
    public static final String BEAM_CAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_CAPI_NAME = "beam_capi";
    public static final String THIS_VAR_NAME = "_this";
    public static final String METHOD_VAR_NAME = "_method";
    public static final String RESULT_VAR_NAME = "_result";
    public static final String[] JAVA_LANG_CLASSES = new String[]{"Boolean", "Byte", "Character", "Short", "Integer", "Long", "Float", "Double", "String"};
    public static final String[] JAVA_UTIL_CLASSES = new String[]{"HashMap", "HashSet"};

    private final Map<ApiMethod, String> functionNames;

    public CModuleGenerator(ApiInfo apiInfo) {
        super(apiInfo, new CFunctionGeneratorFactory(apiInfo));
        this.functionNames = createFunctionNames(apiInfo);
        getTemplateEval().add("libName", BEAM_CAPI_NAME);
        getTemplateEval().add("libNameUC", BEAM_CAPI_NAME.toUpperCase().replace("-", "_"));
    }

    @Override
    public void run() throws IOException {
        writeWinDef();
        writeCHeader();
        writeCSource();
        printStats();
    }

    @Override
    public String getFunctionNameFor(ApiMethod apiMethod) {
        return functionNames.get(apiMethod);
    }

    private static String getFunctionBaseName(ApiInfo apiInfo, ApiMethod apiMethod) {
        String targetTypeName = getComponentCClassName(apiMethod.getEnclosingClass().getType());
        String methodCName = apiInfo.getConfig().getFunctionName(apiMethod.getEnclosingClass().getType().qualifiedTypeName(),
                                                                 apiMethod.getJavaName(),
                                                                 apiMethod.getJavaSignature());
        if (methodCName.equals("<init>")) {
            methodCName = String.format("new%s", targetTypeName);
        }
        return String.format("%s_%s", targetTypeName, methodCName);
    }

    private void writeWinDef() throws IOException {
        new TargetFile(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".def") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "CModuleGenerator-stub-init.def");
                for (ApiClass apiClass : getApiClasses()) {
                    for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                        writer.printf("\t%s\n", getFunctionNameFor(generator.getApiMethod()));
                    }
                }
            }
        }.create();
    }

    private void writeCHeader() throws IOException {
        new TargetCHeaderFile(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".h") {
            @Override
            protected void writeContent() throws IOException {
                CModuleGenerator.this.writeCHeaderContents(writer);
            }
        }.create();
    }

    Set<String> getCoreJavaClassNames() {
        Set<String> coreJavaClassNames = new HashSet<String>();
        for (String simpleClassName : JAVA_LANG_CLASSES) {
            coreJavaClassNames.add("java.lang." + simpleClassName);
        }
        for (String simpleClassName : JAVA_UTIL_CLASSES) {
            coreJavaClassNames.add("java.util." + simpleClassName);
        }
        return coreJavaClassNames;
    }

    private void writeClassDefinitions(PrintWriter writer, boolean header) {

        String extDecl = header ? "extern " : "";

        writer.printf("/* java.lang classes. */\n");
        for (String simpleClassName : JAVA_LANG_CLASSES) {
            writer.write(String.format("%sjclass %s;\n",
                                       extDecl, String.format(CLASS_VAR_NAME_PATTERN, simpleClassName)));
        }
        writer.printf("\n");

        writer.printf("/* java.util classes. */\n");
        for (String simpleClassName : JAVA_UTIL_CLASSES) {
            writer.write(String.format("%sjclass %s;\n",
                                       extDecl, String.format(CLASS_VAR_NAME_PATTERN, simpleClassName)));
        }
        writer.printf("\n");

        final Set<String> coreJavaClassNames = getCoreJavaClassNames();

        writer.printf("/* API classes. */\n");
        for (ApiClass apiClass : getApiClasses()) {
            if (!coreJavaClassNames.contains(apiClass.getType().qualifiedTypeName())) {
                writer.write(String.format("%sjclass %s;\n",
                                           extDecl, getComponentCClassVarName(apiClass.getType())));
            }
        }
        writer.printf("\n");

        writer.printf("/* Used non-API classes. */\n");
        for (ApiClass apiClass : getApiInfo().getUsedNonApiClasses()) {
            if (!coreJavaClassNames.contains(apiClass.getType().qualifiedTypeName())) {
                if (apiClass.getType().asClassDoc().isEnum()) {
                    printUsedButUnhandledEnumWarning(apiClass);
                }
                writer.write(String.format("%sjclass %s;\n",
                                           extDecl, getComponentCClassVarName(apiClass.getType())));
            }
        }
        writer.write("\n");
    }

    private void printUsedButUnhandledEnumWarning(ApiClass apiClass) {
        System.out.println("Warning: unhandled enum detected: enum " + apiClass);
        System.out.printf("enum %s {\n", apiClass.getType().simpleTypeName());
        final FieldDoc[] fieldDocs = apiClass.getType().asClassDoc().enumConstants();
        for (FieldDoc fieldDoc : fieldDocs) {
            System.out.printf("    %s,\n", fieldDoc.name());
        }
        System.out.printf("}\n");
    }

    protected void writeCHeaderContents(PrintWriter writer) throws IOException {

        writeTemplateResource(writer, "CModuleGenerator-stub-types.h");

        writer.write("\n");
        writer.write("/* Wrapped API classes */\n");
        for (ApiClass apiClass : getApiClasses()) {
            writer.write(String.format("typedef void* %s;\n", getComponentCClassName(apiClass.getType())));
        }
        writer.write("\n");

        writer.write("\n");
        writer.write("/* Non-API classes used in the API */\n");
        writer.write("typedef void* String;\n");
        for (ApiClass usedApiClass : getApiInfo().getUsedNonApiClasses()) {
            if (!JavadocHelpers.isString(usedApiClass.getType())) {
                writer.write(String.format("typedef void* %s;\n", getComponentCClassName(usedApiClass.getType())));
            }
        }
        writer.write("\n");

        writeTemplateResource(writer, "CModuleGenerator-stub-jvm.h");
        writeTemplateResource(writer, "CModuleGenerator-stub-conv.h");

        /////////////////////////////////////////////////////////////////////////////////////
        // Generate function declarations
        //
        for (ApiClass apiClass : getApiClasses()) {
            List<ApiConstant> constants = getApiInfo().getConstantsOf(apiClass);
            if (!constants.isEmpty()) {
                writer.write("\n");
                writer.printf("/* Constants of %s */\n", getComponentCClassName(apiClass.getType()));
                for (ApiConstant constant : constants) {
                    writer.write(String.format("extern const %s %s_%s;\n",
                                               JavadocHelpers.getCTypeName(constant.getType()),
                                               getComponentCClassName(apiClass.getType()),
                                               constant.getJavaName()));
                }
            }
            writer.write("\n");
            writer.printf("/* Functions for class %s */\n", getComponentCClassName(apiClass.getType()));
            writer.write("\n");
            final FunctionWriter functionWriter = new FunctionWriter(this, writer);
            for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                functionWriter.writeFunctionDeclaration(generator);
            }
        }
    }

    private void writeCSource() throws IOException {
        new TargetCFile(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".c") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "CModuleGenerator-stub-init.c");
                writer.printf("\n");
                writeClassDefinitions(writer, false);
                writer.printf("\n");
                writeTemplateResource(writer, "CModuleGenerator-stub-jvm.c");
                writer.printf("\n");
                writeInitApiFunction(writer);
                writer.printf("\n");
                writeTemplateResource(writer, "CModuleGenerator-stub-conv.c");
                writer.printf("\n");
                writeApiConstants(writer);
                writer.printf("\n");
                writeApiFunctions(writer);
            }
        }.create();
    }

    private void writeApiConstants(PrintWriter writer) {
        for (ApiClass apiClass : getApiClasses()) {
            List<ApiConstant> constants = getApiInfo().getConstantsOf(apiClass);
            if (!constants.isEmpty()) {
                writer.printf("/* Constants of %s */\n", getComponentCClassName(apiClass.getType()));
                for (ApiConstant constant : constants) {
                    writer.write(String.format("const %s %s_%s = %s;\n",
                                               JavadocHelpers.getCTypeName(constant.getType()),
                                               getComponentCClassName(apiClass.getType()),
                                               constant.getJavaName(),
                                               getConstantCValue(constant)));
                }
            }
        }
    }

    private void writeApiFunctions(PrintWriter writer) throws IOException {
        final FunctionWriter functionWriter = new FunctionWriter(this, writer);
        for (ApiClass apiClass : getApiClasses()) {
            for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                functionWriter.writeFunctionDefinition(generator);
                writer.println();
            }
        }
    }

    private void writeInitApiFunction(PrintWriter writer) {
        writer.write("" +
                             "int beam_initApi()\n" +
                             "{\n" +
                             "    static int exitCode = -1;\n" +
                             "    if (exitCode >= 0) {\n" +
                             "        return exitCode;\n" +
                             "    }\n" +
                             "\n" +
                             "    if (!beam_isJvmCreated() && !beam_createJvmWithDefaults()) {\n" +
                             "        exitCode = 1;\n" +
                             "        return exitCode;\n" +
                             "    }\n" +
                             "\n");

        int errCode = 1000;
        for (String javaLangClass : JAVA_LANG_CLASSES) {
            writeClassDef(writer,
                          String.format(CLASS_VAR_NAME_PATTERN, javaLangClass),
                          "java/lang/" + javaLangClass,
                          errCode++);
        }
        for (String javaUtilClass : JAVA_UTIL_CLASSES) {
            writeClassDef(writer,
                          String.format(CLASS_VAR_NAME_PATTERN, javaUtilClass),
                          "java/util/" + javaUtilClass,
                          errCode++);
        }
        errCode = 2000;
        final Set<String> coreJavaClassNames = getCoreJavaClassNames();
        for (ApiClass apiClass : getApiClasses()) {
            if (!coreJavaClassNames.contains(apiClass.getJavaName())) {
                writeClassDef(writer,
                              getComponentCClassVarName(apiClass.getType()),
                              apiClass.getResourceName(),
                              errCode++);
            }
        }
        writer.write("" +
                             "    exitCode = 0;\n" +
                             "    return exitCode;\n" +
                             "}\n" +
                             "\n");
    }

    private String getConstantCValue(ApiConstant constant) {
        String expr = constant.getFieldDoc().constantValueExpression();
        return expr != null ? expr : "NULL";
    }

    private void writeClassDef(PrintWriter writer, String classVarName, String classResourceName, int errCode) {
        writer.write(TemplateEval.eval(""
                                               + "    ${cv} = beam_findJvmClass(\"${cr}\");\n"
                                               + "    if (${cv} == NULL) { \n"
                                               + "        fprintf(stderr, \"${libName}: Java class not found: ${cr}\\n\");\n"
                                               + "        exitCode = ${err};\n"
                                               + "        return exitCode;\n"
                                               + "    }\n",
                                       new TemplateEval.KV("libName", BEAM_CAPI_NAME),
                                       new TemplateEval.KV("cv", classVarName),
                                       new TemplateEval.KV("cr", classResourceName),
                                       new TemplateEval.KV("err", errCode)));
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
                    boolean classChange = !enclosingClass.equals(lastEnclosingClass);
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

}
