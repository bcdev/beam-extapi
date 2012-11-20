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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

import static org.esa.beam.extapi.gen.TemplateEval.KV;

/**
 * @author Norman Fomferra
 */
public abstract class ModuleGenerator implements GeneratorContext {

    private final ApiInfo apiInfo;
    private final Map<ApiClass, List<FunctionGenerator>> functionGenerators;
    private final TemplateEval templateEval;

    protected ModuleGenerator(ApiInfo apiInfo, FunctionGeneratorFactory factory) {
        this.apiInfo = apiInfo;
        functionGenerators = createFunctionGenerators(apiInfo, factory);
        templateEval = TemplateEval.create();
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public Set<ApiClass> getApiClasses() {
        return apiInfo.getApiClasses();
    }

    public TemplateEval getTemplateEval() {
        return templateEval;
    }

    public String format(String pattern, KV ... pairs) {
        return templateEval.add(pairs).eval(pattern);
    }

    public List<FunctionGenerator> getFunctionGenerators(ApiClass apiClass) {
        List<FunctionGenerator> generatorList = functionGenerators.get(apiClass);
        return generatorList != null ? generatorList : new ArrayList<FunctionGenerator>(0);
    }

    @Override
    public ApiParameter[] getParametersFor(ApiMethod apiMethod) {
        return apiInfo.getParametersFor(apiMethod);
    }

    public abstract String getModuleName();

    public void run() throws IOException {
        getTemplateEval().add("libName", getModuleName());
        getTemplateEval().add("libNameUC", getModuleName().toUpperCase().replace("-", "_"));
    }

    protected void writeCHeader(Writer writer) throws IOException {
        PrintWriter pw = new PrintWriter(writer);
        writeFileInfo(pw);
        pw.write(format("#ifndef ${libNameUC}_H\n" +
                                    "#define ${libNameUC}_H\n" +
                                    "\n" +
                                    "#ifdef __cplusplus\n" +
                                    "extern \"C\" {\n" +
                                    "#endif\n"));

        pw.write("\n");
        writeCHeaderContents(pw);
        pw.write("\n");

        pw.write(format("#ifdef __cplusplus\n" +
                                    "} /* extern \"C\" */\n" +
                                    "#endif\n" +
                                    "#endif /* !${libNameUC}_H */"));
    }

    protected abstract void writeCHeaderContents(PrintWriter writer) throws IOException;


    protected void writeFunctionDeclaration(PrintWriter writer, FunctionGenerator generator) {
        writer.printf("%s;\n", generator.generateFunctionSignature(this));
    }

    protected void writeFunctionDefinition(FunctionGenerator functionGenerator, PrintWriter writer) throws IOException {
        writer.printf("%s\n", functionGenerator.generateFunctionSignature(this));
        writer.print("{\n");
        writeLocalMethodVarDecl(writer);
        for (ParameterGenerator parameterGenerator : functionGenerator.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generateLocalVarDecl(this));
        }
        writeCode(writer, functionGenerator.generateLocalVarDecl(this));
        writeCode(writer, functionGenerator.generateInitCode(this));
        writeInitCode(writer, functionGenerator);
        for (ParameterGenerator parameterGenerator : functionGenerator.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generatePreCallCode(this));
        }
        writeCode(writer, functionGenerator.generatePreCallCode(this));
        writeCode(writer, functionGenerator.generateCallCode(this));
        writeCode(writer, functionGenerator.generatePostCallCode(this));
        for (ParameterGenerator parameterGenerator : functionGenerator.getParameterGenerators()) {
            writeCode(writer, parameterGenerator.generatePostCallCode(this));
        }
        writeCode(writer, functionGenerator.generateReturnCode(this));
        writer.print("}\n");
        writer.print("\n");
    }

    protected abstract void writeLocalMethodVarDecl(PrintWriter writer) throws IOException;

    protected abstract void writeInitCode(PrintWriter writer, FunctionGenerator functionGenerator) throws IOException;

    protected void writeCode(PrintWriter writer, String code) throws IOException {
        String[] callCode = generateLines(code);
        for (String line : callCode) {
            writer.printf("    %s\n", line);
        }
    }

    protected void writeResource(Writer writer, String resourceName, KV ... pairs) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceName)));
        try {
            templateEval.add(pairs).eval(bufferedReader, writer);
        } finally {
            bufferedReader.close();
        }
    }

    protected void writeFileInfo(PrintWriter writer) {
        writer.write(String.format("/*\n" +
                                           " * DO NOT EDIT THIS FILE, IT IS MACHINE-GENERATED\n" +
                                           " * File created at %s using %s\n" +
                                           " */\n", new Date(), ApiGeneratorDoclet.class.getName()));
        writer.write("\n");
    }

    protected String[] generateLines(String code) {
        if (code == null || code.length() == 0) {
            return new String[0];
        }
        return code.split("\n");
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
