package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Java class of the API.
 * Immutable object.
 *
 * @author Norman Fomferra
 */
final class ApiClass implements Comparable<ApiClass> {
    private final Type type;
    private final String javaName;
    private final String resourceName;
    private final List<CodeGenCallable> apiMethods;

    public ApiClass(Type type) {
        this.type = type;
        this.javaName = type.qualifiedTypeName();
        this.resourceName = type.qualifiedTypeName().replace(".", "/");
        this.apiMethods = new ArrayList<CodeGenCallable>(64);
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

    // fix: move to Generator
    public String getTargetComponentTypeName() {
        return Generator.getTargetComponentTypeName(getType(), true);
    }

    // fix: remove
    public void addCallable(CodeGenCallable apiMethod) {
        apiMethods.add(apiMethod);
    }

    // fix: remove
    public List<CodeGenCallable> getCallableList() {
        return Collections.unmodifiableList(apiMethods);
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
