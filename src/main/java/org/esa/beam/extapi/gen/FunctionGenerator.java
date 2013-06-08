package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface FunctionGenerator {
    ApiMethod getApiMethod();

    ParameterGenerator[] getParameterGenerators();

    String generateParamListDecl(GeneratorContext context);

    String generateLocalVarDecl(GeneratorContext context);

    String generatePreCallCode(GeneratorContext context);

    String generateCallCode(GeneratorContext context);

    String generatePostCallCode(GeneratorContext context);

    String getFunctionName(GeneratorContext context);

    String generateInitCode(GeneratorContext context);

    String generateFunctionSignature(GeneratorContext context);

    String generateReturnCode(GeneratorContext context);

    String generateDocText(GeneratorContext context);
}
