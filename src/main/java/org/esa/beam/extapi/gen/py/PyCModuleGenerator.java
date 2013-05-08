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
import java.util.Date;
import java.util.List;

import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGenerator extends ModuleGenerator {

    // TODO: move the following constants into ApiGeneratorDoclet-config.xml (nf, 29.04.2013)
    public static final String BEAM_PYAPI_C_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_PY_SRCDIR = ".";
    public static final String BEAM_PYAPI_NAME = "beampy";
    public static final String BEAM_PYAPI_VARNAMEPREFIX = "BeamPy";
    public static final String THIS_VAR_NAME = "thisObj";
    public static final String RESULT_VAR_NAME = "result";
    public static final String SELF_OBJ_NAME = "_obj";

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
        final PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_PY_SRCDIR, BEAM_PYAPI_NAME + ".py")));
        try {
            writer.printf("# Please note: This file is machine generated. DO NOT EDIT!\n");
            writer.printf("# It will be regenerated every time you run 'java %s <beam-src-dir>'.\n", ApiGeneratorDoclet.class.getName());
            writer.printf("# Last updated on %s.\n", new Date());
            writer.printf("\n");
            writer.printf("from _%s import *\n", BEAM_PYAPI_NAME);
            writer.printf("\n");
            for (ApiClass apiClass : getApiInfo().getAllClasses()) {
                final String commentText = JavadocHelpers.convertToPythonDoc(getApiInfo(), apiClass.getType().asClassDoc(), "", false);
                if (!commentText.isEmpty()) {
                    writer.printf("\"\"\" %s\n\"\"\"\n", commentText);
                }
                writer.printf("class %s:\n", getClassName(apiClass.getType()));

                List<ApiConstant> constants = getApiInfo().getConstantsOf(apiClass);
                if (!constants.isEmpty()) {
                    writer.printf("\n");
                    for (ApiConstant constant : constants) {
                        Object value = constant.getValue();
                        if (value == null) {
                            writer.printf("    %s = None\n", constant.getJavaName());
                        } else if (value instanceof String) {
                            writer.printf("    %s = '%s'\n", constant.getJavaName(), ((String) value).replace("\n", "\\n").replace("\t", "\\t").replace("'", "''"));
                        } else {
                            writer.printf("    %s = %s\n", constant.getJavaName(), value);
                        }
                    }
                    writer.printf("\n");
                }

                writer.printf("    def __init__(self, obj):\n");
                writer.printf("        if obj == None:\n");
                writer.printf("            raise TypeError('A tuple (<type_name>, <pointer>) is required, but got None')\n");
                writer.printf("        self.%s = obj\n", SELF_OBJ_NAME);
                writer.printf("\n");
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    String staticFName = getCModuleGenerator().getFunctionNameFor(generator.getApiMethod());
                    String javaFName = generator.getApiMethod().getJavaName();
                    String instanceFName;
                    if (javaFName.equals("<init>")) {
                        instanceFName = "new" + getClassName(apiClass.getType());
                    } else {
                        instanceFName = staticFName.substring(staticFName.indexOf('_') + 1);
                    }
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
                            args.append(".");
                            args.append(SELF_OBJ_NAME);
                        } else {
                            args.append(parameterGenerator.getName());
                        }
                    }

                    final String functionCommentText = JavadocHelpers.convertToPythonDoc(getApiInfo(),
                                                                                         generator.getApiMethod().getMemberDoc(),
                                                                                         "           ",
                                                                                         false);

                    if (JavadocHelpers.isVoid(generator.getApiMethod().getReturnType())) {
                        if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                            writePythonInstanceFuncHeader(writer, instanceFName, params, functionCommentText);
                            writer.printf("        %s(self.%s%s)\n", staticFName, SELF_OBJ_NAME, args.length() > 0 ? ", " + args : "");
                            writer.printf("        return\n");
                        } else {
                            writePythonStaticFuncHeader(writer, instanceFName, params, functionCommentText);
                            writer.printf("        %s(%s)\n", staticFName, args);
                            writer.printf("        return\n");
                        }
                    } else {
                        if (isObject(generator.getApiMethod().getReturnType())) {
                            String className = getClassName(generator.getApiMethod().getReturnType());
                            if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                                writePythonInstanceFuncHeader(writer, instanceFName, params, functionCommentText);
                                writer.printf("        return %s(%s(self.%s%s))\n", className, staticFName, SELF_OBJ_NAME, args.length() > 0 ? ", " + args : "");
                            } else {
                                writePythonStaticFuncHeader(writer, instanceFName, params, functionCommentText);
                                writer.printf("        return %s(%s(%s))\n", className, staticFName, args);
                            }
                        } else {
                            if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                                writePythonInstanceFuncHeader(writer, instanceFName, params, functionCommentText);
                                writer.printf("        return %s(self.%s%s)\n", staticFName, SELF_OBJ_NAME, args.length() > 0 ? ", " + args : "");
                            } else {
                                writePythonStaticFuncHeader(writer, instanceFName, params, functionCommentText);
                                writer.printf("        return %s(%s)\n", staticFName, args);
                            }
                        }
                    }
                    writer.printf("\n");
                }
                writer.printf("\n");
            }

            writer.write("class String:\n" +
                                 "    def __init__(self, obj):\n" +
                                 "        if obj == None:\n" +
                                 "            raise TypeError('A tuple (<type_name>, <pointer>) is required, but got None')\n" +
                                 "        self._obj = obj\n" +
                                 "    \n" +
                                 "    @staticmethod\n" +
                                 "    def newString(str):\n" +
                                 "        return String(String_newString(str))\n" +
                                 "\n");

            writer.write("class Map:\n" +
                                 "    def __init__(self, obj):\n" +
                                 "        if obj == None:\n" +
                                 "            raise TypeError('A tuple (<type_name>, <pointer>) is required, but got None')\n" +
                                 "        self._obj = obj\n" +
                                 "    \n" +
                                 "    @staticmethod\n" +
                                 "    def newHashMap(dict):\n" +
                                 "        return Map(Map_newHashMap(dict))\n" +
                                 "\n");

        } finally {
            writer.close();
        }
    }

    private void writePythonStaticFuncHeader(PrintWriter writer, String funcName, StringBuilder params, String commentText) {
        writer.printf("    @staticmethod\n");
        writer.printf("    def %s(%s):\n", funcName, params);
        if (!commentText.isEmpty()) {
            writer.printf("        \"\"\"\n");
            writer.printf("%s\n", commentText);
            writer.printf("        \"\"\"\n");
        }
    }

    private void writePythonInstanceFuncHeader(PrintWriter writer, String funcName, StringBuilder params, String commentText) {
        writer.printf("    def %s(self%s):\n", funcName, params.length() > 0 ? ", " + params : "");
        if (!commentText.isEmpty()) {
            writer.printf("        \"\"\"\n");
            writer.printf("%s\n", commentText);
            writer.printf("        \"\"\"\n");
        }
    }

    private String getClassName(Type type) {
        return type.typeName().replace('.', '_');
    }

    private boolean isObject(Type type) {
        return !type.isPrimitive() && !JavadocHelpers.isString(type);
    }

    private void writeWinDef() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".def")));
        try {
            writeResource(writer, "PyCModuleGenerator-stubs.def");
        } finally {
            writer.close();
        }
    }

    private void writeCHeader() throws IOException {
        final String fileName = BEAM_PYAPI_NAME + ".h";
        final PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_C_SRCDIR, fileName)));
        try {
            CodeGenHelpers.writeCHeader(writer, fileName, new ContentWriter() {
                @Override
                public void writeContent(PrintWriter writer) throws IOException {
                    writer.println("#include \"../beampy_carray.h\"");
                }
            });
        } finally {
            writer.close();
        }
    }

    private void writeCSource() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".c")));
        try {
            CodeGenHelpers.writeCFileInfo(writer);
            writer.printf("#include \"%s\"\n", BEAM_PYAPI_NAME + ".h");
            writer.printf("#include \"%s\"\n", CModuleGenerator.BEAM_CAPI_NAME + ".h");
            writer.printf("#include \"%s\"\n", CModuleGenerator.BEAM_CAPI_NAME + "_j.h");
            writer.printf("#include \"Python.h\"\n");
            writer.printf("#include \"structmember.h\"\n");

            writer.printf("\n");
            writeResource(writer, "PyCModuleGenerator-stubs-1.c");
            writer.printf("\n");

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
            writer.printf("    {\"String_newString\", BeamPyString_newString, METH_VARARGS, \"Converts a Python unicode string into a Java java.lang.String object\"},\n");
            writer.printf("    {\"Map_newHashMap\", BeamPyMap_newHashMap, METH_VARARGS, \"Converts a Python dictionary into a Java java.utils.Map object\"},\n");
            writer.printf("    {NULL, NULL, 0, NULL}  /* Sentinel */\n");
            writer.printf("};\n");
            writer.printf("\n");

            writer.printf("\n");
            writeResource(writer, "PyCModuleGenerator-stubs-2a.c");
            writeResource(writer, "PyCModuleGenerator-stubs-2b.c");
            writer.printf("\n");

            for (ApiClass apiClass : getApiClasses()) {
                for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                    writeFunctionDefinition(generator, writer);
                    writer.println();
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
