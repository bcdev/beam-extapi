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

import org.esa.beam.extapi.gen.*;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import static org.esa.beam.extapi.gen.JavadocHelpers.*;
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
        //writePythonSource();
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
                writer.println("#include \"../beampy_jpyutil.h\"");
                writer.println("#include \"../beampy_buffer.h\"");

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

                writeTemplateResource(writer, "/org/esa/beam/extapi/gen/c/CModuleGenerator-stub-jvm.c");
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
        TemplateEval templateEval = file.getTemplateEval();
        for (ApiClass apiClass : getApiInfo().getAllClasses()) {

            String className = getComponentCClassName(apiClass.getType());

            templateEval.add(kv("className", className),
                             kv("classDoc", convertToPythonDoc(getApiInfo(), apiClass.getType().asClassDoc(), "", true)));

            file.writeText("static PyMemberDef ${className}_Members[] = {\n");
            List<ApiConstant> constants = getApiInfo().getConstantsOf(apiClass);
            if (!constants.isEmpty()) {
                for (ApiConstant constant : constants) {
                    Object value = constant.getValue();
                    String constantValue;
                    if (value == null) {
                        constantValue = "NULL";
                    } else if (value instanceof String) {
                        constantValue = "\"" + ((String) value).replace("\n", "\\n").replace("\t", "\\t").replace("\n", "\\n").replace("\"", "\\\"") + "\"";
                    } else {
                        constantValue = value.toString();
                    }
                    // todo - find out how to declare constant members, e.g. how do we assign 'constantValue'?
                    file.writeText(eval("//     {\"${memberName}\", 0, 0, READONLY, ${doc}}\n",
                                        kv("memberName", constant.getJavaName()),
                                        kv("constantValue", constantValue),
                                        kv("doc", "NULL")));
                }
            }

            file.writeText("    {NULL, 0, 0, 0, NULL} /*Sentinel*/\n");
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
            file.writeText("    {NULL, NULL, 0, NULL} /*Sentinel*/\n");
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
                               kv("className", getComponentCClassName(apiClass.getType()))));
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
        writer.printf("    {NULL, NULL, 0, NULL} /*Sentinel*/\n");
        writer.printf("};\n");
    }
}
