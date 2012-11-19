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

import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGenerator extends ModuleGenerator {

    public static final String BEAM_PYAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_NAME = "beampy";
    public static final String BEAM_PYAPI_VARNAMEPREFIX = "BeamPy";

    public static final String THIS_VAR_NAME = "thisObj";
    public static final String RESULT_VAR_NAME = "result";

    private final CModuleGenerator cModuleGenerator;

    public PyCModuleGenerator(CModuleGenerator cModuleGenerator) {
        super(cModuleGenerator.getApiInfo(), new PyCFunctionGeneratorFactory(cModuleGenerator.getApiInfo()));
        this.cModuleGenerator = cModuleGenerator;
        getTemplateEval().add("libName", BEAM_PYAPI_NAME);
        getTemplateEval().add("libNameUC", BEAM_PYAPI_NAME.toUpperCase().replace("-", "_"));
    }

    CModuleGenerator getCModuleGenerator() {
        return cModuleGenerator;
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
        final FileWriter fileWriter = new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".h"));
        try {
            writeHeader(fileWriter);
        } finally {
            fileWriter.close();
        }
    }

    @Override
    protected void writeHeaderContents(PrintWriter writer) throws IOException {
    }

    @Override
    protected void writeSource() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".c")));
        try {
            writeFileInfo(writer);
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
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    writer.printf("%s;\n", generator.generateFunctionSignature(this));
                }
            }
            writer.printf("\n");

            writer.printf("\n");
            writer.printf("static PyMethodDef BeamPy_Methods[] = {\n");

            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    writer.printf("    {\"%s\", %s, METH_VARARGS, \"TODO: gen doc for function %s\"},\n",
                                  generator.getFunctionName(cModuleGenerator),
                                  generator.getFunctionName(this),
                                  generator.getFunctionName(this));
                }
            }
            writer.printf("    {NULL, NULL, 0, NULL}  /* Sentinel */\n");
            writer.printf("};\n");
            writer.printf("\n");

            writer.printf("\n");
            writeResource(writer, "PyCModuleGenerator-stubs-2.c");
            writer.printf("\n");

            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    writeFunctionDefinition(generator, writer);
//                                writer.printf("\n");
//                                writer.printf("PyObject* BeamPy%s(PyObject* self, PyObject* args)\n",
//                                              generator.getFunctionName(cModuleGenerator));
//                                writer.printf("{\n");
//                                writer.printf("    /* TODO: gen implementation code code */\n");
//                                writer.printf("    fprintf(stderr, \"error: BEAM function 'BeamPy%s' not implemented yet.\\n\");\n",
//                                              generator.getFunctionName(cModuleGenerator));
//                                writer.printf("    Py_INCREF(Py_None);\n");
//                                writer.printf("    return Py_None;\n");
//                                writer.printf("}\n");

                }
            }

            writer.printf("\n");
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "boolean"), kv("ctype", "boolean"), kv("itemFactoryCall", "PyBool_FromLong(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "char"), kv("ctype", "char"), kv("itemFactoryCall", "PyUnicode_FromFormat(\"%c\", elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "byte"), kv("ctype", "byte"), kv("itemFactoryCall", "PyLong_FromLong(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "short"), kv("ctype", "short"), kv("itemFactoryCall", "PyLong_FromLong(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "int"), kv("ctype", "int"), kv("itemFactoryCall", "PyLong_FromLong(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "long"), kv("ctype", "dlong"), kv("itemFactoryCall", "PyLong_FromLongLong(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "float"), kv("ctype", "float"), kv("itemFactoryCall", "PyFloat_FromDouble(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "double"), kv("ctype", "double"), kv("itemFactoryCall", "PyFloat_FromDouble(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "string"), kv("ctype", "char*"), kv("itemFactoryCall", "PyUnicode_FromString(elems[i])"));
            writeResource(writer, "PyCModuleGenerator-stubs-3.c", kv("typeName", "jobject"), kv("ctype", "void*"), kv("itemFactoryCall", "PyLong_FromVoidPtr(elems[i])"));
            writer.printf("\n");

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
