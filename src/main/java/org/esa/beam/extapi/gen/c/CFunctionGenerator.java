package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.RESULT_VAR_NAME;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.THIS_VAR_NAME;

/**
 * @author Norman Fomferra
 */
public abstract class CFunctionGenerator extends AbstractFunctionGenerator {

    protected CFunctionGenerator(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
        super(apiMethod, parameterGenerators);
    }

    @Override
    public String generateDocText(GeneratorContext context) {
        // todo: generate C Doxygen-style documentation
        return JavadocHelpers.convertToDoxygenDoc(context.getApiInfo(), apiMethod.getMemberDoc());
    }

    @Override
    public String generateFunctionSignature(GeneratorContext context) {
        String returnTypeName = JavadocHelpers.getCTypeName(getReturnType());
        String functionName = generateFunctionName(context);
        String parameterList = getParameterList(context);
        return eval("${type} ${name}(${params})",
                    kv("type", returnTypeName),
                    kv("name", functionName),
                    kv("params", parameterList.isEmpty() ? "" : parameterList));
    }

    private String getParameterList(GeneratorContext context) {
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

    private static String getDefaultTargetResultDeclaration(Type type) {
        String targetTypeName = JavadocHelpers.getCTypeName(type);
        return eval("${t} ${r} = (${t}) 0;",
                    kv("t", targetTypeName),
                    kv("r", RESULT_VAR_NAME));
    }

    private static String getDefaultObjectReturnStatement() {
        return eval("return ${r} != NULL ? (*jenv)->NewGlobalRef(jenv, ${r}) : NULL;",
                    kv("r", RESULT_VAR_NAME));
    }

    static class VoidMethod extends CFunctionGenerator {

        VoidMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
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
            return getJniMethodCall(context, "Void") + ";";
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
            return getDefaultTargetResultDeclaration(getReturnType());
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            // todo - make sure we always have a JniResultDeclaration here for all function types.
            return null;
        }
    }

    static class Constructor extends ReturnValueCallable {

        Constructor(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return eval("${r} = ${c};",
                        kv("r", RESULT_VAR_NAME),
                        kv("c", getJniObjectMethodCall(context)));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return getDefaultObjectReturnStatement();
        }
    }

    static abstract class ValueMethod extends ReturnValueCallable {

        ValueMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            return getDefaultTargetResultDeclaration(getReturnType());
        }
    }


    static class PrimitiveMethod extends ValueMethod {
        PrimitiveMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            String t = getPrimitiveTypeTypeName();
            return eval("${r} = ${c};",
                        kv("r", RESULT_VAR_NAME),
                        kv("c", getJniMethodCall(context, t)));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return eval("return ${r};",
                        kv("r", RESULT_VAR_NAME));
        }

        private String getPrimitiveTypeTypeName() {
            String typeName = getReturnType().typeName();
            return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        }

    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return eval("${r} = ${c};",
                        kv("r", RESULT_VAR_NAME),
                        kv("c", getJniObjectMethodCall(context)));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return getDefaultObjectReturnStatement();
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return eval("jstring ${r}String = NULL;",
                        kv("r", RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return eval("${r}String = ${c};",
                        kv("r", RESULT_VAR_NAME),
                        kv("c", getJniObjectMethodCall(context)));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return eval("${r} = beam_alloc_string(${r}String);",
                        kv("r", RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${r}String);",
                        kv("r", RESULT_VAR_NAME));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return eval("return ${r};",
                        kv("r", RESULT_VAR_NAME));
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
                        kv("r", RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return eval("${r}Array = ${c};",
                        kv("r", RESULT_VAR_NAME),
                        kv("c", getJniObjectMethodCall(context)));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            if (!hasReturnParameter(context)) {
                return eval("${r} = ${f}(${r}Array, resultArrayLength);",
                            kv("r", RESULT_VAR_NAME),
                            kv("f", getArrayAllocFunctionName()));
            }
            return null;
        }

        @Override
        public String generateJniResultDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${r}Array);",
                        kv("r", RESULT_VAR_NAME));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return eval("return ${r};",
                        kv("r", RESULT_VAR_NAME));
        }

        protected abstract String getArrayAllocFunctionName();
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getArrayAllocFunctionName() {
            return String.format("beam_alloc_%s_array",
                                 JavadocHelpers.getComponentCTypeName(getReturnType()));
        }
    }


    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getArrayAllocFunctionName() {
            return "beam_alloc_object_array";
        }
    }

    static class StringArrayMethod extends ArrayMethod {

        StringArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getArrayAllocFunctionName() {
            return "beam_alloc_string_array";
        }
    }
}
