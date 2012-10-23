package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;


// todo - use configurable mapping from Java name to extern name used in C/Python

/**
 * @author Norman Fomferra
 */
public class ApiClass implements Comparable<ApiClass> {
    private final Type type;
    private final String javaName;
    private final String externalName;
    private final String resourceName;

    public ApiClass(Type type) throws ClassNotFoundException {
        this.type = type;
        this.javaName = type.qualifiedTypeName();
        this.resourceName = type.qualifiedTypeName().replace(".", "/");
        this.externalName = type.simpleTypeName();
    }

    public Type getType() {
        return type;
    }

    public String getJavaName() {
        return type.qualifiedTypeName();
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getExternalName() {
        return externalName;
    }

    @Override
    public int compareTo(ApiClass other) {
        return javaName.compareTo(other.javaName);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && javaName.equals(((ApiClass) o).javaName);

    }

    @Override
    public int hashCode() {
        return javaName.hashCode();
    }
}
