package org.esa.beam.extapi.gen.py;

import org.esa.beam.extapi.gen.AbstractFunctionGenerator;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.JavadocHelpers;
import org.esa.beam.extapi.gen.ModuleGenerator;

import static org.esa.beam.extapi.gen.JavadocHelpers.firstCharUp;
import static org.esa.beam.extapi.gen.ModuleGenerator.METHOD_VAR_NAME;
import static org.esa.beam.extapi.gen.ModuleGenerator.THIS_VAR_NAME;
import static org.esa.beam.extapi.gen.ModuleGenerator.getComponentCClassName;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public abstract class PyCFunctionGenerator extends AbstractFunctionGenerator {

    protected PyCFunctionGenerator(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
        super(apiMethod, parameterGenerators);
        templateEval.add(kv("res", ModuleGenerator.RESULT_VAR_NAME),
                         kv("this", ModuleGenerator.THIS_VAR_NAME));
    }

    @Override
    public final PyCParameterGenerator[] getParameterGenerators() {
        return (PyCParameterGenerator[]) super.getParameterGenerators();
    }

    @Override
    public String generateDocText(GeneratorContext context) {
        return JavadocHelpers.convertToPythonDoc(context.getApiInfo(), apiMethod.getMemberDoc(), "", true);
    }

    @Override
    public final String generateFunctionName(GeneratorContext context) {
        return context.getUniqueFunctionNameFor(getApiMethod());
    }

    @Override
    public final String generateFunctionSignature(GeneratorContext context) {
        // We have a fixed function signature: PyObject* <function>(PyObject* self, PyObject* args)
        return String.format("PyObject* %s(PyObject* self, PyObject* args)",
                             context.getUniqueFunctionNameFor(getApiMethod()));
    }

    @Override
    public final String generateExtraFunctionParamDeclaration(GeneratorContext context) {
        // We have a fixed function signature: PyObject* <function>(PyObject* self, PyObject* args)
        return null;
    }


    @Override
    public final String generateLocalVarDeclarations(GeneratorContext context) {
        final String methodVarDecl = super.generateLocalVarDeclarations(context);
        if (isInstanceMethod()) {
            return methodVarDecl +
                    format("\n" +
                                   "jobject ${this}JObj = NULL;",
                           kv("this", ModuleGenerator.THIS_VAR_NAME));
        } else {
            return methodVarDecl;
        }
    }

    @Override
    public String generateEnterCode(GeneratorContext context) {
        final ApiMethod apiMethod = getApiMethod();

        String s = format("" +
                                  "if (!BPy_InitApi()) {\n" +
                                  "    return NULL;\n" +
                                  "}\n" +
                                  "if (!BPy_InitJMethod(&${method}, ${classVar}, \"${className}\", \"${methodName}\", \"${methodSig}\", ${isstatic})) {\n" +
                                  "    return NULL;\n" +
                                  "}\n",
                          kv("method", ModuleGenerator.METHOD_VAR_NAME),
                          kv("isstatic", apiMethod.getMemberDoc().isStatic() ? "1" : "0"),
                          kv("classVar", context.getComponentCClassVarName(apiMethod.getEnclosingClass().getType())),
                          kv("className", apiMethod.getEnclosingClass().getType().qualifiedTypeName()),
                          kv("methodName", apiMethod.getJavaName()),
                          kv("methodSig", apiMethod.getJavaSignature()));
        if (isInstanceMethod()) {
            return s + format("" +
                                      "${this}JObj = JObject_AsJObjectRefT(self, ${classVarName});\n" +
                                      "if (${this}JObj == NULL) {\n" +
                                      "    PyErr_SetString(PyExc_ValueError, \"argument 'self' must be of type '${typeName}' (Java object reference)\");\n" +
                                      "    return NULL;\n" +
                                      "}",
                              kv("typeName", getComponentCClassName(getEnclosingClass().getType())),
                              kv("classVarName", context.getComponentCClassVarName(getEnclosingClass().getType())));
        } else {
            return s;
        }
    }


    @Override
    public final String generateTargetArgsFromParsedParamsAssignment(GeneratorContext context) {
        String parseFormats = getParseFormats(context);

        if (parseFormats.isEmpty()) {
            return null;
        }

        String parseArgs = getParseArgs(context);
        return format("" +
                              "if (!PyArg_ParseTuple(args, \"${parseFormats}:${function}\", ${parseArgs})) {\n" +
                              "    return NULL;\n" +
                              "}\n",
                      kv("parseFormats", parseFormats),
                      kv("function", apiMethod.getJavaName()),
                      kv("parseArgs", parseArgs));
    }

    private String getParseFormats(GeneratorContext context) {
        StringBuilder parseFormats = new StringBuilder();
        for (PyCParameterGenerator generator : getParameterGenerators()) {
            String format = generator.getParseFormat(context);
            if (format != null) {
                parseFormats.append(format);
            }
        }
        return parseFormats.toString();
    }


    private String getParseArgs(GeneratorContext context) {
        StringBuilder parseArgs = new StringBuilder();
        for (PyCParameterGenerator generator : getParameterGenerators()) {
            String args = generator.getParseArgs(context);
            if (args != null) {
                if (parseArgs.length() > 0) {
                    parseArgs.append(", ");
                }
                parseArgs.append(args);
            }
        }
        return parseArgs.toString();
    }


    final ApiParameter getReturnParameter(GeneratorContext context) {
        for (ApiParameter parameter : context.getParametersFor(getApiMethod())) {
            if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                return parameter;
            }
        }
        return null;
    }

    protected boolean hasReturnParameter(GeneratorContext context) {
        return getReturnParameter(context) != null;
    }

    protected String getJniMethodCall(GeneratorContext context, String methodTypeName) {
        return getJniMethodCall(context, methodTypeName, METHOD_VAR_NAME, THIS_VAR_NAME + "JObj");
    }

    static class VoidMethod extends PyCFunctionGenerator {

        VoidMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            // void
            return null;
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            // void
            return null;
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${call};",
                          kv("call", getJniMethodCall(context, "Void")));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return "return Py_BuildValue(\"\");";
        }
    }

    static class PrimitiveMethod extends PyCFunctionGenerator {
        PrimitiveMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            // Not needed, because primitive parameter type should automatically match JNI type
            return null;
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return format("j${type} ${res} = (j${type}) 0;",
                          kv("type", getReturnType().simpleTypeName()));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            String typeName = firstCharUp(getReturnType().simpleTypeName());
            return format("${res} = ${call};",
                          kv("call", getJniMethodCall(context, typeName)));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {

            String s = getReturnType().typeName();
            if (s.equals("boolean")) {
                return format("return PyBool_FromLong(${res});");
            } else if (s.equals("char")) {
                return format("return PyUnicode_FromFormat(\"%c\", ${res});");
            } else if (s.equals("byte")) {
                return format("return PyLong_FromLong(${res});");
            } else if (s.equals("short")) {
                return format("return PyLong_FromLong(${res});");
            } else if (s.equals("int")) {
                return format("return PyLong_FromLong(${res});");
            } else if (s.equals("long")) {
                return format("return PyLong_FromLongLong(${res});");
            } else if (s.equals("float")) {
                return format("return PyFloat_FromDouble(${res});");
            } else if (s.equals("double")) {
                return format("return PyFloat_FromDouble(${res});");
            } else {
                throw new IllegalArgumentException("can't deal with type '" + s + "'");
            }
        }

    }

    static class ObjectMethod extends PyCFunctionGenerator {
        ObjectMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return format("jobject ${res}JObj = NULL;");
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            return format("PyObject* ${res}PyObj = NULL;");
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${res}JObj = ${call};",
                          kv("call", getJniMethodCall(context, "Object")));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            String typeName = getComponentCClassName(getReturnType());
            return format("${res}PyObj = BPy_FromJObject(&${typeName}_Type, ${res}JObj);",
                          kv("typeName", typeName));
        }

        @Override
        public String generateJniResultDeref(GeneratorContext context) {
            return format("(*jenv)->DeleteLocalRef(jenv, ${res}JObj);");
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return format("return ${res}PyObj;");
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res}PyObj = BPy_FromJString((jstring) ${res}JObj);");
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            if (hasReturnParameter(context)) {
                // return value ${res}PyObj is already set by return parameter
                return null;
            } else {
                String primType = firstCharUp(getReturnType().simpleTypeName());
                return format("${res}PyObj = BPy_FromJ${primType}Array((jarray) ${res}JObj);",
                              kv("primType", primType));
            }
        }
    }

    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res}PyObj = BPy_FromJObjectArray((jarray) ${res}JObj);");
        }
    }

    static class StringArrayMethod extends ArrayMethod {
        StringArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res}PyObj = BPy_FromJStringArray((jarray) ${res}JObj);");
        }
    }
}
