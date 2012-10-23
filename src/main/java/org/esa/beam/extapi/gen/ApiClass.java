package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public class ApiClass implements Comparable<ApiClass> {
    public final String javaName;
    public final String simpleName;
    public final String fullName;

    public ApiClass(Type type) {
        this.javaName = type.qualifiedTypeName();
        this.simpleName = type.simpleTypeName().replace("$", ".");
        this.fullName = javaName.replace("$", ".").replace(".", "_");
    }

    @Override
    public int compareTo(ApiClass other) {
        return fullName.compareTo(other.fullName);
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
