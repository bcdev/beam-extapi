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

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;
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

    // todo: move to config
    public static final String BEAM_PYAPI_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_NAME = "beampy";
    public static final String BEAM_PYAPI_VARNAMEPREFIX = "BeamPy";
    public static final String THIS_VAR_NAME = "thisObj";
    public static final String RESULT_VAR_NAME = "result";

    private final CModuleGenerator cModuleGenerator;

    public PyCModuleGenerator(CModuleGenerator cModuleGenerator) {
        super(cModuleGenerator.getApiInfo(), new PyCFunctionGeneratorFactory(cModuleGenerator.getApiInfo()));
        this.cModuleGenerator = cModuleGenerator;
    }

    CModuleGenerator getCModuleGenerator() {
        return cModuleGenerator;
    }

    @Override
    public String getModuleName() {
        return BEAM_PYAPI_NAME;
    }

    @Override
    public String getFunctionNameFor(ApiMethod apiMethod) {
        return BEAM_PYAPI_VARNAMEPREFIX + cModuleGenerator.getFunctionNameFor(apiMethod);
    }

    @Override
    public void run() throws IOException {
        super.run();
        writeWinDef();
        writeCHeader();
        writeCSource();
        writePythonSource();
    }

    private void writePythonSource() throws IOException {
        final PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".py")));
        try {
            writer.printf("from _%s import *\n", BEAM_PYAPI_NAME);
            writer.printf("\n");
            for (ApiClass apiClass : getApiInfo().getAllClasses()) {
                System.out.println("apiClass = " + apiClass);
                writer.printf("class %s:\n", getClassName(apiClass.getType()));
                writer.printf("\n");
                writer.printf("    def __init__(self, obj):\n");
                writer.printf("        self.__obj = obj\n");
                writer.printf("\n");
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    String instanceFName = generator.getApiMethod().getJavaName();
                    if (instanceFName.equals("<init>")) {
                        instanceFName = "new" + getClassName(apiClass.getType());
                    }
                    String staticFName = getCModuleGenerator().getFunctionNameFor(generator.getApiMethod());
                    StringBuilder params = new StringBuilder();
                    for (ParameterGenerator parameterGenerator : generator.getParameterGenerators()) {
                        if (params.length() > 0) {
                            params.append(", ");
                        }
                        params.append(parameterGenerator.getName());
                    }
                    StringBuilder args = new StringBuilder();
                    for (ParameterGenerator parameterGenerator : generator.getParameterGenerators()) {
                        if (args.length() > 0) {
                            args.append(", ");
                        }
                        if (isObject(parameterGenerator.getType())) {
                            args.append(parameterGenerator.getName());
                            args.append(".__obj");
                        } else {
                            args.append(parameterGenerator.getName());
                        }
                    }
                    if (JavadocHelpers.isVoid(generator.getApiMethod().getReturnType())) {
                        if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                            writer.printf("    def %s(self%s):\n", instanceFName, params.length() > 0 ? ", " + params : "");
                            writer.printf("        self.%s(self.__obj%s)\n", staticFName, args.length() > 0 ? ", " + args : "");
                            writer.printf("        return\n");
                        } else {
                            writer.printf("    def %s(%s):\n", instanceFName, params);
                            writer.printf("        %s(%s)\n", staticFName, args);
                            writer.printf("        return\n");
                        }
                    } else {
                        if (isObject(generator.getApiMethod().getReturnType())) {
                            String className = getClassName(generator.getApiMethod().getReturnType());
                            if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                                writer.printf("    def %s(self%s):\n", instanceFName, params.length() > 0 ? ", " + params : "");
                                writer.printf("        return %s(self.%s(self.__obj%s))\n", className, staticFName, args.length() > 0 ? ", " + args : "");
                            } else {
                                writer.printf("    def %s(%s):\n", instanceFName, params);
                                writer.printf("        return %s(%s(%s))\n", className, staticFName, args);
                            }
                        } else {
                            if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                                writer.printf("    def %s(self%s):\n", instanceFName, params.length() > 0 ? ", " + params : "");
                                writer.printf("        return self.%s(self.__obj%s)\n", staticFName, args.length() > 0 ? ", " + args : "");
                            } else {
                                writer.printf("    def %s(%s):\n", instanceFName, params);
                                writer.printf("        return %s(%s)\n", staticFName, args);
                            }
                        }
                    }
                    writer.printf("\n");
                }
                writer.printf("\n");
            }
        } finally {
            writer.close();
        }
    }

    private String getClassName(Type type) {
        return type.typeName().replace('.', '_');
    }

    private boolean isObject(Type type) {
        return !type.isPrimitive() && !JavadocHelpers.isString(type);
    }

    private void writeWinDef() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".def")));
        try {
            writeResource(writer, "PyCModuleGenerator-stubs.def");
        } finally {
            writer.close();
        }
    }

    private void writeCHeader() throws IOException {
        final FileWriter fileWriter = new FileWriter(new File(BEAM_PYAPI_SRCDIR, BEAM_PYAPI_NAME + ".h"));
        try {
            writeCHeader(fileWriter);
        } finally {
            fileWriter.close();
        }
    }

    @Override
    protected void writeCHeaderContents(PrintWriter writer) throws IOException {
    }

    private void writeCSource() throws IOException {
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
                        // todo: generate Python constants
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
                    writer.printf("    {\"%s\", %s, METH_VARARGS, \"%s\"},\n",
                                  generator.getFunctionName(cModuleGenerator),
                                  generator.getFunctionName(this),
                                  generator.generateDocText(this));
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
                }
            }

            writer.printf("\n");
            writeArrayConverters(writer, "boolean", "boolean", "PyBool_FromLong(elems[i])", "(boolean)(PyLong_AsLong(item) != 0)");
            writeArrayConverters(writer, "char", "char", "PyUnicode_FromFormat(\"%c\", elems[i])", "(char) PyLong_AsLong(item)");
            writeArrayConverters(writer, "byte", "byte", "PyLong_FromLong(elems[i])", "(byte) PyLong_AsLong(item)");
            writeArrayConverters(writer, "short", "short", "PyLong_FromLong(elems[i])", "(short) PyLong_AsLong(item)");
            writeArrayConverters(writer, "int", "int", "PyLong_FromLong(elems[i])", "(int) PyLong_AsLong(item)");
            writeArrayConverters(writer, "dlong", "dlong", "PyLong_FromLongLong(elems[i])", "PyLong_AsLongLong(item)");
            writeArrayConverters(writer, "float", "float", "PyFloat_FromDouble(elems[i])", "(float) PyFloat_AsDouble(item)");
            writeArrayConverters(writer, "double", "double", "PyFloat_FromDouble(elems[i])", "PyFloat_AsDouble(item)");
            writeResource(writer, "PyCModuleGenerator-stubs-4.c");
            writer.printf("\n");

        } finally {
            writer.close();
        }
    }

    void writeArrayConverters(PrintWriter writer, String typeName, String ctype, String elemToItemCall, String itemToElemCall) throws IOException {
        writeResource(writer, "PyCModuleGenerator-stubs-3.c",
                      kv("typeName", typeName),
                      kv("ctype", ctype),
                      kv("elemToItemCall", elemToItemCall),
                      kv("itemToElemCall", itemToElemCall));
    }

    @Override
    protected void writeLocalMethodVarDecl(PrintWriter writer) throws IOException {
    }

    @Override
    protected void writeInitCode(PrintWriter writer, FunctionGenerator functionGenerator) throws IOException {
    }
}
