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
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiConstant;
import org.esa.beam.extapi.gen.ApiGeneratorDoclet;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.FunctionGenerator;
import org.esa.beam.extapi.gen.FunctionWriter;
import org.esa.beam.extapi.gen.JavadocHelpers;
import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.ParameterGenerator;
import org.esa.beam.extapi.gen.TargetCFile;
import org.esa.beam.extapi.gen.TargetCHeaderFile;
import org.esa.beam.extapi.gen.TargetFile;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
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
        getTemplateEval().add("libName", BEAM_PYAPI_NAME);
        getTemplateEval().add("libNameUC", BEAM_PYAPI_NAME.toUpperCase());
    }

    CModuleGenerator getCModuleGenerator() {
        return cModuleGenerator;
    }

    @Override
    public String getFunctionNameFor(ApiMethod apiMethod) {
        return BEAM_PYAPI_VARNAMEPREFIX + cModuleGenerator.getFunctionNameFor(apiMethod);
    }

    @Override
    public void run() throws IOException {
        writeWinDef();
        writeCHeader();
        writeCSource();
        writePythonSource();
    }

    private void writePythonSource() throws IOException {
        new TargetFile(BEAM_PYAPI_PY_SRCDIR, BEAM_PYAPI_NAME + ".py") {
            @Override
            protected void writeHeader() throws IOException {
                writer.printf("# Please note: This file is machine generated. DO NOT EDIT!\n");
                writer.printf("# It will be regenerated every time you run 'java %s <beam-src-dir>'.\n", ApiGeneratorDoclet.class.getName());
                //writer.printf("# Last updated on %s.\n", new Date());
                writer.printf("\n");
            }

            @Override
            protected void writeContent() throws IOException {
                writer.printf("from _%s import *\n", BEAM_PYAPI_NAME);
                writer.printf("\n");
                writer.printf(eval(""
                                      + "class JObject:\n"
                                      + "    def __init__(self, obj):\n"
                                      + "        try:\n"
                                      + "            assert obj is not None\n"
                                      + "            assert len(obj) == 2\n"
                                      + "            assert type(obj[0]) == str\n"
                                      + "            assert type(obj[1]) == int\n"
                                      + "        except (TypeError, AssertionError):\n"
                                      + "            raise TypeError('A tuple (<java-type-name>, <java-obj-pointer>) is required, but got:', obj)\n"
                                      + "        self.${obj} = obj\n"
                                      + "\n"
                                      + "    def __del__(self):\n"
                                      + "        if self.${obj} != None:\n"
                                      + "            Object_delete(self.${obj})\n"
                                      + "\n",
                                   kv("obj", SELF_OBJ_NAME)));
                for (ApiClass apiClass : getApiInfo().getAllClasses()) {
                    writer.printf("class %s(JObject):\n", getClassName(apiClass.getType()));

                    final String commentText = JavadocHelpers.convertToPythonDoc(getApiInfo(), apiClass.getType().asClassDoc(), "", false);
                    if (!commentText.isEmpty()) {
                        writer.printf("    \"\"\" %s\n\"\"\"\n", commentText);
                    }

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

                    writer.print(eval(""
                                              + "    def __init__(self, obj):\n"
                                              + "        JObject.__init__(self, obj)\n"
                                              + "\n"
                                              + "    def __del__(self):\n"
                                              + "        JObject.__del__(self)\n"
                                              + "\n",
                                      kv("obj", SELF_OBJ_NAME)));

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

            }
        }.create();
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
        new TargetFile(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".def") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "PyCModuleGenerator-stubs.def");
            }
        }.create();
    }

    private void writeCHeader() throws IOException {
        new TargetCHeaderFile(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".h") {
            @Override
            protected void writeContent() {
                writer.println("#include \"../beampy_carray.h\"");
            }
        }.create();
    }

    private void writeCSource() throws IOException {
        new TargetCFile(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".c") {
            @Override
            protected void writeContent() throws IOException {

                writeTemplateResource(writer, "PyCModuleGenerator-stubs-1.c");
                writer.printf("\n");

                writer.write("\n");

                writer.printf("\n");
                for (ApiClass apiClass : getApiClasses()) {
                    for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                        writer.printf("%s;\n", generator.generateFunctionSignature(PyCModuleGenerator.this));
                    }
                }
                writer.printf("\n");

                writer.printf("\n");
                writer.printf("static PyMethodDef BeamPy_Methods[] = {\n");

                for (ApiClass apiClass : getApiClasses()) {
                    for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                        writer.printf("    {\"%s\", %s, METH_VARARGS, \"%s\"},\n",
                                      generator.generateFunctionName(cModuleGenerator),
                                      generator.generateFunctionName(PyCModuleGenerator.this),
                                      generator.generateDocText(PyCModuleGenerator.this));
                    }
                }
                writer.printf("    {\"Object_delete\", BeamPyObject_delete, METH_VARARGS, \"Deletes global references to Java objects held by Python objects\"},\n");
                writer.printf("    {\"String_newString\", BeamPyString_newString, METH_VARARGS, \"Converts a Python unicode string into a Java java.lang.String object\"},\n");
                // experimental
                // writer.printf("    {\"Map_newHashMap\", BeamPyMap_newHashMap, METH_VARARGS, \"Converts a Python dictionary into a Java java.utils.Map object\"},\n");
                writer.printf("    {NULL, NULL, 0, NULL}  /* Sentinel */\n");
                writer.printf("};\n");
                writer.printf("\n");

                writer.printf("\n");
                writeTemplateResource(writer, "PyCModuleGenerator-stubs-2a.c");
                writeTemplateResource(writer, "PyCModuleGenerator-stubs-2b.c");
                writer.printf("\n");

                final FunctionWriter functionWriter = new FunctionWriter(PyCModuleGenerator.this, writer);
                for (ApiClass apiClass : getApiClasses()) {
                    for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                        functionWriter.writeFunctionDefinition(generator);
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
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stubs-4.c");

                writer.printf("\n");
            }
        }.create();
    }

    void writeArrayConverters(PrintWriter writer, String typeName, String ctype, String elemToItemCall, String itemToElemCall) throws IOException {
        writeTemplateResource(writer, "PyCModuleGenerator-stubs-3.c",
                              kv("typeName", typeName),
                              kv("ctype", ctype),
                              kv("elemToItemCall", elemToItemCall),
                              kv("itemToElemCall", itemToElemCall));
    }
}
