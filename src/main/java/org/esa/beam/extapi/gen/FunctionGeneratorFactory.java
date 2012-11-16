package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface FunctionGeneratorFactory {
    FunctionGenerator createFunctionGenerator(ApiMethod apiMethod) throws GeneratorException;

    ParameterGenerator[] createParameterGenerators(ApiMethod apiMethod) throws GeneratorException;
}
