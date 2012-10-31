package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

/**
* @author Norman Fomferra
*/
class MyType implements Type {

    final Class baseClass;
    final int dimCount;

    MyType(Class c) {
        this.baseClass = getBaseClass(c);
        this.dimCount = getDimCount(c);
    }

    @Override
    public String typeName() {
        int i = baseClass.getName().lastIndexOf('.');
        return (i > 0 ? baseClass.getName().substring(i + 1) : baseClass.getName()).replace('$', '.');
    }

    @Override
    public String qualifiedTypeName() {
        return baseClass.getName();
    }

    @Override
    public String simpleTypeName() {
        return baseClass.getSimpleName();
    }

    @Override
    public String dimension() {
        String s = "";
        for (int i = 0; i < dimCount; i++) {
            s += "[]";
        }
        return s;
    }

    @Override
    public boolean isPrimitive() {
        return baseClass.isPrimitive();
    }

    @Override
    public ClassDoc asClassDoc() {
        return null;
    }

    @Override
    public ParameterizedType asParameterizedType() {
        return null;
    }

    @Override
    public TypeVariable asTypeVariable() {
        return null;
    }

    @Override
    public WildcardType asWildcardType() {
        return null;
    }

    @Override
    public AnnotationTypeDoc asAnnotationTypeDoc() {
        return null;
    }

    static Class getBaseClass(Class c) {
        Class ct = c;
        while (ct.getComponentType() != null) {
            ct = ct.getComponentType();
        }
        return ct;
    }

    static int getDimCount(Class c) {
        Class ct = c;
        int n = 0;
        while (ct.getComponentType() != null) {
            ct = ct.getComponentType();
            n++;
        }
        return n;
    }

}
