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
    private String lengthExpr;

    public ApiParameter(Parameter parameter, Modifier modifier) {
        this(parameter, modifier, null);
    }

    public ApiParameter(Parameter parameter, Modifier modifier, String lengthExpr) {
        this.parameter = parameter;
        this.modifier = modifier;
        this.lengthExpr = lengthExpr;
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

    public String getLengthExpr() {
        return lengthExpr;
    }
}
