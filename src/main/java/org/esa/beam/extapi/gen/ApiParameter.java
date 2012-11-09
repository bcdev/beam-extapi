package org.esa.beam.extapi.gen;

import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public class ApiParameter {
    public static enum Modifier {
        IN,
        OUT,
        RETURN
    }

    private final Parameter parameter;
    private final Modifier modifier;

    public ApiParameter(Parameter parameter, Modifier modifier) {
        this.parameter = parameter;
        this.modifier = modifier;
    }

    public Parameter getParameterDoc() {
        return parameter;
    }

    public String getJavaName() {
        return parameter.name();
    }

    public Type getType() {
        return parameter.type();
    }

    public String getJavaSignature() {
        return ApiMethod.getJavaSignature(parameter.type());
    }

    public Modifier getModifier() {
        return modifier;
    }
}
