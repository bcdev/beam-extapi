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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Norman Fomferra
 */
public class CModuleGenerator extends ModuleGenerator {

    public static final String BEAM_CAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_CAPI_NAME = "beam_capi";

    public CModuleGenerator(ApiInfo apiInfo) {
        super(apiInfo, new CFunctionGeneratorFactory(apiInfo));
        getTemplateEval().add("libName", BEAM_CAPI_NAME);
        getTemplateEval().add("libNameUC", BEAM_CAPI_NAME.toUpperCase().replace("-", "_"));
    }

    @Override
    public String getUniqueFunctionNameFor(ApiMethod apiMethod) {
        return getApiInfo().getUniqueFunctionNameFor(apiMethod);
    }

    @Override
    public void run() throws IOException {
        writeWinDef();
        writeCHeader();
        writeCSource();
        printStats();
    }

    private void writeWinDef() throws IOException {
        new TargetFile(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".def") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "CModuleGenerator-stub-init.def");
                for (ApiClass apiClass : getApiClasses()) {
                    for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                        writer.printf("\t%s\n", getUniqueFunctionNameFor(generator.getApiMethod()));
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

    private void writeCHeaderContents(PrintWriter writer) throws IOException {
        writeTemplateResource(writer, "CModuleGenerator-stub-types.h");
        writer.write("\n");
        writeClassTypedefs(writer);
        writer.write("\n");
        writeTemplateResource(writer, "CModuleGenerator-stub-jvm.h");
        writer.write("\n");
        writeTemplateResource(writer, "CModuleGenerator-stub-conv.h");
        writer.write("\n");
        writeClassConstantAndFunctionDefinitions(writer);
    }

    private void writeClassConstantAndFunctionDefinitions(PrintWriter writer) {
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

    public void writeClassTypedefs(PrintWriter writer) {
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
    }

    private void writeCSource() throws IOException {
        new TargetCFile(BEAM_CAPI_SRCDIR, BEAM_CAPI_NAME + ".c") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "CModuleGenerator-stub-init.c");
                writer.printf("\n");

                writeClassDefinitions(writer);
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
                writer.printf("\n");
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

    private String getConstantCValue(ApiConstant constant) {
        String expr = constant.getFieldDoc().constantValueExpression();
        return expr != null ? expr : "NULL";
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

}
