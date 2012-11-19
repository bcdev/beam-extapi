package org.esa.beam.extapi.gen;

import com.sun.javadoc.Doc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public class JavadocHelpers {

    public static String encodeRawDocText(Doc doc) {
        final String text = doc.getRawCommentText();
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "    ");
    }


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

    public static String firstCharToUpperCase(String typeName) {
        typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        return typeName;
    }

    public static boolean isInstance(ProgramElementDoc elementDoc) {
        return !elementDoc.isStatic() && !elementDoc.isConstructor();
    }
}
