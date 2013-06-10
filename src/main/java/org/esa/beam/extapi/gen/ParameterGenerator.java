package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public interface ParameterGenerator {
    String getName();

    Type getType();

    String generateFunctionParamDeclaration(GeneratorContext context);

    String generateJniArgDeclaration(GeneratorContext context);

    String generateTargetArgDeclaration(GeneratorContext context);

    String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context);

    /**
     * Contribution of this parameter to the JNI call arguments list.
     */
    String generateJniCallArgs(GeneratorContext context);

    String generateJniArgDeref(GeneratorContext context);

    /**
     * Parameters may be return values
     */
    String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context);
}
