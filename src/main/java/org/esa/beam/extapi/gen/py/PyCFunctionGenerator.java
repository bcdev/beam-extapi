package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import java.util.HashMap;

import static org.esa.beam.extapi.gen.JavadocHelpers.getComponentCTypeName;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.py.PyCModuleGenerator.RESULT_VAR_NAME;
import static org.esa.beam.extapi.gen.py.PyCModuleGenerator.THIS_VAR_NAME;

/**
 * @author Norman Fomferra
 */
public abstract class PyCFunctionGenerator implements FunctionGenerator {
    // TODO - put into ApiGeneratorDoclet-config.xml, whether a method shall return a CArray or a Python List object. (nf)
    private final static boolean USE_CARRAY = true;
    private final static HashMap<String, String> CARRAY_TYPE_CODES = new HashMap<String, String>();

    static {
        CARRAY_TYPE_CODES.put(Byte.TYPE.getName(), "b");
        CARRAY_TYPE_CODES.put(Boolean.TYPE.getName(), "b");
        CARRAY_TYPE_CODES.put(Character.TYPE.getName(), "h");
        CARRAY_TYPE_CODES.put(Short.TYPE.getName(), "h");
        CARRAY_TYPE_CODES.put(Integer.TYPE.getName(), "i");
        CARRAY_TYPE_CODES.put(Long.TYPE.getName(), "l");
        CARRAY_TYPE_CODES.put(Float.TYPE.getName(), "f");
        CARRAY_TYPE_CODES.put(Double.TYPE.getName(), "d");
    }

    protected final ApiMethod apiMethod;
    protected final PyCParameterGenerator[] parameterGenerators;
    protected final TemplateEval templateEval;

    protected PyCFunctionGenerator(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
        this.apiMethod = apiMethod;
        this.parameterGenerators = parameterGenerators;
        templateEval = TemplateEval.create(kv("res", RESULT_VAR_NAME),
                                           kv("this", THIS_VAR_NAME));
    }

    @Override
    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    public ApiClass getEnclosingClass() {
        return getApiMethod().getEnclosingClass();
    }

    public ExecutableMemberDoc getMemberDoc() {
        return getApiMethod().getMemberDoc();
    }

    public Type getReturnType() {
        return getApiMethod().getReturnType();
    }

    @Override
    public String getFunctionName(GeneratorContext context) {
        return context.getFunctionNameFor(getApiMethod());
    }

    @Override
    public PyCParameterGenerator[] getParameterGenerators() {
        return parameterGenerators;
    }

    @Override
    public String generateFunctionSignature(GeneratorContext context) {
        return String.format("PyObject* %s(PyObject* self, PyObject* args)",
                             context.getFunctionNameFor(getApiMethod()));
    }

