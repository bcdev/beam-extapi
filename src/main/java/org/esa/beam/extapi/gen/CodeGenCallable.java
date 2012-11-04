package org.esa.beam.extapi.gen;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;

import static org.esa.beam.extapi.gen.Generator.*;

/**
 * @author Norman Fomferra
 */
abstract class CodeGenCallable implements Comparable<CodeGenCallable>, CodeGen {

    protected final ApiMethod apiMethod;
    protected final CodeGenParameter[] parameters;

    protected CodeGenCallable(ApiMethod apiMethod, CodeGenParameter[] parameters) {
        this.apiMethod = apiMethod;
        this.parameters = parameters;
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

    public CodeGenParameter[] getParameters() {
        return parameters;
    }

    public String getTargetEnclosingClassVarName() {
        return Generator.getCClassVarName(getEnclosingClass().getType());
    }

    protected String getTargetEnclosingTypeName() {
        return Generator.getCTypeName(getEnclosingClass().getType());
    }

    protected String getTargetReturnTypeName() {
        return Generator.getCTypeName(getReturnType());
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
        String argumentList = generateArgumentList(context);
        String classVarName = getTargetEnclosingClassVarName();
        String functionName = generateJniCallFunctionName(context);
        String memberVarName = METHOD_VAR_NAME;
        if (argumentList.isEmpty()) {
            return String.format("(*jenv)->%s(jenv, %s, %s);",
                                 functionName, classVarName, memberVarName);

        } else {
            return String.format("(*jenv)->%s(jenv, %s, %s, %s);",
                                 functionName, classVarName, memberVarName, argumentList);

        }
    }

    protected String generateJniCallFunctionName(GeneratorContext context) {
        if (getMemberDoc().isConstructor()) {
            return "NewObject";
        } else if (getMemberDoc().isStatic()) {
            return String.format("CallStatic%sMethod", generateCallTypeName(context));
        } else {
            return String.format("Call%sMethod", generateCallTypeName(context));
        }
    }

    protected String generateParameterList(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
            parameterList.append(String.format("%s %s",
                                               getTargetEnclosingTypeName(),
                                               SELF_VAR_NAME));
        }
        for (CodeGenParameter parameter : parameters) {
            String decl = parameter.generateParamListDecl(context);
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

    protected String generateArgumentList(GeneratorContext context) {
        StringBuilder argumentList = new StringBuilder();
        if (isInstanceMethod()) {
            argumentList.append(SELF_VAR_NAME);
        }
        for (CodeGenParameter parameter : parameters) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(parameter.generateCallCode(context));
        }
        return argumentList.toString();
    }

    private boolean isInstanceMethod() {
        return !getMemberDoc().isStatic() && !getMemberDoc().isConstructor();
    }

    protected abstract String generateCallTypeName(GeneratorContext context);

    @Override
    public int compareTo(CodeGenCallable o) {
        return getApiMethod().compareTo(o.getApiMethod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeGenCallable)) return false;
        CodeGenCallable callable = (CodeGenCallable) o;
        return getApiMethod().equals(callable.getApiMethod());
    }

    @Override
    public int hashCode() {
        return getApiMethod().hashCode();
    }

    static class VoidMethod extends CodeGenCallable {

        VoidMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
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

    static abstract class ReturnValueCallable extends CodeGenCallable {
        ReturnValueCallable(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            String targetTypeName = getTargetReturnTypeName();
            return String.format("%s %s = (%s) 0;", targetTypeName, RESULT_VAR_NAME, targetTypeName);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("%s = %s", RESULT_VAR_NAME, generateJniCall(context));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", RESULT_VAR_NAME);
        }
    }


    static class Constructor extends ReturnValueCallable {

        Constructor(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 RESULT_VAR_NAME, RESULT_VAR_NAME);
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {

        ValueMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            String targetTypeName = getTargetReturnTypeName();
            return String.format("%s %s = (%s) 0;", targetTypeName, RESULT_VAR_NAME, targetTypeName);
        }
    }


    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            String typeName = getReturnType().typeName();
            return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        }

    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 RESULT_VAR_NAME, RESULT_VAR_NAME);
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
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
                                 generateJniCall(context), RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", RESULT_VAR_NAME);
        }
    }

    static class PrimitiveArrayMethod extends ObjectMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
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
                                 RESULT_VAR_NAME,
                                 Generator.getTargetComponentTypeName(getReturnType(), false));
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", RESULT_VAR_NAME);
        }
    }


    static class ObjectArrayMethod extends ObjectMethod {
        ObjectArrayMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
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
                                 generateJniCall(context), RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s;", RESULT_VAR_NAME);
        }
    }

    static class StringArrayMethod extends ObjectArrayMethod {

        StringArrayMethod(ApiMethod apiMethod, CodeGenParameter[] codeGenParameters) {
            super(apiMethod, codeGenParameters);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("_resultArray = %s\n" +
                                         "%s = beam_alloc_string_array(_resultArray, resultArrayLength);",
                                 generateJniCall(context), RESULT_VAR_NAME);
        }
    }
}
