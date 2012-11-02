package org.esa.beam.extapi.gen;

import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;

import static org.esa.beam.extapi.gen.Generator.*;

/**
 * @author Norman Fomferra
 */
abstract class CodeGenCallable implements Comparable<CodeGenCallable>, CodeGen {

    protected final ApiClass enclosingClass;
    protected final CodeGenParameter[] parameters;
    protected final String javaName;
    protected final String javaSignature;

    protected CodeGenCallable(ApiClass enclosingClass, ExecutableMemberDoc memberDoc, CodeGenParameter[] parameters) {
        this.enclosingClass = enclosingClass;
        this.parameters = parameters;
        javaName = memberDoc.isConstructor() ? "<init>" : memberDoc.name();
        javaSignature = Generator.getTypeSignature(memberDoc);
    }

    public ApiClass getEnclosingClass() {
        return enclosingClass;
    }

    public String getTargetEnclosingClassVarName() {
        return Generator.getTargetClassVarName(getEnclosingClass().getType());
    }

    public CodeGenParameter[] getParameters() {
        return parameters;
    }

    public String getJavaName() {
        return javaName;
    }

    public String getJavaSignature() {
        return javaSignature;
    }

    public abstract ExecutableMemberDoc getMemberDoc();

    public abstract Type getReturnType();

    protected String getTargetEnclosingTypeName() {
        return Generator.getTargetTypeName(getEnclosingClass().getType());
    }

    protected String getTargetReturnTypeName() {
        return Generator.getTargetTypeName(getReturnType());
    }

    public String getFunctionBaseName() {
        String targetTypeName = getTargetEnclosingTypeName();
        return String.format("%s_%s",
                             targetTypeName, getMemberDoc().name());
    }

    public String generateFunctionSignature(GeneratorContext context) {
        String returnTypeName = getTargetReturnTypeName();
        String functionName = context.getTargetFunctionName(this);
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
        int n = enclosingClass.compareTo(o.enclosingClass);
        if (n != 0) {
            return n;
        }
        n = javaName.compareTo(o.javaName);
        if (n != 0) {
            return n;
        }
        return javaSignature.compareTo(o.javaSignature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeGenCallable)) return false;

        CodeGenCallable apiMethod = (CodeGenCallable) o;

        return enclosingClass.equals(apiMethod.enclosingClass)
                && javaName.equals(apiMethod.javaName)
                && javaSignature.equals(apiMethod.javaSignature);
    }

    @Override
    public int hashCode() {
        int result = enclosingClass.hashCode();
        result = 31 * result + javaName.hashCode();
        result = 31 * result + javaSignature.hashCode();
        return result;
    }

    static class VoidMethod extends CodeGenCallable {

        private final MethodDoc methodDoc;

        VoidMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, methodDoc, codeGenParameters);
            this.methodDoc = methodDoc;
        }

        @Override
        public ExecutableMemberDoc getMemberDoc() {
            return methodDoc;
        }

        @Override
        public Type getReturnType() {
            return methodDoc.returnType();
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

        ReturnValueCallable(ApiClass apiClass, ExecutableMemberDoc memberDoc, CodeGenParameter[] codeGenParameters) {
            super(apiClass, memberDoc, codeGenParameters);
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
        protected final ConstructorDoc constructorDoc;

        Constructor(ApiClass apiClass, ConstructorDoc constructorDoc, CodeGenParameter[] codeGenParameters) {
            super(apiClass, constructorDoc, codeGenParameters);
            this.constructorDoc = constructorDoc;
        }

        @Override
        public ConstructorDoc getMemberDoc() {
            return constructorDoc;
        }

        @Override
        public Type getReturnType() {
            return constructorDoc.containingClass();
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String getFunctionBaseName() {
            String targetTypeName = getTargetEnclosingTypeName();
            return String.format("%s_new%s",
                                 targetTypeName,
                                 targetTypeName);
        }

        @Override
        public String generateReturnCode(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 RESULT_VAR_NAME, RESULT_VAR_NAME);
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {
        protected final MethodDoc methodDoc;

        protected ValueMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, methodDoc, codeGenParameters);
            this.methodDoc = methodDoc;
        }

        @Override
        public MethodDoc getMemberDoc() {
            return methodDoc;
        }

        @Override
        public Type getReturnType() {
            return methodDoc.returnType();
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            String targetTypeName = getTargetReturnTypeName();
            return String.format("%s %s = (%s) 0;", targetTypeName, RESULT_VAR_NAME, targetTypeName);
        }
    }


    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, codeGenParameters, methodDoc);
        }

        @Override
        protected String generateCallTypeName(GeneratorContext context) {
            String typeName = getReturnType().typeName();
            return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        }

    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, codeGenParameters, methodDoc);
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
        StringMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, codeGenParameters, methodDoc);
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
        PrimitiveArrayMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, codeGenParameters, methodDoc);
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
        ObjectArrayMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, codeGenParameters, methodDoc);
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

        StringArrayMethod(ApiClass apiClass, CodeGenParameter[] codeGenParameters, MethodDoc methodDoc) {
            super(apiClass, codeGenParameters, methodDoc);
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("_resultArray = %s\n" +
                                         "%s = beam_alloc_string_array(_resultArray, resultArrayLength);",
                                 generateJniCall(context), RESULT_VAR_NAME);
        }
    }
}
