package org.esa.beam.extapi.gen;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Type;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public abstract class AbstractFunctionGenerator implements FunctionGenerator {

    protected final ApiMethod apiMethod;
    protected final ParameterGenerator[] parameterGenerators;
    protected final TemplateEval templateEval;

    protected AbstractFunctionGenerator(ApiMethod apiMethod, ParameterGenerator[] parameterGenerators) {
        this.apiMethod = apiMethod;
        this.parameterGenerators = parameterGenerators;
        this.templateEval = TemplateEval.create();
    }

    @Override
    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    @Override
    public ParameterGenerator[] getParameterGenerators() {
        return parameterGenerators;
    }

    protected String format(String pattern, TemplateEval.KV... pairs) {
        return templateEval.add(pairs).eval(pattern);
    }

    public ApiClass getEnclosingClass() {
        return getApiMethod().getEnclosingClass();
    }

    public boolean isInstanceMethod() {
        return JavadocHelpers.isInstance(getMemberDoc());
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
        return eval("static jmethodID ${method} = NULL;\n", kv("method", ModuleGenerator.METHOD_VAR_NAME));
    }

    @Override
    public String generateExtraFunctionParamDeclaration(GeneratorContext context) {
        return null;
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
            argumentList.append(ModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(ModuleGenerator.METHOD_VAR_NAME);
        } else if (getMemberDoc().isStatic()) {
            functionName = String.format("CallStatic%sMethod", methodTypeName);
            argumentList.append(ModuleGenerator.getComponentCClassVarName(getEnclosingClass().getType()));
            argumentList.append(", ");
            argumentList.append(ModuleGenerator.METHOD_VAR_NAME);
        } else {
            functionName = String.format("Call%sMethod", methodTypeName);
            argumentList.append(ModuleGenerator.THIS_VAR_NAME);
            argumentList.append(", ");
            argumentList.append(ModuleGenerator.METHOD_VAR_NAME);
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
