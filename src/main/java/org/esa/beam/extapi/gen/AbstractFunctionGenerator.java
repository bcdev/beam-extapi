package org.esa.beam.extapi.gen;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.*;

/**
 * @author Norman Fomferra
 */
public abstract class AbstractFunctionGenerator implements FunctionGenerator {

    protected final ApiMethod apiMethod;
    protected final ParameterGenerator[] parameterGenerators;

    protected AbstractFunctionGenerator(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
        this.apiMethod = apiMethod;
        this.parameterGenerators = parameterGenerators;
    }

    @Override
    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    @Override
    public ParameterGenerator[] getParameterGenerators() {
        return parameterGenerators;
    }

    public ApiClass getEnclosingClass() {
        return getApiMethod().getEnclosingClass();
    }

    public boolean isInstanceMethod() {
        return !getMemberDoc().isStatic() && !getMemberDoc().isConstructor();
    }

    public ExecutableMemberDoc getMemberDoc() {
        return getApiMethod().getMemberDoc();
    }

    public Type getReturnType() {
        return getApiMethod().getReturnType();
    }

    @Override
    public String generateFunctionName(GeneratorContext context) {
        return context.getFunctionNameFor(getApiMethod());
    }

    @Override
    public String generateLocalVarDeclarations(GeneratorContext context) {
        return eval("static jmethodID ${mv} = NULL;\n", kv("mv", METHOD_VAR_NAME));
    }

    @Override
    public String generateExtraFunctionParamDeclaration(GeneratorContext context) {
        return null;
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
                    kv("c", CModuleGenerator.getComponentCClassVarName(apiMethod.getEnclosingClass().getType())),
                    kv("name", apiMethod.getJavaName()),
                    kv("sig", apiMethod.getJavaSignature()));
    }

    @Override
    public String generateExitCode(GeneratorContext context) {
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

    protected String getJniObjectMethodCall(GeneratorContext context) {
        return getJniMethodCall(context, "Object");
    }

    protected String getJniMethodCall(GeneratorContext context, String methodTypeName) {

        final String functionName;
        final StringBuilder argumentList = new StringBuilder();

        if (getMemberDoc().isConstructor()) {
            functionName = "NewObject";
            argumentList.append(CModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else if (getMemberDoc().isStatic()) {
            functionName = String.format("CallStatic%sMethod", methodTypeName);
            argumentList.append(CModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(METHOD_VAR_NAME);
        } else {
            functionName = String.format("Call%sMethod", methodTypeName);
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

}
