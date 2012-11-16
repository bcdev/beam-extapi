package org.esa.beam.extapi.gen;

import org.esa.beam.extapi.gen.c.CParameterGenerator;

/**
 * @author Norman Fomferra
 */
public interface FunctionGenerator extends CodeGenerator {
    ApiMethod getApiMethod();

    CParameterGenerator[] getParameterGenerators();

    String getFunctionName(GeneratorContext context);

    String generateFunctionSignature(GeneratorContext context);

    String generateReturnCode(GeneratorContext context);
}
