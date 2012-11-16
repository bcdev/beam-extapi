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
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.FunctionGenerator;
import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGenerator extends ModuleGenerator {

    public static final String BEAM_PYAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_NAME = "beampy";
    public static final String BEAM_PYAPI_VARNAMEPREFIX = "BeamPy";

    private final CModuleGenerator cModuleGenerator;

    public PyCModuleGenerator(CModuleGenerator cModuleGenerator) {
        super(cModuleGenerator.getApiInfo());
        this.cModuleGenerator = cModuleGenerator;
        getTemplateEval().add("libName", BEAM_PYAPI_NAME);
    }

    @Override
    public String getFunctionNameFor(ApiMethod apiMethod) {
        return BEAM_PYAPI_VARNAMEPREFIX + cModuleGenerator.getFunctionNameFor(apiMethod);
    }

    @Override
    protected void writeWinDef() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".def")));
        try {
            writeResource(writer, "PyCModuleGenerator-stubs.def");
        } finally {
            writer.close();
        }
    }

    @Override
    protected void writeHeader() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".h")));
        try {
            generateFileInfo(writer);
        } finally {
            writer.close();
        }
    }

    @Override
    protected void writeSource() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".c")));
        try {
            generateFileInfo(writer);
            writer.printf("#include \"%s\"\n", BEAM_PYAPI_NAME + ".h");
            writer.printf("#include \"%s\"\n", CModuleGenerator.BEAM_CAPI_NAME + ".h");
            writer.printf("#include \"python.h\"\n");

            writer.printf("\n");
            writeResource(writer, "PyCModuleGenerator-stubs-1.c");
            writer.printf("\n");

            writer.write("\n");
            for (ApiClass apiClass : getApiClasses()) {
                List<ApiConstant> constants = getApiInfo().getConstantsOf(apiClass);
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
            writeResource(writer, "PyCModuleGenerator-stubs-2.c");
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

    @Override
    protected void writeLocalMethodVarDecl(PrintWriter writer) throws IOException {
    }

    @Override
    protected void writeInitCode(PrintWriter writer, FunctionGenerator functionGenerator) throws IOException {
    }
}
