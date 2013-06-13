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

import com.sun.javadoc.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.esa.beam.extapi.gen.TemplateEval.KV;

/**
 * @author Norman Fomferra
 */
public abstract class ModuleGenerator implements GeneratorContext {

    public static final String THIS_VAR_NAME = "_this";
    public static final String METHOD_VAR_NAME = "_method";
    public static final String RESULT_VAR_NAME = "_result";
    public static final String CLASS_VAR_NAME_PATTERN = "class%s";

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

    public static String getComponentCClassVarName(Type type) {
        return String.format(CLASS_VAR_NAME_PATTERN, getComponentCClassName(type));
    }

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
}
