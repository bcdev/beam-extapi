package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.ModuleGenerator.*;

/**
 * @author Norman Fomferra
 */
public abstract class FunctionGenerator implements CodeGenerator {

    protected final ApiMethod apiMethod;
    protected final ParameterGenerator[] parameterGenerators;

    protected FunctionGenerator(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
        this.apiMethod = apiMethod;
        this.parameterGenerators = parameterGenerators;
    }

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

    public ParameterGenerator[] getParameterGenerators() {
        return parameterGenerators;
    }

    public String generateFunctionSignature(GeneratorContext context) {
        String returnTypeName = getCTypeName(getReturnType());
        String functionName = context.getFunctionNameFor(getApiMethod());
        String parameterList = generateParameterList(context);
        if (parameterList.isEmpty()) {
            return String.format("%s %s()",
                                 returnTypeName, functionName);
        } else {
            return String.format("%s %s(%s)",
                                 returnTypeName, functionName, parameterList);
        }
    }

    @Override
    public String generateParamListDecl(GeneratorContext context) {
        return null;
    }

    @Override
    public abstract String generateLocalVarDecl(GeneratorContext context);

    @Override
    public String generatePreCallCode(GeneratorContext context) {
        return null;
    }

    @Override
    public abstract String generateCallCode(GeneratorContext context);

    @Override
    public String generatePostCallCode(GeneratorContext context) {
        return null;
    }

    public abstract String generateReturnCode(GeneratorContext context);

    protected boolean hasReturnParameter(GeneratorContext context) {
        for (ApiParameter parameter : context.getParametersFor(getApiMethod())) {
            if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                return true;
            }
        }
        return false;
    }

    protected String generateJniCall(GeneratorContext context) {

        final String functionName;
        final StringBuilder argumentList = new StringBuilder();

        if (getMemberDoc().isConstructor()) {
            functionName = "NewObject";
            argumentList.append(ModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else if (getMemberDoc().isStatic()) {
            functionName = String.format("CallStatic%sMethod", generateCallTypeName(context));
            argumentList.append(ModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else {
            functionName = String.format("Call%sMethod", generateCallTypeName(context));
            argumentList.append(SELF_VAR_NAME);
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        }

        for (ParameterGenerator parameterGenerator : parameterGenerators) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(parameterGenerator.generateCallCode(context));
        }

        return String.format("(*jenv)->%s(jenv, %s)", functionName, argumentList);
    }

    protected String generateParameterList(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
            parameterList.append(String.format("%s %s",
                                               getCTypeName(getEnclosingClass().getType()),
                                               SELF_VAR_NAME));
        }
        for (ParameterGenerator parameterGenerator : parameterGenerators) {
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

    protected abstract String generateCallTypeName(GeneratorContext context);

    static class VoidMethod extends FunctionGenerator {

        VoidMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            return "Void";
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("%s;", generateJniCall(context));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return null;
        }
    }

    static abstract class ReturnValueCallable extends FunctionGenerator {
        ReturnValueCallable(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            String targetTypeName = getCTypeName(getReturnType());
            return String.format("%s %s = (%s) 0;", targetTypeName, ModuleGenerator.RESULT_VAR_NAME, targetTypeName);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("%s = %s;", ModuleGenerator.RESULT_VAR_NAME, generateJniCall(context));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", ModuleGenerator.RESULT_VAR_NAME);
        }
    }


    static class Constructor extends ReturnValueCallable {

        Constructor(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 ModuleGenerator.RESULT_VAR_NAME, ModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {

        ValueMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            String targetTypeName = getCTypeName(getReturnType());
            return String.format("%s %s = (%s) 0;", targetTypeName, ModuleGenerator.RESULT_VAR_NAME, targetTypeName);
        }
    }


    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            String typeName = getReturnType().typeName();
            return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        }

    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 ModuleGenerator.RESULT_VAR_NAME, ModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
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
                                 generateJniCall(context), ModuleGenerator.RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", ModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
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
                           kv("r", ModuleGenerator.RESULT_VAR_NAME));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            if (hasReturnParameter(context)) {
                // NOTE: ParameterGenerator.<T>Array will generate code which sets ${r} = ...
                return eval("${r}Array = ${c};",
                            kv("r", ModuleGenerator.RESULT_VAR_NAME),
                            kv("c", generateJniCall(context)));
            }   else {
                return eval("${r}Array = ${c};\n" +
                                    "${r} = ${f}(${r}Array, resultArrayLength);",
                            kv("r", ModuleGenerator.RESULT_VAR_NAME),
                            kv("c", generateJniCall(context)),
                            kv("f", getAllocFunctionName()));
            }
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return eval("return ${r};",
                        kv("r", ModuleGenerator.RESULT_VAR_NAME));
        }

        protected abstract String getAllocFunctionName();
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getAllocFunctionName() {
            return String.format("beam_alloc_%s_array",
                                 getComponentCTypeName(getReturnType()));
        }
    }


    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getAllocFunctionName() {
            return "beam_alloc_object_array";
        }
    }

    static class StringArrayMethod extends ArrayMethod {

        StringArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getAllocFunctionName() {
            return "beam_alloc_string_array";
        }
    }
}
