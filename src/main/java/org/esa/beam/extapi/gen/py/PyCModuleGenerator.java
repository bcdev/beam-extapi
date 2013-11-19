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
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.FunctionGenerator;
import org.esa.beam.extapi.gen.FunctionWriter;
import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.TargetCFile;
import org.esa.beam.extapi.gen.TargetCHeaderFile;
import org.esa.beam.extapi.gen.TargetFile;
import org.esa.beam.extapi.gen.TemplateEval;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.esa.beam.extapi.gen.JavadocHelpers.convertToPythonDoc;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGenerator extends ModuleGenerator {
    public final static boolean DEBUG = true;

    public static final String BEAM_PYAPI_C_SRCDIR = "src/main/c/gen";
    public static final String BEAM_PYAPI_NAME = "beampy";
    public static final String BEAM_PYAPI_VARNAMEPREFIX = "BeamPy";
    public static final String CLASS_VAR_NAME_PATTERN = "BPy_%s_Class";

    final static HashMap<String, String> CARRAY_FORMATS = new HashMap<String, String>();

    static {
        CARRAY_FORMATS.put(Boolean.TYPE.getName(), "b");  // 1 byte
        CARRAY_FORMATS.put(Character.TYPE.getName(), "h");  // 2 byte
        CARRAY_FORMATS.put(Byte.TYPE.getName(), "b"); // 1 byte
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

    public PyCModuleGenerator(ApiInfo apiInfo) {
        super(apiInfo, new PyCFunctionGeneratorFactory(apiInfo));
        getTemplateEval().add("libName", BEAM_PYAPI_NAME);
        getTemplateEval().add("libNameUC", BEAM_PYAPI_NAME.toUpperCase());
    }

    @Override
    public String getUniqueFunctionNameFor(ApiMethod apiMethod) {
        return BEAM_PYAPI_VARNAMEPREFIX + getApiInfo().getUniqueFunctionNameFor(apiMethod);
    }

    @Override
    public String getComponentCClassVarName(Type type) {
        return String.format(CLASS_VAR_NAME_PATTERN, getComponentCClassName(type));
    }

    @Override
    public void run() throws IOException {
        writeWinDef();
        writeCHeader();
        writeCSource();
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
                writer.printf("#include <Python.h>\n");
                writer.printf("#include <structmember.h>\n");
                writer.printf("\n");

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

                writeFunctionDeclarations(writer);
                writer.printf("\n");

                writePyMethodDefs(writer);
                writer.printf("\n");

                writeClassDefinitions(writer);
                writer.printf("\n");

                writeJObjectSubtypesDefinitions(this);
                writer.printf("\n");

                writeJObjectSubtypesRegistration(writer);
                writer.printf("\n");

                writeTemplateResource(writer, "PyCModuleGenerator-stub-pymodule.c");
                writer.printf("\n");

                writeInitApiFunction(writer);
                writer.printf("\n");

                writeFunctionDefinitions(writer);
                writer.printf("\n");

            }
        }.create();
    }

    private void writeJObjectSubtypesDeclarations(PrintWriter writer) {
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {
            writer.printf(eval("extern PyTypeObject ${className}_Type;\n",
                               kv("className", getComponentCClassName(apiClass.getType()))));
        }
    }

    private void writeJObjectSubtypesDefinitions(TargetFile file) {
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {

            String className = getComponentCClassName(apiClass.getType());

            file.getTemplateEval().add(kv("className", className),
                                       kv("classDoc", convertToPythonDoc(getApiInfo(), apiClass.getType().asClassDoc(), "", true)));

            writeClassMethodDefs(file, apiClass);

            try {
                file.writeResource("PyCModuleGenerator-stub-pytype.c");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

            file.writeText("\n");
        }
    }

    private void writeClassMethodDefs(TargetFile file, ApiClass apiClass) {

        file.writeText("static PyMethodDef ${className}_methods[] = {\n");
        List<ApiMethod> methods = getApiInfo().getMethodsOf(apiClass);
        for (ApiMethod method : methods) {
            // Note: we can't simply take method.getJavaName() because this name may be overloaded in Java.
            // cModuleGenerator.getFunctionNameFor(method) will return a unique name required by C and Python
            String cFunctionName = getApiInfo().getUniqueFunctionNameFor(method);
            // Strip class name from C function name --> Python method name.
            String pyFunctionName = cFunctionName.substring(file.getTemplateEval().eval("${className}").length() + 1);

            file.getTemplateEval().add(kv("methodName", pyFunctionName),
                                       kv("methodDoc", convertToPythonDoc(getApiInfo(), method.getMemberDoc(), "", true)),
                                       kv("methodFlags", method.getMemberDoc().isStatic() || method.getMemberDoc().isConstructor() ? "METH_VARARGS | METH_STATIC" : "METH_VARARGS"));

            file.writeText("    {\"${methodName}\", (PyCFunction) BeamPy${className}_${methodName}, ${methodFlags}, \"${methodDoc}\"},\n");
        }
        file.writeText("    {NULL, NULL, 0, NULL} /*Sentinel*/\n");
        file.writeText("};\n");
        file.writeText("\n");
    }

    private String getConstantPythonValue(Object value) {
        String constantValue = null;
        if (value == null) {
            constantValue = "Py_BuildValue(\"\")";
        } else if (value instanceof Boolean) {
            constantValue = String.format("PyBool_FromLong(%d)", (Boolean) value ? 1 : 0);
        } else if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
            constantValue = String.format("PyLong_FromLong(%s)", value);
        } else if (value instanceof Long) {
            constantValue = String.format("PyLong_FromLongLong(%s)", value);
        } else if (value instanceof Float || value instanceof Double) {
            constantValue = String.format("PyFloat_FromDouble(%s)", value);
        } else if (value instanceof Character) {
            constantValue = String.format("PyUnicode_FromString(\"%s\")", toCEncodedString(value));
        } else if (value instanceof String) {
            constantValue = String.format("PyUnicode_FromString(\"%s\")", toCEncodedString(value));
        }
        return constantValue;
    }

    private String toCEncodedString(Object value) {
        return value.toString().replace("\n", "\\n").replace("\t", "\\t").replace("\n", "\\n").replace("\"", "\\\"");
    }


    private void writeJObjectSubtypesRegistration(PrintWriter writer) {
        writer.printf("" +
                              "int BPy_RegisterJObjectSubtypes(PyObject* module)\n" +
                              "{\n");
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {
            String className = getComponentCClassName(apiClass.getType());
            writer.printf(eval("" +
                                       "    // Register ${className}:\n" +
                                       "    ${className}_Type.tp_base = &JObject_Type;\n" +
                                       "    if (PyType_Ready(&${className}_Type) < 0) {\n" +
                                       "        return 0;\n" +
                                       "    }\n" +
                                       "    Py_INCREF(&${className}_Type);\n" +
                                       "    PyModule_AddObject(module, \"${className}\", (PyObject*) &${className}_Type);\n",
                               kv("className", className)));


            // Register class constants via the type's dict object.
            // There seems to be no other way in the Python's C-API to register static class variables  (Class.CONST = 8)
            // See discussions in
            //   http://stackoverflow.com/questions/2374334/static-variables-in-python-c-api
            //   http://stackoverflow.com/questions/10161609/class-property-using-python-c-api
            // Unfortunately we loose the member documentation this way.
            //
            List<ApiConstant> constants = getApiInfo().getConstantsOf(apiClass);
            if (!constants.isEmpty()) {
                writer.print(eval("    // Constants of class ${className}_Type:\n",
                                  kv("className", className)));
                for (ApiConstant constant : constants) {
                    String fieldName = constant.getJavaName();
                    Object value = constant.getValue();
                    String constantValue = getConstantPythonValue(value);
                    if (constantValue != null) {
                        // todo - Python Doc says, it is unsafe to modify tp_dict, but does not tell what to do else
                        //        Does not work: PyObject_SetAttrString((PyObject*) &${className}_Type, "${fieldName}", ${constantValue});
                        writer.print(eval("    PyDict_SetItemString(${className}_Type.tp_dict, \"${fieldName}\", ${constantValue});\n",
                                          kv("className", className),
                                          kv("fieldName", fieldName),
                                          kv("constantValue", constantValue)));

                    } else {
                        System.out.printf(eval("Warning: constant value of field ${className}.${memberName} cannot be represented in Python: ${constantValue}\n",
                                               kv("className", className),
                                               kv("memberName", fieldName),
                                               kv("constantValue", value.toString())));
                    }
                }
            }
            writer.print("\n");
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
        writer.printf("" +
                              "//\n" +
                              "// Global functions of module ${libName}\n" +
                              "//\n" +
                              "static PyMethodDef BeamPy_Functions[] = {\n");
/*
        for (ApiClass apiClass : getApiClasses()) {
            for (FunctionGenerator generator : getFunctionGenerators(apiClass)) {
                writer.printf("    {\"%s\", %s, METH_VARARGS, \"%s\"},\n",
                              generator.generateFunctionName(cModuleGenerator),
                              generator.generateFunctionName(this),
                              generator.generateDocText(this));
            }
        }
*/
        writer.printf("    {\"newRectangle\", BPy_NewRectangle, METH_VARARGS, \"Creates a Java 'Rectangle' object from x, y, width, height (all integers)\"},\n");
        writer.printf("    {\"newFile\", BPy_NewFile, METH_VARARGS, \"Creates a Java 'File' object from a given string.\"},\n");
        writer.printf("    {\"to_jobject\", BPy_to_jobject, METH_VARARGS, \"Test function which takes an argument, converts it into a Java object and returns a JObject\"},\n");
        writer.printf("    {NULL, NULL, 0, NULL} /*Sentinel*/\n");
        writer.printf("};\n");
    }

    protected void writeInitApiFunction(PrintWriter writer) {
        writer.write("" +
                             "jboolean BPy_InitApi(void)\n" +
                             "{\n" +
                             "    static int initialized = 0;\n" +
                             "\n" +
                             "    if (initialized) {\n" +
                             "        return 1;\n" +
                             "    }\n" +
                             "\n" +
                             "    if (!beam_isJvmCreated() && !beam_createJvmWithDefaults()) {\n" +
                             "        PyErr_SetString(PyExc_RuntimeError, \"BPy_InitApi: failed to create Java VM\");\n" +
                             "        return 0;\n" +
                             "    }\n" +
                             "\n");

        writeClassInitialisations(writer);

        writer.write("" +
                             "    initialized = 1;\n" +
                             "    return 1;\n" +
                             "}\n" +
                             "\n");
    }

    private void writeClassInitialisations(PrintWriter writer) {
        for (String javaLangClass : JAVA_LANG_CLASSES) {
            writeClassDef(writer,
                          String.format(CLASS_VAR_NAME_PATTERN, javaLangClass),
                          "java/lang/" + javaLangClass);
        }
        writer.println();

        for (String javaUtilClass : JAVA_UTIL_CLASSES) {
            writeClassDef(writer,
                          String.format(CLASS_VAR_NAME_PATTERN, javaUtilClass),
                          "java/util/" + javaUtilClass);
        }
        writer.println();

        final Set<String> coreJavaClassNames = getCoreJavaClassNames();
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {
            if (!coreJavaClassNames.contains(apiClass.getJavaName())) {
                writeClassDef(writer,
                              getComponentCClassVarName(apiClass.getType()),
                              apiClass.getResourceName());
            }
        }
        writer.println();
    }

    private void writeClassDef(PrintWriter writer, String classVarName, String classResourceName) {
        writer.write(format("    if (!BPy_InitJClass(&${classVar}, \"${classRes}\")) return 0;\n",
                            new TemplateEval.KV("classVar", classVarName),
                            new TemplateEval.KV("classRes", classResourceName)));
    }

    protected void writeClassDefinitions(PrintWriter writer) {

        writer.printf("/* java.lang classes */\n");
        for (String simpleClassName : JAVA_LANG_CLASSES) {
            writer.write(String.format("jclass %s;\n",
                                       String.format(CLASS_VAR_NAME_PATTERN, simpleClassName)));
        }
        writer.printf("\n");

        writer.printf("/* java.util classes */\n");
        for (String simpleClassName : JAVA_UTIL_CLASSES) {
            writer.write(String.format("jclass %s;\n",
                                       String.format(CLASS_VAR_NAME_PATTERN, simpleClassName)));
        }
        writer.printf("\n");

        writer.printf("/* BEAM API classes */\n");
        final Set<String> coreJavaClassNames = getCoreJavaClassNames();
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {
            if (!coreJavaClassNames.contains(apiClass.getType().qualifiedTypeName())) {
                writer.write(String.format("jclass %s;\n",
                                           getComponentCClassVarName(apiClass.getType())));
            }
        }
        writer.printf("\n");
    }
}
