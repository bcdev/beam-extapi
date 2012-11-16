package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.FunctionGenerator;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.TypeHelpers;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.py.PyCModuleGenerator.RESULT_VAR_NAME;

/**
 * @author Norman Fomferra
 */
public abstract class PyCFunctionGenerator implements FunctionGenerator {

    protected final ApiMethod apiMethod;
    protected final PyCParameterGenerator[] parameterGenerators;

    protected PyCFunctionGenerator(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
        this.apiMethod = apiMethod;
        this.parameterGenerators = parameterGenerators;
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
    public String generateLocalVarDecl(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
                    parameterList.append(String.format("%s %s;\n",
                                                       TypeHelpers.getCTypeName(getEnclosingClass().getType()),
                                                       PyCModuleGenerator.OBJ_VAR_NAME));
                }
        for (PyCParameterGenerator parameterGenerator : parameterGenerators) {
            String decl = parameterGenerator.generateLocalVarDecl(context);
            if (decl != null) {
                parameterList.append(decl);
                parameterList.append("\n");
            }
        }
        return parameterList.toString();
    }

    @Override
    public String generateParamListDecl(GeneratorContext context) {
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

        final PyCModuleGenerator pyCModuleGenerator = (PyCModuleGenerator) context;
        final CModuleGenerator cModuleGenerator = pyCModuleGenerator.getCModuleGenerator();

        final String functionName = cModuleGenerator.getFunctionNameFor(apiMethod);

        final StringBuilder argumentList = new StringBuilder();
        for (PyCParameterGenerator parameterGenerator : parameterGenerators) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(parameterGenerator.generateCallCode(context));
        }

        return String.format("// %s(%s)", functionName, argumentList);
    }

    protected String generateParameterList(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
            parameterList.append(String.format("%s %s",
                                               TypeHelpers.getCTypeName(getEnclosingClass().getType()),
                                               PyCModuleGenerator.OBJ_VAR_NAME));
        }
        for (PyCParameterGenerator parameterGenerator : parameterGenerators) {
            String decl = parameterGenerator.generateParamListDecl(context);
            if (decl != null) {
                if (parameterList.length() > 0) {
                    parameterList.append(", ");
                }
                parameterList.append(decl);
            }
        }
        String decl = generateParamListDecl(context);
        if (decl != null) {
            if (parameterList.length() > 0) {
                parameterList.append(", ");
            }
            parameterList.append(decl);
        }
        return parameterList.toString();
    }

    private boolean isInstanceMethod() {
        return !getMemberDoc().isStatic() && !getMemberDoc().isConstructor();
    }

    static class VoidMethod extends PyCFunctionGenerator {

        VoidMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
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
        public String generateLocalVarDecl(GeneratorContext context) {
            final String lvd = super.generateLocalVarDecl(context);
            String targetTypeName = TypeHelpers.getCTypeName(getReturnType());
            return String.format("%s\n%s %s = (%s) 0;", lvd, targetTypeName, RESULT_VAR_NAME, targetTypeName);
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


    static class Constructor extends ReturnValueCallable {

        Constructor(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {

        ValueMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            String targetTypeName = TypeHelpers.getCTypeName(getReturnType());
            return String.format("%s %s = (%s) 0;", targetTypeName, RESULT_VAR_NAME, targetTypeName);
        }
    }


    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }
    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 RESULT_VAR_NAME, RESULT_VAR_NAME);
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return super.generateLocalVarDecl(context) + "\n" +
                    "jstring _resultString = NULL;";
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("_resultString = %s;\n" +
                                         "%s = beam_alloc_string(_resultString);",
                                 generateCApiCall(context), RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", RESULT_VAR_NAME);
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return "int* resultArrayLength";
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return super.generateLocalVarDecl(context)
                    + eval("\njarray ${r}Array = NULL;",
                           kv("r", RESULT_VAR_NAME));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
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
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return eval("return ${r};",
                        kv("r", RESULT_VAR_NAME));
        }

        protected abstract String getAllocFunctionName();
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getAllocFunctionName() {
            return String.format("beam_alloc_%s_array",
                                 TypeHelpers.getComponentCTypeName(getReturnType()));
        }
    }


    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getAllocFunctionName() {
            return "beam_alloc_object_array";
        }
    }

    static class StringArrayMethod extends ArrayMethod {

        StringArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getAllocFunctionName() {
            return "beam_alloc_string_array";
        }
    }
}
