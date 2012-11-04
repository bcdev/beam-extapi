package org.esa.beam.extapi.gen.c;

/**
 * Interface whose only use is to make sure generator method names are same in
 * {@link FunctionGenerator} and {@link ParameterGenerator}.
 *
 * @author Norman Fomferra
 */
public interface CodeGenerator {
    String generateParamListDecl(GeneratorContext context);

    String generateLocalVarDecl(GeneratorContext context);

    String generatePreCallCode(GeneratorContext context);

    String generateCallCode(GeneratorContext context);

    String generatePostCallCode(GeneratorContext context);
}
