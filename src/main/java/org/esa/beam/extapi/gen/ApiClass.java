package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Norman Fomferra
 */
class ApiClass implements Comparable<ApiClass> {
    private final String javaName;
    private final String resourceName;
    private final List<ApiMethod> apiMethods;
    private final Type type;

    public ApiClass(Type type) throws ClassNotFoundException {
        this.type = type;
        this.javaName = type.qualifiedTypeName();
        this.resourceName = type.qualifiedTypeName().replace(".", "/");
        this.apiMethods = new ArrayList<ApiMethod>(64);
    }

    @Override
    public String toString() {
        return "ApiClass[javaName=" + javaName + "]";
    }

    public Type getType() {
        return type;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void addApiMethod(ApiMethod apiMethod) {
        apiMethods.add(apiMethod);
    }

    public List<ApiMethod> getApiMethods() {
        return Collections.unmodifiableList(apiMethods);
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
