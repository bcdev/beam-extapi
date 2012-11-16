package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public class TypeHelpers {
    public static String getCTypeName(Type type) {
        String name = getComponentCTypeName(type);
        return name + type.dimension().replace("[]", "*");
    }

    public static String getComponentCTypeName(Type type) {
        if (type.isPrimitive()) {
            if (type.typeName().equals("long")) {
                return "dlong";
            } else {
                return type.typeName();
            }
        } else {
            if (isString(type)) {
                return "char*";
            } else {
                return type.typeName().replace('.', '_');
            }
        }
    }

    public static boolean isVoid(Type type) {
        return type.qualifiedTypeName().equals("void");
    }

    public static boolean isString(Type type) {
        return type.qualifiedTypeName().equals("java.lang.String");
    }

    public static boolean isPrimitiveArray(Type type) {
        return type.dimension().equals("[]") && type.isPrimitive();
    }

    public static boolean isStringArray(Type type) {
        return type.dimension().equals("[]") && isString(type);
    }

    public static boolean isObjectArray(Type type) {
        return type.dimension().equals("[]") && !type.isPrimitive();
    }
}
