package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

/**
* @author Norman Fomferra
*/
class MyParameter implements Parameter {
    String name;
    Type type;

    MyParameter(String name, Class type) {
        this.name = name;
        this.type = new MyType(type);
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String typeName() {
        return type.typeName();
    }

    @Override
    public AnnotationDesc[] annotations() {
        return new AnnotationDesc[0];
    }

}
