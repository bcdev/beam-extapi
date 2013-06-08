package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.CodeGenerator;
import org.esa.beam.extapi.gen.GeneratorContext;

/**
 * @author Norman Fomferra
 */
public interface ParameterGenerator {
    String getName();

    Type getType();

    String generateParamListDecl(GeneratorContext context);

    String generateLocalVarDecl(GeneratorContext context);

    String generatePreCallCode(GeneratorContext context);

    String generateCallCode(GeneratorContext context);

    String generatePostCallCode(GeneratorContext context);
}
