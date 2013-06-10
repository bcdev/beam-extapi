package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface FunctionGenerator {
    ApiMethod getApiMethod();

    ParameterGenerator[] getParameterGenerators();

    String generateDocText(GeneratorContext context);

    String generateFunctionName(GeneratorContext context);

    String generateFunctionSignature(GeneratorContext context);

    String generateExtraFunctionParamDeclaration(GeneratorContext context);

    String generateLocalVarDeclarations(ModuleGenerator moduleGenerator);

    String generateTargetResultDeclaration(GeneratorContext context);

    String generateJniResultDeclaration(GeneratorContext context);

    String generateTargetArgsFromParsedParamsAssignment(GeneratorContext context);

    String generateJniResultFromJniCallAssignment(GeneratorContext context);

    String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context);

    String generateJniResultDeref(GeneratorContext context);

    String generateReturnStatement(GeneratorContext context);
}
