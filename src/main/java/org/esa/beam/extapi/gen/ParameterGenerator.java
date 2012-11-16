package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.CodeGenerator;
import org.esa.beam.extapi.gen.GeneratorContext;

/**
 * @author Norman Fomferra
 */
public interface ParameterGenerator extends CodeGenerator {
    String getName();

    Type getType();

    @Override
    String generateParamListDecl(GeneratorContext context);

    @Override
    String generateLocalVarDecl(GeneratorContext context);

    @Override
    String generatePreCallCode(GeneratorContext context);

    String generateCallCode(GeneratorContext context);

    @Override
    String generatePostCallCode(GeneratorContext context);
}
