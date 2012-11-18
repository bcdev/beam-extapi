package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.py.PyCModuleGenerator.RESULT_VAR_NAME;
import static org.esa.beam.extapi.gen.py.PyCModuleGenerator.THIS_VAR_NAME;

/**
 * @author Norman Fomferra
 */
public abstract class PyCFunctionGenerator implements FunctionGenerator {

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
        String lvd = generateLocalVarDecl0(context);
        if (lvd != null) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(lvd);
        }
        return sb.toString();
    }

    protected abstract String generateLocalVarDecl0(GeneratorContext context);

    String format(String pattern, TemplateEval.KV... pairs) {
        return templateEval.add(pairs).eval(pattern);
    }

    static String generateObjectTypeDecl(String varName) {
        return eval("const char* ${var}TypeId;\n" +
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
            argumentsStrings.append(format("&${this}TypeId, &${this}"));
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

    protected boolean hasReturnParameter(GeneratorContext context) {
        for (ApiParameter parameter : context.getParametersFor(getApiMethod())) {
            if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                return true;
            }
        }
        return false;
    }

    protected String generateCApiCall(GeneratorContext context) {

        final String functionName = getCApiFunctionName(context);

        final StringBuilder argumentList = new StringBuilder();
        if (isInstanceMethod()) {
            argumentList.append(String.format("(%s) %s",
                                              TypeHelpers.getComponentCTypeName(getEnclosingClass().getType()),
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
        return !getMemberDoc().isStatic() && !getMemberDoc().isConstructor();
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
            return String.format("%s;", generateCApiCall(context));
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
            return String.format("%s %s;",
                                 TypeHelpers.getCTypeName(getReturnType()),
                                 RESULT_VAR_NAME);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("%s = %s;", RESULT_VAR_NAME, generateCApiCall(context));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", RESULT_VAR_NAME);
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
                return String.format("return Py_BuildValue(\"p\", (int) %s);", RESULT_VAR_NAME);
            } else if (s.equals("char")) {
                return String.format("return Py_BuildValue(\"C\", (int) %s);", RESULT_VAR_NAME);
            } else if (s.equals("byte")) {
                return String.format("return Py_BuildValue(\"b\", %s);", RESULT_VAR_NAME);
            } else if (s.equals("short")) {
                return String.format("return Py_BuildValue(\"h\", %s);", RESULT_VAR_NAME);
            } else if (s.equals("int")) {
                return String.format("return Py_BuildValue(\"i\", %s);", RESULT_VAR_NAME);
            } else if (s.equals("long")) {
                return String.format("return Py_BuildValue(\"L\", (PY_LONG_LONG) %s);", RESULT_VAR_NAME);
            } else if (s.equals("float")) {
                return String.format("return Py_BuildValue(\"f\", %s);", RESULT_VAR_NAME);
            } else if (s.equals("double")) {
                return String.format("return Py_BuildValue(\"d\", %s);", RESULT_VAR_NAME);
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
            return eval("unsigned PY_LONG_LONG ${res};",
                        kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${res} = (unsigned PY_LONG_LONG) ${call};",
                        kv("res", RESULT_VAR_NAME),
                        kv("call", generateCApiCall(context)));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return eval("return ${res} != 0 ? Py_BuildValue(\"(sK)\", \"${type}\", ${res}) : Py_BuildValue(\"\");",
                        kv("res", RESULT_VAR_NAME),
                        kv("type", TypeHelpers.getCTypeName(getReturnType())));
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return eval("char* ${res}Str;\n" +
                                "PyObject* ${res};",
                        kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${res}Str = ${call};\n" +
                                "${res} = PyUnicode_FromString(${res}Str);\n" +
                                "free(${res}Str);",
                        kv("res", RESULT_VAR_NAME),
                        kv("call", generateCApiCall(context)));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return eval("return ${res};", kv("res", RESULT_VAR_NAME));
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateExtraArgs() {
            return String.format("&%sLength", RESULT_VAR_NAME);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            /*
            if (hasReturnParameter(context)) {
                // NOTE: ParameterGenerator.<T>Array will generate code which sets ${r} = ...
                return eval("${r}Array = ${c};",
                            kv("r", RESULT_VAR_NAME),
                            kv("c", generateCApiCall(context)));
            } else {
                return eval("${r}Array = ${c};\n" +
                                    "${r} = ${f}(${r}Array, resultArrayLength);",
                            kv("r", RESULT_VAR_NAME),
                            kv("c", generateCApiCall(context)),
                            kv("f", getAllocFunctionName()));
            }
            */

            return super.generateCallCode(context);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("PyErr_SetString(BeamPy_Error, \"Not implemented: %s\");\n" +
                                         "return NULL;", getFunctionName(context));
        }
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return String.format("int %sLength;\n" +
                                         "%s* %s;", RESULT_VAR_NAME, TypeHelpers.getComponentCTypeName(getReturnType()), RESULT_VAR_NAME);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${res} = ${call};",
                        kv("res", RESULT_VAR_NAME),
                        kv("call", generateCApiCall(context)));
        }
    }

    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return eval("int ${res}Length;\n" +
                                "unsigned PY_LONG_LONG ${res};",
                        kv("res", RESULT_VAR_NAME));
        }
    }

    static class StringArrayMethod extends ArrayMethod {
        StringArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl0(GeneratorContext context) {
            return eval("int ${res}Length;\n" +
                                "char** ${res};", kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${res} = ${call};",
                        kv("res", RESULT_VAR_NAME),
                        kv("call", generateCApiCall(context)));
        }
    }
}
