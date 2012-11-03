package org.esa.beam.extapi.gen;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;

/**
 * Represents a callable element of the Java API. May be a constructor or method.
 * Immutable object.
 *
 * @author Norman Fomferra
 */
final class ApiMethod implements Comparable<ApiMethod> {

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
        this.javaSignature = Generator.getTypeSignature(memberDoc);
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

    public boolean isInstanceMethod() {
        return !getMemberDoc().isStatic() && !getMemberDoc().isConstructor();
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
}
