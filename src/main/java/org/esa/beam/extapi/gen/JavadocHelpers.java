package org.esa.beam.extapi.gen;

import com.sun.javadoc.Doc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Type;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

/**
 * @author Norman Fomferra
 */
public class JavadocHelpers {

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

    public static String convertToDoxygenDoc(ApiInfo apiInfo, Doc doc) {
        String text = doc.getRawCommentText();
        if (text == null) {
            return "";
        }
        text = text.trim();
        if (text.isEmpty()) {
            return "";
        }
        return text;
    }

    public static String convertToPythonDoc(ApiInfo apiInfo, Doc doc) {
        return convertToPythonDoc(apiInfo, doc, "", true);
    }

    public static String convertToPythonDoc(ApiInfo apiInfo, Doc doc, String indent, boolean cCodeString) {
        String text = doc.getRawCommentText();
        if (text == null) {
            return "";
        }
        text = text.trim();
        if (text.isEmpty()) {
            return "";
        }
        final String[] lines = StringUtils.split(text, "\n");
        ArrayList<String> strippedLines = new ArrayList<String>();
        for (String line : lines) {
            String strippedLine = line.trim();
            if (!strippedLine.trim().startsWith("@version")
                    && !strippedLine.trim().startsWith("@author")
                    && !strippedLine.trim().startsWith("@since")) {
                final String[] packages = apiInfo.getConfig().getPackages();
                // Remove known package names, because we don't have them in python
                // todo: instead of using the config, we shall collect *all* packages names
                // from all classes first and use this list.
                for (String packageName : packages) {
                    strippedLine = strippedLine.replace(packageName + ".", "");
                }
                // Strip away HTML elements.
                // todo: find better replacements for HTML tags than empty strings
                // It is important to maintain structural and semantic HTML elements such as <li>, <b>, <pre>, ...
                strippedLine = strippedLine.replace("<p/>", "");
                strippedLine = strippedLine.replace("<p>", "");
                strippedLine = strippedLine.replace("</p>", "");
/*
                strippedLine = strippedLine.replace("<b>", "");
                strippedLine = strippedLine.replace("</b>", "");
                strippedLine = strippedLine.replace("<i>", "");
                strippedLine = strippedLine.replace("</i>", "");
                strippedLine = strippedLine.replace("<code>", "");
                strippedLine = strippedLine.replace("</code>", "");
*/
                strippedLines.add(indent + strippedLine);
            }
        }
        final String docText = StringUtils.join(strippedLines.toArray(new String[strippedLines.size()]), "\n");
        return cCodeString ? encodeCCodeString(docText) : docText;
    }

    public static String encodeCCodeString(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "    ");
    }
}
