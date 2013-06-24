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

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Type;

import java.io.*;
import java.util.*;

import static org.esa.beam.extapi.gen.TemplateEval.KV;

/**
 * @author Norman Fomferra
 */
public abstract class ModuleGenerator implements GeneratorContext {

    public static final String THIS_VAR_NAME = "_this";
    public static final String METHOD_VAR_NAME = "_method";
    public static final String RESULT_VAR_NAME = "_result";

    public static final String[] JAVA_LANG_CLASSES = new String[]{"Boolean", "Byte", "Character", "Short", "Integer", "Long", "Float", "Double", "String"};
    public static final String[] JAVA_UTIL_CLASSES = new String[]{"HashMap", "HashSet", "ArrayList"};

    private final ApiInfo apiInfo;
    private final Map<ApiClass, List<FunctionGenerator>> functionGenerators;
    private final TemplateEval templateEval;

    protected ModuleGenerator(ApiInfo apiInfo, FunctionGeneratorFactory factory) {
        this.apiInfo = apiInfo;
        functionGenerators = createFunctionGenerators(apiInfo, factory);
        templateEval = TemplateEval.create();
    }

    public static String getComponentCClassName(Type type) {
        return type.typeName().replace('.', '_');
    }

    public abstract String getComponentCClassVarName(Type type);

    public Set<ApiClass> getApiClasses() {
        return apiInfo.getApiClasses();
    }

    public TemplateEval getTemplateEval() {
        return templateEval;
    }

    public String format(String pattern, KV... pairs) {
        return templateEval.add(pairs).eval(pattern);
    }

    public List<FunctionGenerator> getFunctionGenerators(ApiClass apiClass) {
        List<FunctionGenerator> generatorList = functionGenerators.get(apiClass);
        return generatorList != null ? generatorList : new ArrayList<FunctionGenerator>(0);
    }

    @Override
    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    @Override
    public ApiParameter[] getParametersFor(ApiMethod apiMethod) {
        return apiInfo.getParametersFor(apiMethod);
    }

    public abstract void run() throws IOException;

    private static Map<ApiClass, List<FunctionGenerator>> createFunctionGenerators(ApiInfo apiInfo, FunctionGeneratorFactory factory) {
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


    protected void writeTemplateResource(Writer writer, String resourceName, KV... pairs) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceName)));
        boolean isC = resourceName.endsWith(".h") || resourceName.endsWith(".c");
        try {
            if (isC) writer.write("// <<<<<<<< Begin include from " + resourceName + "\n");
            templateEval.add(pairs).eval(bufferedReader, writer);
            if (isC) writer.write("// >>>>>>>> End include from " + resourceName + "\n");
        } finally {
            bufferedReader.close();
        }
    }

    protected Set<String> getCoreJavaClassNames() {
        Set<String> coreJavaClassNames = new HashSet<String>();
        for (String simpleClassName : JAVA_LANG_CLASSES) {
            coreJavaClassNames.add("java.lang." + simpleClassName);
        }
        for (String simpleClassName : JAVA_UTIL_CLASSES) {
            coreJavaClassNames.add("java.util." + simpleClassName);
        }
        return coreJavaClassNames;
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

}
