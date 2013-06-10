package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.METHOD_VAR_NAME;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.THIS_VAR_NAME;

/**
 * @author Norman Fomferra
 */
public abstract class CFunctionGenerator implements FunctionGenerator {

    protected final ApiMethod apiMethod;
    protected final ParameterGenerator[] parameterGenerators;

    protected CFunctionGenerator(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
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
    public ParameterGenerator[] getParameterGenerators() {
        return parameterGenerators;
    }

    @Override
    public String generateFunctionName(GeneratorContext context) {
        return context.getFunctionNameFor(getApiMethod());
    }

    @Override
    public String generateFunctionSignature(GeneratorContext context) {
        String returnTypeName = JavadocHelpers.getCTypeName(getReturnType());
        String functionName = generateFunctionName(context);
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
    public String generateLocalVarDeclarations(ModuleGenerator moduleGenerator) {
        return String.format("static jmethodID %s = NULL;\n", METHOD_VAR_NAME);
    }

    @Override
    public String generateExtraFunctionParamDeclaration(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateTargetArgsFromParsedParamsAssignment(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateJniResultDeref(GeneratorContext context) {
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

    protected String generateJniCall(GeneratorContext context) {

        final String functionName;
        final StringBuilder argumentList = new StringBuilder();

        if (getMemberDoc().isConstructor()) {
            functionName = "NewObject";
            argumentList.append(CModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else if (getMemberDoc().isStatic()) {
            functionName = String.format("CallStatic%sMethod", generateJniCallTypeName(context));
            argumentList.append(CModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else {
            functionName = String.format("Call%sMethod", generateJniCallTypeName(context));
            argumentList.append(THIS_VAR_NAME);
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        }

        for (ParameterGenerator parameterGenerator : parameterGenerators) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(parameterGenerator.generateJniCallArgs(context));
        }

        return String.format("(*jenv)->%s(jenv, %s)", functionName, argumentList);
    }

    protected String generateParameterList(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
            parameterList.append(String.format("%s %s",
                                               JavadocHelpers.getCTypeName(getEnclosingClass().getType()),
                                               THIS_VAR_NAME));
        }
        for (ParameterGenerator parameterGenerator : parameterGenerators) {
            String decl = parameterGenerator.generateFunctionParamDeclaration(context);
            if (decl != null) {
                if (parameterList.length() > 0) {
                    parameterList.append(", ");
                }
                parameterList.append(decl);
            }
        }
        String decl = generateExtraFunctionParamDeclaration(context);
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

    protected abstract String generateJniCallTypeName(GeneratorContext context);

    @Override
    public String generateDocText(GeneratorContext context) {
        // todo: generate C Doxygen-style documentation
        return JavadocHelpers.convertToDoxygenDoc(context.getApiInfo(), apiMethod.getMemberDoc());
    }

    static class VoidMethod extends CFunctionGenerator {

        VoidMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateJniCallTypeName(GeneratorContext context) {
            return "Void";
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return String.format("%s;", generateJniCall(context));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return null;
        }
    }

    static abstract class ReturnValueCallable extends CFunctionGenerator {
        ReturnValueCallable(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            String targetTypeName = JavadocHelpers.getCTypeName(getReturnType());
            return String.format("%s %s = (%s) 0;", targetTypeName, CModuleGenerator.RESULT_VAR_NAME, targetTypeName);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            // todo - make sure we always have a JniResultDeclaration here for all function types.
            return null;
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return String.format("%s = %s;", CModuleGenerator.RESULT_VAR_NAME, generateJniCall(context));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return String.format("return %s;", CModuleGenerator.RESULT_VAR_NAME);
        }
    }


    static class Constructor extends ReturnValueCallable {

        Constructor(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateJniCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 CModuleGenerator.RESULT_VAR_NAME, CModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {

        ValueMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            String targetTypeName = JavadocHelpers.getCTypeName(getReturnType());
            return String.format("%s %s = (%s) 0;", targetTypeName, CModuleGenerator.RESULT_VAR_NAME, targetTypeName);
        }
    }


    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateJniCallTypeName(GeneratorContext context) {
            String typeName = getReturnType().typeName();
            return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        }

    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String generateJniCallTypeName(GeneratorContext context) {
            return "Object";
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return String.format("return %s != NULL ? (*jenv)->NewGlobalRef(jenv, %s) : NULL;",
                                 CModuleGenerator.RESULT_VAR_NAME, CModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return "jstring _resultString = NULL;";
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return String.format("_resultString = %s;\n" +
                                         "%s = beam_alloc_string(_resultString);",
                                 generateJniCall(context), CModuleGenerator.RESULT_VAR_NAME);
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return String.format("return %s;", CModuleGenerator.RESULT_VAR_NAME);
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateExtraFunctionParamDeclaration(GeneratorContext context) {
            return "int* resultArrayLength";
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            return super.generateTargetResultDeclaration(context);

        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return eval("jarray ${r}Array = NULL;",
                          kv("r", CModuleGenerator.RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            if (hasReturnParameter(context)) {
                // NOTE: ParameterGenerator.<T>Array will generate code which sets ${r} = ...
                return eval("${r}Array = ${c};",
                            kv("r", CModuleGenerator.RESULT_VAR_NAME),
                            kv("c", generateJniCall(context)));
            } else {
                return eval("${r}Array = ${c};\n" +
                                    "${r} = ${f}(${r}Array, resultArrayLength);",
                            kv("r", CModuleGenerator.RESULT_VAR_NAME),
                            kv("c", generateJniCall(context)),
                            kv("f", getAllocFunctionName()));
            }
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return eval("return ${r};",
                        kv("r", CModuleGenerator.RESULT_VAR_NAME));
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
                                 JavadocHelpers.getComponentCTypeName(getReturnType()));
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
