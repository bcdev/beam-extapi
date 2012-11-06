package org.esa.beam.extapi.gen;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public class ApiConstant {

    private final ApiClass enclosingClass;
    private final FieldDoc fieldDoc;
    private final String javaName;
    private final String javaSignature;

    public ApiConstant(ApiClass enclosingClass, FieldDoc fieldDoc) {
        this.enclosingClass = enclosingClass;
        this.fieldDoc = fieldDoc;
        javaName = fieldDoc.name();
        javaSignature = ApiMethod.getJavaSignature(fieldDoc.type());
    }

    public ApiClass getEnclosingClass() {
        return enclosingClass;
    }

    public FieldDoc getFieldDoc() {
        return fieldDoc;
    }

    public String getJavaName() {
        return javaName;
    }

    public String getJavaSignature() {
        return javaSignature;
    }

    public Type getType() {
        return fieldDoc.type();
    }

    public Object getValue() {
        return fieldDoc.constantValue();
    }

    @Override
    public String toString() {
        return getEnclosingClass() + "#" + getJavaName() + ":" + getJavaSignature();
    }
}
