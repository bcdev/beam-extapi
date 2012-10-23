package org.esa.beam.extapi.gen;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public class ApiMethod implements Comparable<ApiMethod> {
    private final ApiClass apiClass;
    private final MethodDoc methodDoc;
    private final String javaName;
    private final String externalName;
    private final String javaSignature;

    public ApiMethod(ApiClass apiClass, MethodDoc methodDoc) throws ClassNotFoundException, NoSuchMethodException {
        this.apiClass = apiClass;
        this.methodDoc = methodDoc;
        javaName = methodDoc.name();
        javaSignature = getTypeSignature(methodDoc);
        externalName = apiClass.getExternalName() + "_" + javaName;
    }

    public ApiClass getApiClass() {
        return apiClass;
    }

    public MethodDoc getMethodDoc() {
        return methodDoc;
    }

    public String getJavaName() {
        return javaName;
    }

    public String getJavaSignature() {
        return javaSignature;
    }

    public String getExternalName() {
        return externalName;
    }

    static String getTypeSignature(MethodDoc methodDoc) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Parameter parameter : methodDoc.parameters()) {
            sb.append(getTypeSignature(parameter.type()));
        }
        sb.append(')');
        sb.append(getTypeSignature(methodDoc.returnType()));
        return sb.toString();
    }

    static String getTypeSignature(Type type) {
        String comp;
        if (type.isPrimitive()) {
            if ("boolean".equals(type.typeName())) {
                comp = "Z";
            } else if ("byte".equals(type.typeName())) {
                comp = "B";
            } else if ("char".equals(type.typeName())) {
                comp = "C";
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

    @Override
    public int compareTo(ApiMethod o) {
        int n = javaName.compareTo(o.javaName);
        return n == 0 ? javaSignature.compareTo(o.javaSignature) : n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiMethod)) {
            return false;
        }

        ApiMethod apiMethod = (ApiMethod) o;

        return javaName.equals(apiMethod.javaName) && javaSignature.equals(apiMethod.javaSignature);

    }

    @Override
    public int hashCode() {
        int result = javaName.hashCode();
        result = 31 * result + javaSignature.hashCode();
        return result;
    }
}
