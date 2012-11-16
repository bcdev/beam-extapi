package org.esa.beam.extapi.gen;

/**
 * Interface whose only use is to make sure generator method names are same in
 * {@link org.esa.beam.extapi.gen.c.CFunctionGenerator} and {@link org.esa.beam.extapi.gen.c.CParameterGenerator}.
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
