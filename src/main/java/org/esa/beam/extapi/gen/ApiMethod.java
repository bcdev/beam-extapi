package org.esa.beam.extapi.gen;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * Represents a callable element of the Java API. May be a constructor or method.
 * Immutable object.
 *
 * @author Norman Fomferra
 */
public final class ApiMethod implements Comparable<ApiMethod> {

    private final ApiClass enclosingClass;
    private final ExecutableMemberDoc memberDoc;
    private final Type returnType;
    private final String javaName;
    private final String javaSignature;

    public ApiMethod(ApiClass enclosingClass, ExecutableMemberDoc memberDoc) {
        this.enclosingClass = enclosingClass;
        this.memberDoc = memberDoc;
        this.returnType = memberDoc.isConstructor() ? enclosingClass.getType() : ((MethodDoc) memberDoc).returnType();
        this.javaName = memberDoc.isConstructor() ? "<init>" : memberDoc.name();
        this.javaSignature = getJavaSignature(memberDoc);
    }

    public ApiClass getEnclosingClass() {
        return enclosingClass;
    }

    public ExecutableMemberDoc getMemberDoc() {
        return memberDoc;
    }

    public String getJavaName() {
        return javaName;
    }

    public String getJavaSignature() {
        return javaSignature;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public int compareTo(ApiMethod o) {
        int n = enclosingClass.compareTo(o.enclosingClass);
        if (n != 0) {
            return n;
        }
        n = javaName.compareTo(o.javaName);
        if (n != 0) {
            return n;
        }
        return javaSignature.compareTo(o.javaSignature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiMethod)) return false;

        ApiMethod apiMethod = (ApiMethod) o;

        return enclosingClass.equals(apiMethod.enclosingClass)
                && javaName.equals(apiMethod.javaName)
                && javaSignature.equals(apiMethod.javaSignature);
    }

    @Override
    public int hashCode() {
        int result = enclosingClass.hashCode();
        result = 31 * result + javaName.hashCode();
        result = 31 * result + javaSignature.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getEnclosingClass() + "#" + getJavaName() + ":" + getJavaSignature();
    }

    static String getJavaSignature(ExecutableMemberDoc memberDoc) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Parameter parameter : memberDoc.parameters()) {
            sb.append(getJavaSignature(parameter.type()));
        }
        sb.append(')');
        if (memberDoc instanceof MethodDoc) {
            sb.append(getJavaSignature(((MethodDoc) memberDoc).returnType()));
        } else {
            sb.append('V');
        }
        return sb.toString();
    }

    static String getJavaSignature(Type type) {
        String comp;
        if (type.isPrimitive()) {
            if ("boolean".equals(type.typeName())) {
                comp = "Z";
            } else if ("byte".equals(type.typeName())) {
                comp = "B";
            } else if ("char".equals(type.typeName())) {
                comp = "Constructor";
            } else if ("short".equals(type.typeName())) {
                comp = "S";
            } else if ("int".equals(type.typeName())) {
                comp = "I";
            } else if ("long".equals(type.typeName())) {
                comp = "J";
            } else if ("float".equals(type.typeName())) {
                comp = "F";
            } else if ("double".equals(type.typeName())) {
                comp = "D";
            } else if ("void".equals(type.typeName())) {
                comp = "V";
            } else {
                throw new IllegalStateException();
            }
        } else {
            comp = "L" + type.qualifiedTypeName().replace('.', '/') + ";";
        }
        if (!type.dimension().isEmpty()) {
            return type.dimension().replace("]", "") + comp;
        }
        return comp;
    }
}
