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

package org.esa.beam.extapi.gen.py;

import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiConstant;
import org.esa.beam.extapi.gen.ApiGeneratorDoclet;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.TemplateEval;
import org.esa.beam.extapi.gen.c.CModuleGenerator;
import org.esa.beam.extapi.gen.c.FunctionGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public class PyModuleGenerator {

    public static final String BEAM_PYAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_NAME = "beampy";

    private final ApiInfo apiInfo;
    private final CModuleGenerator cModuleGenerator;
    private TemplateEval templateEval;

    public PyModuleGenerator(CModuleGenerator cModuleGenerator) {
        apiInfo = cModuleGenerator.getApiInfo();
        this.cModuleGenerator = cModuleGenerator;
        templateEval = TemplateEval.create(kv("libName", BEAM_PYAPI_NAME));
    }

    public Set<ApiClass> getApiClasses() {
        return apiInfo.getApiClasses();
    }

    public void run() throws Exception {
        writeWinDef();
        writeHeader();
        writeSource();
    }

    private void writeWinDef() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".def")));
        try {
            writeResource(writer, "PyModuleGenerator-stubs.def");
        } finally {
            writer.close();
        }
    }

    private void writeHeader() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".h")));
        try {
            generateFileInfo(writer);
        } finally {
            writer.close();
        }
    }

    private void writeSource() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".c")));
        try {
            generateFileInfo(writer);
            writer.printf("#include \"%s\"\n", BEAM_PYAPI_NAME + ".h");
            writer.printf("#include \"%s\"\n", CModuleGenerator.BEAM_CAPI_NAME + ".h");
            writer.printf("#include \"python.h\"\n");

            writer.printf("\n");
            writeResource(writer, "PyModuleGenerator-stubs-1.c");
            writer.printf("\n");

            writer.write("\n");
            for (ApiClass apiClass : getApiClasses()) {
                List<ApiConstant> constants = apiInfo.getConstantsOf(apiClass);
                if (!constants.isEmpty()) {
                    for (ApiConstant constant : constants) {
                        // todo
                    }
                }
            }
            writer.write("\n");

            writer.printf("\n");
            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator generator : cModuleGenerator.getFunctionGenerators(apiClass)) {
                    writer.printf("PyObject* BeamPy%s(PyObject* self, PyObject* args);\n",
                                  generator.getFunctionName(cModuleGenerator));
                }
            }
            writer.printf("\n");

            writer.printf("\n");
            writer.printf("static PyMethodDef BeamPy_Methods[] = {\n");

            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator generator : cModuleGenerator.getFunctionGenerators(apiClass)) {
                    writer.printf("    {\"%s\", BeamPy%s, METH_VARARGS, \"TODO: gen doc for function %s\"},\n",
                                  generator.getFunctionName(cModuleGenerator),
                                  generator.getFunctionName(cModuleGenerator),
                                  generator.getFunctionName(cModuleGenerator));
                }
            }
            writer.printf("    {NULL, NULL, 0, NULL}  /* Sentinel */\n");
            writer.printf("};\n");
            writer.printf("\n");

            writer.printf("\n");
            writeResource(writer, "PyModuleGenerator-stubs-2.c");
            writer.printf("\n");

            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator generator : cModuleGenerator.getFunctionGenerators(apiClass)) {
                    writer.printf("\n");
                    writer.printf("PyObject* BeamPy%s(PyObject* self, PyObject* args)\n",
                                  generator.getFunctionName(cModuleGenerator));
                    writer.printf("{\n");
                    writer.printf("    /* TODO: gen implementation code code */\n");
                    writer.printf("    fprintf(stderr, \"error: BEAM function 'BeamPy%s' not implemented yet.\\n\");\n",
                                  generator.getFunctionName(cModuleGenerator));
                    writer.printf("    Py_INCREF(Py_None);\n");
                    writer.printf("    return Py_None;\n");
                    writer.printf("}\n");
                }
            }

        } finally {
            writer.close();
        }
    }

    private void writeResource(Writer writer, String resourceName) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceName)));
        try {
            templateEval.eval(bufferedReader, writer);
        } finally {
            bufferedReader.close();
        }
    }

    private void generateFileInfo(PrintWriter writer) {
        writer.write(String.format("/*\n" +
                                           " * DO NOT EDIT THIS FILE, IT IS MACHINE-GENERATED\n" +
                                           " * File created at %s using %s\n" +
                                           " */\n", new Date(), ApiGeneratorDoclet.class.getName()));
        writer.write("\n");
    }


}
