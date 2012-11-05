package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiMethod;

import static org.esa.beam.extapi.gen.c.ModuleGenerator.METHOD_VAR_NAME;
import static org.esa.beam.extapi.gen.c.ModuleGenerator.SELF_VAR_NAME;

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

    public String getTargetEnclosingClassVarName() {
        return ModuleGenerator.getCClassVarName(getEnclosingClass().getType());
    }

    protected String getTargetEnclosingTypeName() {
        return ModuleGenerator.getCTypeName(getEnclosingClass().getType());
    }

    protected String getTargetReturnTypeName() {
        return ModuleGenerator.getCTypeName(getReturnType());
    }

    public String generateFunctionSignature(GeneratorContext context) {
        String returnTypeName = getTargetReturnTypeName();
        String functionName = context.getFunctionName(this);
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

    protected String generateJniCall(GeneratorContext context) {

        final String functionName;
        final StringBuilder argumentList = new StringBuilder();

        if (getMemberDoc().isConstructor()) {
            functionName = "NewObject";
            argumentList.append(getTargetEnclosingClassVarName());
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else if (getMemberDoc().isStatic()) {
            functionName = String.format("CallStatic%sMethod", generateCallTypeName(context));
            argumentList.append(getTargetEnclosingClassVarName());
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

        return String.format("(*jenv)->%s(jenv, %s);", functionName, argumentList);
    }

    protected String generateParameterList(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
            parameterList.append(String.format("%s %s",
                                               getTargetEnclosingTypeName(),
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
            return generateJniCall(context);
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
            String targetTypeName = getTargetReturnTypeName();
            return String.format("%s %s = (%s) 0;", targetTypeName, ModuleGenerator.RESULT_VAR_NAME, targetTypeName);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("%s = %s", ModuleGenerator.RESULT_VAR_NAME, generateJniCall(context));
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
            String targetTypeName = getTargetReturnTypeName();
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
            return String.format("_resultString = %s\n" +
                                         "%s = beam_alloc_string(_resultString);",
                                 generateJniCall(context), ModuleGenerator.RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", ModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static class PrimitiveArrayMethod extends ObjectMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return "int* resultArrayLength";
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return super.generateLocalVarDecl(context) + "\n" +
                    "jarray _resultArray = NULL;";
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("_resultArray = %s\n" +
                                         "%s = beam_alloc_%s_array(_resultArray, resultArrayLength);",
                                 generateJniCall(context),
                                 ModuleGenerator.RESULT_VAR_NAME,
                                 ModuleGenerator.getTargetComponentTypeName(getReturnType(), false));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", ModuleGenerator.RESULT_VAR_NAME);
        }
    }


    static class ObjectArrayMethod extends ObjectMethod {
        ObjectArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return "int* resultArrayLength";
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return super.generateLocalVarDecl(context) + "\n" +
                    "jobjectArray _resultArray = NULL;";
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("_resultArray = %s\n" +
                                         "%s = beam_alloc_object_array(_resultArray, resultArrayLength);",
                                 generateJniCall(context), ModuleGenerator.RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", ModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static class StringArrayMethod extends ObjectArrayMethod {

        StringArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("_resultArray = %s\n" +
                                         "%s = beam_alloc_string_array(_resultArray, resultArrayLength);",
                                 generateJniCall(context), ModuleGenerator.RESULT_VAR_NAME);
        }
    }
}
