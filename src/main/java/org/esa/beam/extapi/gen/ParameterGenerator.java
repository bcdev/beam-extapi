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
}