    @Override
    public final String generateLocalVarDecl(GeneratorContext context) {
        StringBuilder sb = new StringBuilder();
        if (isInstanceMethod()) {
            sb.append(generateObjectTypeDecl(PyCModuleGenerator.THIS_VAR_NAME));
        }
        String localVarDecl = generateLocalVarDecl0(context);
        if (localVarDecl != null) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(localVarDecl);
        }
        return sb.toString();
    }

    protected abstract String generateLocalVarDecl0(GeneratorContext context);

    String format(String pattern, TemplateEval.KV... pairs) {
        return templateEval.add(pairs).eval(pattern);
    }

    static String generateObjectTypeDecl(String varName) {
        return eval("const char* ${var}Type;\n" +
                            "unsigned PY_LONG_LONG ${var};",
                    kv("var", varName));
    }

    @Override
    public String generateParamListDecl(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateInitCode(GeneratorContext context) {
        StringBuilder formatString = new StringBuilder();
        StringBuilder argumentsStrings = new StringBuilder();
        if (isInstanceMethod()) {
            formatString.append("(sK)");
            argumentsStrings.append(format("&${this}Type, &${this}"));
        }
        for (PyCParameterGenerator pyCParameterGenerator : getParameterGenerators()) {
            String format = pyCParameterGenerator.generateParseFormat(context);
            if (format != null) {
                formatString.append(format);
            }
            String args = pyCParameterGenerator.generateParseArgs(context);
            if (args != null) {
                if (argumentsStrings.length() > 0) {
                    argumentsStrings.append(", ");
                }
                argumentsStrings.append(args);
            }
        }
        if (argumentsStrings.length() > 0) {
            return String.format("if (!PyArg_ParseTuple(args, \"%s:%s\", %s)) {\n" +
                                         "    return NULL;\n" +
                                         "}",
                                 formatString,
                                 getCApiFunctionName(context),
                                 argumentsStrings);
        }
        return null;
    }

    @Override
    public String generatePreCallCode(GeneratorContext context) {
        return null;
    }

    @Override
    public String generatePostCallCode(GeneratorContext context) {
        return null;
    }

    protected ApiParameter getReturnParameter(GeneratorContext context) {
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

    protected String generateCApiCall(GeneratorContext context) {

        final String functionName = getCApiFunctionName(context);

        final StringBuilder argumentList = new StringBuilder();
        if (isInstanceMethod()) {
            argumentList.append(String.format("(%s) %s",
                                              getComponentCTypeName(getEnclosingClass().getType()),
                                              PyCModuleGenerator.THIS_VAR_NAME));
        }
        for (PyCParameterGenerator parameterGenerator : parameterGenerators) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(parameterGenerator.generateCallCode(context));
        }

        String extraArgs = generateExtraArgs();
        if (extraArgs != null) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(extraArgs);
        }

        return String.format("%s(%s)", functionName,
                             argumentList);
    }

    protected String generateExtraArgs() {
        return null;
    }

    private String getCApiFunctionName(GeneratorContext context) {
        final PyCModuleGenerator pyCModuleGenerator = (PyCModuleGenerator) context;
        final CModuleGenerator cModuleGenerator = pyCModuleGenerator.getCModuleGenerator();
        return cModuleGenerator.getFunctionNameFor(apiMethod);
    }

    private boolean isInstanceMethod() {
        return JavadocHelpers.isInstance(getMemberDoc());
    }

    @Override
    public String generateDocText(GeneratorContext context) {
        // TODO: generate Python-style documentation
        final String text = JavadocHelpers.encodeCCodeString(apiMethod.getMemberDoc().getRawCommentText());
        if (isInstanceMethod()) {
            final String thisParamText = String.format("@param this The %s object.", JavadocHelpers.getComponentCTypeName(apiMethod.getEnclosingClass().getType()));
            final int i = text.indexOf("@param");
            if (i > 0) {
                return String.format("%s\\n%s\\n%s", text.substring(0, i), thisParamText, text.substring(i));
            } else {
                return String.format("%s\\n%s", text, thisParamText);
            }
        }
        return text;
    }

    static class VoidMethod extends PyCFunctionGenerator {

        VoidMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return format("${call};",
                          kv("call", generateCApiCall(context)));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return "return Py_BuildValue(\"\");";
        }
    }

    static abstract class ReturnValueCallable extends PyCFunctionGenerator {
        ReturnValueCallable(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return format("${type} ${res};",
                          kv("type", JavadocHelpers.getCTypeName(getReturnType())));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return format("${res} = ${call};",
                          kv("call", generateCApiCall(context)));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return format("return ${res};");
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {
        ValueMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }
    }

    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {

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

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return format("void* ${res};");
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return format("${res} = ${call};",
                          kv("call", generateCApiCall(context)));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return format("if (${res} != NULL) {\n" +
                                  "    return Py_BuildValue(\"(sK)\", \"${type}\", (unsigned PY_LONG_LONG) ${res});\n" +
                                  "} else {\n" +
                                  "    return Py_BuildValue(\"\");\n" +
                                  "}",
                          kv("type", JavadocHelpers.getCTypeName(getReturnType())));
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return format("char* ${res};\n" +
                                  "PyObject* ${res}Str;");
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return format("${res} = ${call};",
                          kv("call", generateCApiCall(context)));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return format("if (${res} != NULL) {\n" +
                                  "    ${res}Str = PyUnicode_FromString(${res});\n" +
                                  "    beam_release_string(${res});\n" +
                                  "    return ${res}Str;\n" +
                                  "} else {\n" +
                                  "    return Py_BuildValue(\"\");\n" +
                                  "}\n");
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return format("${t}* ${res};\n" +
                                  "int ${res}Length;\n" +
                                  "PyObject* ${res}Seq;",
                          kv("t", getComponentCTypeName(getReturnType())));
        }

        @Override
        protected String generateExtraArgs() {
            return format("&${res}Length");
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            ApiParameter arrayReturnParameter = getReturnParameter(context);
            if (arrayReturnParameter != null) {
                // TODO: we need to gnerate a special call here, one uses the CArray's 'elems' field.
                return format("${res} = ${call};",
                              kv("call", generateCApiCall(context)));
            }  else {
                return format("${res} = ${call};",
                              kv("call", generateCApiCall(context)));
            }
        }
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            String typeName = getComponentCTypeName(getReturnType());
            if (USE_CARRAY) {
                String typeCode = CARRAY_TYPE_CODES.get(typeName);
                if (typeCode == null) {
                    throw new IllegalStateException("type " + typeName);
                }
                // this code uses the CArray buffer type
                return format("if (${res} != NULL) {\n" +
                                      "    ${res}Seq = CArray_createFromItems(\"${typeCode}\", ${res}, ${res}Length, beam_release_primitive_array);\n" +
                                      "    return ${res}Seq;\n" +
                                      "} else {\n" +
                                      "    return Py_BuildValue(\"\");\n" +
                                      "}\n",
                              kv("type", typeName),
                              kv("typeCode", typeCode));
            } else {
                // this code returns a python list
                return format("if (${res} != NULL) {\n" +
                                      "    ${res}Seq = beam_new_pyseq_from_${type}_array(${res}, ${res}Length);\n" +
                                      "    beam_release_primitive_array(${res}, ${res}Length);\n" +
                                      "    return ${res}Seq;\n" +
                                      "} else {\n" +
                                      "    return Py_BuildValue(\"\");\n" +
                                      "}\n",
                              kv("type", typeName));
            }
        }
    }

    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            String typeName = getComponentCTypeName(getReturnType());
            return format("if (${res} != NULL) {\n" +
                                  "    ${res}Seq = beam_new_pyseq_from_jobject_array(\"${type}\", ${res}, ${res}Length);\n" +
                                  "    beam_release_object_array(${res}, ${res}Length);\n" +
                                  "    return ${res}Seq;\n" +
                                  "} else {\n" +
                                  "    return Py_BuildValue(\"\");\n" +
                                  "}\n",
                          kv("type", typeName));
        }
    }

    static class StringArrayMethod extends ArrayMethod {
        StringArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return format("if (${res} != NULL) {\n" +
                                  "    ${res}Seq = beam_new_pyseq_from_string_array(${res}, ${res}Length);\n" +
                                  "    beam_release_string_array(${res}, ${res}Length);\n" +
                                  "    return ${res}Seq;\n" +
                                  "} else {\n" +
                                  "    return Py_BuildValue(\"\");\n" +
                                  "}\n");
        }
    }
}
