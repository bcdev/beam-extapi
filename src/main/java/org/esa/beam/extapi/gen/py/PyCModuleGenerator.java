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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import static org.esa.beam.extapi.gen.JavadocHelpers.convertToPythonDoc;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGenerator extends ModuleGenerator {

    public static final String BEAM_PYAPI_C_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_PY_SRCDIR = ".";
    public static final String BEAM_PYAPI_NAME = "beampy";
    public static final String BEAM_PYAPI_VARNAMEPREFIX = "BeamPy";
    public static final String SELF_OBJ_NAME = "_jobj";

    final static HashMap<String, String> CARRAY_FORMATS = new HashMap<String, String>();

    static {
        // todo - make sure that the following type mappings match in number of bytes
        CARRAY_FORMATS.put(Byte.TYPE.getName(), "b"); // 1 byte
        CARRAY_FORMATS.put(Boolean.TYPE.getName(), "b");  // 1 byte
        CARRAY_FORMATS.put(Character.TYPE.getName(), "h");  // 2 byte
        CARRAY_FORMATS.put(Short.TYPE.getName(), "h"); // 2 byte
        CARRAY_FORMATS.put(Integer.TYPE.getName(), "i"); // 4 byte
        CARRAY_FORMATS.put(Long.TYPE.getName(), "l"); // 8 byte
        CARRAY_FORMATS.put(Float.TYPE.getName(), "f"); // 4 byte
        CARRAY_FORMATS.put(Double.TYPE.getName(), "d"); // 8 byte
    }

    public static String getCArrayFormat(String typeName) {
        String format = CARRAY_FORMATS.get(typeName);
        if (format == null) {
            throw new IllegalArgumentException("Illegal type: " + typeName);
        }
        return format;
    }

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

    private void writeWinDef() throws IOException {
        new TargetFile(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".def") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "PyCModuleGenerator-stub-init.def");
            }
        }.create();
    }

    private void writeCHeader() throws IOException {
        new TargetCHeaderFile(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".h") {
            @Override
            protected void writeContent() throws IOException {
                writer.println("#include \"../beampy_carray.h\"");
                writer.println("#include \"../beampy_jobject.h\"");
                writeTemplateResource(writer, "/org/esa/beam/extapi/gen/c/CModuleGenerator-stub-types.h");

                writeJObjectSubtypesDeclarations(writer);

            }
        }.create();
    }

    private void writeCSource() throws IOException {
        new TargetCFile(BEAM_PYAPI_C_SRCDIR, BEAM_PYAPI_NAME + ".c") {
            @Override
            protected void writeContent() throws IOException {
                writeTemplateResource(writer, "PyCModuleGenerator-stub-init.c");
                writer.printf("\n");

                cModuleGenerator.writeClassTypedefs(writer);
                writer.printf("\n");

                writeTemplateResource(writer, "/org/esa/beam/extapi/gen/c/CModuleGenerator-stub-jvm.h");
                writer.printf("\n");

                writeFunctionDeclarations(writer);
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-java-util.h");
                writer.printf("\n");

                writePyMethodDefs(writer);
                writer.printf("\n");

                writeClassDefinitions(writer);
                writer.printf("\n");

                writeJObjectSubtypesRegistration(writer);
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-buffer.c");
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-pymodule.c");
                writer.printf("\n");

                writeTemplateResource(writer, "/org/esa/beam/extapi/gen/c/CModuleGenerator-stub-jvm.c");
                writer.printf("\n");

                writeInitApiFunction(writer);
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-init-method.c");
                writer.printf("\n");

                writePrimitiveArrayConverters(writer);
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-conv.c");
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-java-util.c");
                writer.printf("\n");

                writeFunctionDefinitions(writer);
                writer.printf("\n");

                writeJObjectSubtypesDefinitions(this);
                writer.printf("\n");

            }
        }.create();
    }

    private void writeJObjectSubtypesDeclarations(PrintWriter writer) {
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {
            writer.printf(eval("// PyAPI_DATA(PyTypeObject) ${className}_Type;\n" +
                                       "extern PyTypeObject ${className}_Type;\n",
                               kv("className", getClassName(apiClass.getType()))));
        }
    }

    private void writeJObjectSubtypesDefinitions(TargetFile file) {
        TemplateEval templateEval = file.getTemplateEval();
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {

            String className = getClassName(apiClass.getType());

            templateEval.add(kv("className", className),
                             kv("classDoc", convertToPythonDoc(getApiInfo(), apiClass.getType().asClassDoc(), "", true)));

            file.writeText("static PyMemberDef ${className}_Members[] = {\n");
            // todo - write members (constants)
            file.writeText("    {NULL, 0, 0, 0, NULL}\n");
            file.writeText("};\n");
            file.writeText("\n");

            file.writeText("static PyMethodDef ${className}_Methods[] = \n" +
                                   "{\n");
            List<ApiMethod> methods = getApiInfo().getMethodsOf(apiClass);
            for (ApiMethod method : methods) {

                String functionName = cModuleGenerator.getFunctionNameFor(method);
                functionName = functionName.substring(className.length() + 1);

                templateEval.add(kv("methodName", functionName),
                                 kv("methodDoc", convertToPythonDoc(getApiInfo(), method.getMemberDoc(), "", true)),
                                 kv("methodFlags", method.getMemberDoc().isStatic() || method.getMemberDoc().isConstructor() ? "METH_VARARGS | METH_STATIC" : "METH_VARARGS"));

                file.writeText("    {\"${methodName}\", (PyCFunction) BeamPy${className}_${methodName}, ${methodFlags}, \"${methodDoc}\"},\n");
            }
            file.writeText("    {NULL, NULL, 0, NULL}\n");
            file.writeText("};\n");
            file.writeText("\n");

            try {
                file.writeResource("PyCModuleGenerator-stub-pytype.c");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

            file.writeText("\n");
        }
    }

    private void writeJObjectSubtypesRegistration(PrintWriter writer) {
        writer.printf("" +
                              "int beampy_registerJObjectSubtypes(PyObject* module)\n" +
                              "{\n");
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {
            writer.printf(eval("" +
                                       "    // Register ${className}:\n" +
                                       "    ${className}_Type.tp_base = &JObject_Type;\n" +
                                       "    if (PyType_Ready(&${className}_Type) < 0) {\n" +
                                       "        return 0;\n" +
                                       "    }\n" +
                                       "    Py_INCREF(&${className}_Type);\n" +
                                       "    PyModule_AddObject(module, \"${className}\", (PyObject*) &${className}_Type);\n" +
                                       "\n",
                               kv("className", getClassName(apiClass.getType()))));
        }
        writer.printf("" +
                              "    return 1;\n" +
                              "}\n");
    }

    private void writeFunctionDeclarations(PrintWriter writer) {
        for (ApiClass apiClass : getApiClasses()) {
            for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                writer.printf("%s;\n", generator.generateFunctionSignature(this));
            }
        }
    }

    private void writeFunctionDefinitions(PrintWriter writer) throws IOException {
        final FunctionWriter functionWriter = new FunctionWriter(this, writer);
        for (ApiClass apiClass : getApiClasses()) {
            for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                functionWriter.writeFunctionDefinition(generator);
                writer.printf("\n");
            }
        }
    }

    private void writePyMethodDefs(PrintWriter writer) {
        writer.printf("static PyMethodDef BeamPy_Methods[] = {\n");
        for (ApiClass apiClass : getApiClasses()) {
            for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                writer.printf("    {\"%s\", %s, METH_VARARGS, \"%s\"},\n",
                              generator.generateFunctionName(cModuleGenerator),
                              generator.generateFunctionName(this),
                              generator.generateDocText(this));
            }
        }
        writer.printf("    {\"String_newString\", BeamPyString_newString, METH_VARARGS, \"Converts a Python unicode string into a Java java.lang.String object\"},\n");
        writer.printf("    {\"Object_delete\", BeamPyObject_delete, METH_VARARGS, \"Deletes global references to Java objects held by Python objects\"},\n");
        writer.printf("    {\"Map_newHashMap\", BeamPyMap_newHashMap, METH_VARARGS, \"Converts a Python dictionary into a Java java.utils.Map object\"},\n");
        writer.printf("    {NULL, NULL, 0, NULL}  /* Sentinel */\n");
        writer.printf("};\n");
    }

    private void writePrimitiveArrayConverters(PrintWriter writer) throws IOException {
        writePrimitiveArrayConverter(writer, "boolean", "PyBool_FromLong(data[i])", "(jboolean)(PyLong_AsLong(item) != 0)");
        writePrimitiveArrayConverter(writer, "char", "PyUnicode_FromFormat(\"%c\", data[i])", "(jchar) PyLong_AsLong(item)");
        writePrimitiveArrayConverter(writer, "byte", "PyLong_FromLong(data[i])", "(jbyte) PyLong_AsLong(item)");
        writePrimitiveArrayConverter(writer, "short", "PyLong_FromLong(data[i])", "(jshort) PyLong_AsLong(item)");
        writePrimitiveArrayConverter(writer, "int", "PyLong_FromLong(data[i])", "(jint) PyLong_AsLong(item)");
        writePrimitiveArrayConverter(writer, "long", "PyLong_FromLongLong(data[i])", "(jlong) PyLong_AsLongLong(item)");
        writePrimitiveArrayConverter(writer, "float", "PyFloat_FromDouble(data[i])", "(jfloat) PyFloat_AsDouble(item)");
        writePrimitiveArrayConverter(writer, "double", "PyFloat_FromDouble(data[i])", "(jdouble) PyFloat_AsDouble(item)");
    }

    void writePrimitiveArrayConverter(PrintWriter writer, String type, String elemToItemCall, String itemToElemCall) throws IOException {
        final String bufferFormat = getCArrayFormat(type);
        writeTemplateResource(writer, "PyCModuleGenerator-stub-conv-primarr.c",
                              kv("typeLC", type),
                              kv("typeUC", JavadocHelpers.firstCharToUpperCase(type)),
                              kv("bufferFormat", bufferFormat),
                              kv("elemToItemCall", elemToItemCall),
                              kv("itemToElemCall", itemToElemCall));
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
                writer.printf("\n\n");
                for (ApiClass apiClass : getApiInfo().getAllClasses()) {
                    final String className = getClassName(apiClass.getType());

                    writer.printf("class %s(JObject):\n", className);

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

                    if (className.equals("String")) {
                        writer.write("" +
                                             "    @staticmethod\n" +
                                             "    def newString(str):\n" +
                                             "        return String(String_newString(str))\n" +
                                             "\n");

                    } else if (className.equals("Map")) {
                        writer.write("" +
                                             "    @staticmethod\n" +
                                             "    def newMap(dic=None):\n" +
                                             "        return Map(Map_newHashMap(dic))\n" +
                                             "\n" +
                                             "    def __len__(self):\n" +
                                             "        return self.size()\n" +
                                             "\n" +
                                             "    def __getitem__(self, key):\n" +
                                             "        return self.get(key)\n" +
                                             "\n" +
                                             "    def __setitem__(self, key, value):\n" +
                                             "        return self.put(key, value)\n" +
                                             "\n" +
                                             "    def __delitem__(self, key):\n" +
                                             "        return self.remove(key)\n" +
                                             "\n");
                    }

                    for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                        String staticFName = getCModuleGenerator().getFunctionNameFor(generator.getApiMethod());
                        String javaFName = generator.getApiMethod().getJavaName();
                        String instanceFName;
                        if (javaFName.equals("<init>")) {
                            instanceFName = "new" + className;
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
                                String retClassName = getClassName(generator.getApiMethod().getReturnType());
                                if (JavadocHelpers.isInstance(generator.getApiMethod().getMemberDoc())) {
                                    writePythonInstanceFuncHeader(writer, instanceFName, params, functionCommentText);
                                    writer.printf("        return %s(%s(self.%s%s))\n", retClassName, staticFName, SELF_OBJ_NAME, args.length() > 0 ? ", " + args : "");
                                } else {
                                    writePythonStaticFuncHeader(writer, instanceFName, params, functionCommentText);
                                    writer.printf("        return %s(%s(%s))\n", retClassName, staticFName, args);
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

    private static String getClassName(Type type) {
        return type.typeName().replace('.', '_');
    }

    private static boolean isObject(Type type) {
        return !type.isPrimitive() && !JavadocHelpers.isString(type);
    }

}
