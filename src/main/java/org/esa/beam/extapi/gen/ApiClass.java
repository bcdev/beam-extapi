package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

/**
 * Represents a Java class of the API.
 * Immutable object.
 *
 * @author Norman Fomferra
 */
public final class ApiClass implements Comparable<ApiClass> {
    private final Type type;
    private final String javaName;
    private final String resourceName;

    public ApiClass(Type type) {
        if (type.isPrimitive()) {
            throw new IllegalArgumentException("type");
        }

        this.type = type;

        final String s1 = type.typeName();
        final String s2 = type.qualifiedTypeName();
        int pos = s2.indexOf(s1);
        this.javaName = s2.substring(0, pos) + s1.replace('.', '$');
        this.resourceName = s2.substring(0, pos).replace('.', '/') + s1.replace('.', '$');
    }

    public Type getType() {
        return type;
    }

    public String getJavaName() {
        return javaName;
    }

    public String getResourceName() {
        return resourceName;
    }

    @Override
    public int compareTo(ApiClass other) {
        return javaName.compareTo(other.javaName);
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                !(o == null || getClass() != o.getClass())
                        && javaName.equals(((ApiClass) o).javaName);
    }

    @Override
    public int hashCode() {
        return javaName.hashCode();
    }

    @Override
    public String toString() {
        return javaName;
    }
}
