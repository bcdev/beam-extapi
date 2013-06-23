package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.*;

import static org.esa.beam.extapi.gen.JavadocHelpers.firstCharUp;
import static org.esa.beam.extapi.gen.JavadocHelpers.getComponentCTypeName;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.*;

/**
 * @author Norman Fomferra
 */
public abstract class CFunctionGenerator extends AbstractFunctionGenerator {

    protected CFunctionGenerator(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
        super(apiMethod, parameterGenerators);
    }

    @Override
    public String generateDocText(GeneratorContext context) {
        return JavadocHelpers.convertToDoxygenDoc(context.getApiInfo(), apiMethod.getMemberDoc());
    }

    @Override
    public String generateFunctionSignature(GeneratorContext context) {
        String returnTypeName = JavadocHelpers.getCTypeName(getReturnType());
        String functionName = generateFunctionName(context);
        String parameterList = getParameterList(context);
        return format("${type} ${name}(${params})",
                      kv("type", returnTypeName),
                      kv("name", functionName),
                      kv("params", parameterList.isEmpty() ? "" : parameterList));
    }

    private String getParameterList(GeneratorContext context) {
        StringBuilder parameterList = new StringBuilder();
        if (isInstanceMethod()) {
            parameterList.append(format("${type} ${this}",
                                        kv("type", JavadocHelpers.getCTypeName(getEnclosingClass().getType())),
                                        kv("this", THIS_VAR_NAME)));
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

    protected String getDefaultTargetResultDeclaration(Type type) {
        String targetTypeName = JavadocHelpers.getCTypeName(type);
        return format("${type} ${res} = (${type}) 0;",
                      kv("type", targetTypeName),
                      kv("res", RESULT_VAR_NAME));
    }

    @Override
    public String generateEnterCode(GeneratorContext context) {
        final ApiMethod apiMethod = getApiMethod();

        return eval("" +
                            "if (${mv} == NULL) {\n"
                            + "    if (beam_initApi() == 0) {\n"
                            + "        ${mv} = (*jenv)->${f}(jenv, ${c}, \"${name}\", \"${sig}\");\n"
                            + "        if (${mv} == NULL) {\n"
                            + "            /* Set global error */\n"
                            + "        }\n"
                            + "    }\n"
                            + "    if (${mv} == NULL) {\n"
                            + "        " + (JavadocHelpers.isVoid(apiMethod.getReturnType()) ? "return" : "return ${r}") + ";\n"
                            + "    }\n"
                            + "}\n",
                    kv("mv", METHOD_VAR_NAME),
                    kv("r", RESULT_VAR_NAME),
                    kv("f", apiMethod.getMemberDoc().isStatic() ? "GetStaticMethodID" : "GetMethodID"),
                    kv("c", ModuleGenerator.getComponentCClassVarName(apiMethod.getEnclosingClass().getType())),
                    kv("name", apiMethod.getJavaName()),
                    kv("sig", apiMethod.getJavaSignature()));
    }

    protected String getDefaultObjectReturnStatement() {
        return format("return ${res} != NULL ? (*jenv)->NewGlobalRef(jenv, ${res}) : NULL;",
                      kv("res", RESULT_VAR_NAME));
    }

    protected String getJniObjectMethodCall(GeneratorContext context) {
        return getJniMethodCall(context, "Object");
    }

    protected String getJniMethodCall(GeneratorContext context, String methodTypeName) {
        return getJniMethodCall(context, methodTypeName, METHOD_VAR_NAME, THIS_VAR_NAME);
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
            return null;
        }
    }

    static class Constructor extends ReturnValueCallable {

        Constructor(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${res} = ${call};",
                          kv("res", RESULT_VAR_NAME),
                          kv("call", getJniObjectMethodCall(context)));
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
            String typeName = firstCharUp(getReturnType().simpleTypeName());
            return format("${res} = ${call};",
                          kv("res", RESULT_VAR_NAME),
                          kv("call", getJniMethodCall(context, typeName)));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return format("return ${res};",
                          kv("res", RESULT_VAR_NAME));
        }

    }

    static class ObjectMethod extends ValueMethod {
        ObjectMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${res} = ${call};",
                          kv("res", RESULT_VAR_NAME),
                          kv("call", getJniObjectMethodCall(context)));
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
            return format("jstring ${res}String = NULL;",
                          kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${res}String = ${call};",
                          kv("res", RESULT_VAR_NAME),
                          kv("call", getJniObjectMethodCall(context)));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res} = beam_newCString(${res}String);",
                          kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultDeref(GeneratorContext context) {
            return format("(*jenv)->DeleteLocalRef(jenv, ${res}String);",
                          kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return format("return ${res};",
                          kv("res", RESULT_VAR_NAME));
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateExtraFunctionParamDeclaration(GeneratorContext context) {
            return format("int* ${res}ArrayLength", kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            return super.generateTargetResultDeclaration(context);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return format("jarray ${res}Array = NULL;",
                          kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${res}Array = ${call};",
                          kv("res", RESULT_VAR_NAME),
                          kv("call", getJniObjectMethodCall(context)));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            if (!hasReturnParameter(context)) {
                return format("${res} = ${func}(${res}Array, ${res}ArrayLength);",
                              kv("res", RESULT_VAR_NAME),
                              kv("func", getArrayAllocFunctionName()));
            }
            return null;
        }

        @Override
        public String generateJniResultDeref(GeneratorContext context) {
            return format("(*jenv)->DeleteLocalRef(jenv, ${res}Array);",
                          kv("res", RESULT_VAR_NAME));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return format("return ${res};",
                          kv("res", RESULT_VAR_NAME));
        }

        protected abstract String getArrayAllocFunctionName();
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getArrayAllocFunctionName() {
            return format("beam_newC${type}Array",
                          kv("type", firstCharUp(getComponentCTypeName(getReturnType()))));
        }
    }


    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getArrayAllocFunctionName() {
            return "beam_newCObjectArray";
        }
    }

    static class StringArrayMethod extends ArrayMethod {

        StringArrayMethod(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        protected String getArrayAllocFunctionName() {
            return "beam_newCStringArray";
        }
    }
}
