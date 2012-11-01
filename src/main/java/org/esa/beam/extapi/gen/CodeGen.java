package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
interface CodeGen {
    String generateParamListDecl(GeneratorContext context);

    String generateLocalVarDecl(GeneratorContext context);

    String generatePreCallCode(GeneratorContext context);

    String generateCallCode(GeneratorContext context);

    String generatePostCallCode(GeneratorContext context);
}
