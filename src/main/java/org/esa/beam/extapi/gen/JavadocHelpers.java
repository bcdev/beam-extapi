package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

import static org.esa.beam.extapi.gen.ApiInfo.unfoldType;

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

    public static String getJavaName(Type type) {
        final String tn = type.typeName();
        final String qtn = type.qualifiedTypeName();
        int pos = qtn.indexOf(tn);
        if (pos > 0 && pos == qtn.length() - tn.length()) {
            // Must be a nested class, reconstruct '$' that Javadoc has replaced by '.'
            return qtn.substring(0, pos) + tn.replace('.', '$');
        } else {
            return qtn;
        }
    }

    public static String getResourceName(Type type) {
        final String tn = type.typeName();
        final String qtn = type.qualifiedTypeName();
        int pos = qtn.indexOf(tn);
        if (pos > 0 && pos == qtn.length() - tn.length()) {
            // Must be a nested class, reconstruct '$' that Javadoc has replaced by '.'
            return qtn.substring(0, pos).replace('.', '/') + tn.replace('.', '$');
        } else {
            return type.qualifiedTypeName().replace('.', '/');
        }
    }

    static String getJavaSignature(ExecutableMemberDoc memberDoc) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Parameter parameter : memberDoc.parameters()) {
            sb.append(getJavaSignature(parameter.type()));
        }
        sb.append(')');
        if (memberDoc instanceof MethodDoc) {
            sb.append(getJavaSignature(((MethodDoc) memberDoc).returnType()));
        } else {
            sb.append('V');
        }
        return sb.toString();
    }

    static String getJavaSignature(Type type) {
        String comp;
        if (type.isPrimitive()) {
            if ("boolean".equals(type.typeName())) {
                comp = "Z";
            } else if ("byte".equals(type.typeName())) {
                comp = "B";
            } else if ("char".equals(type.typeName())) {
                comp = "Constructor";
            } else if ("short".equals(type.typeName())) {
                comp = "S";
            } else if ("int".equals(type.typeName())) {
                comp = "I";
            } else if ("long".equals(type.typeName())) {
                comp = "J";
            } else if ("float".equals(type.typeName())) {
                comp = "F";
            } else if ("double".equals(type.typeName())) {
                comp = "D";
            } else if ("void".equals(type.typeName())) {
                comp = "V";
            } else {
                throw new IllegalStateException();
            }
        } else {
            Type unfoldType = unfoldType(type);
            comp = "L" + getResourceName(unfoldType) + ";";
        }
        if (!type.dimension().isEmpty()) {
            return type.dimension().replace("]", "") + comp;
        }
        return comp;
    }

    public static boolean isObject(Type type) {
        return !type.isPrimitive() && !isString(type);
    }

    public static boolean isVoid(Type type) {
        return type.qualifiedTypeName().equals("void");
    }

    public static boolean isString(Type type) {
        return type.qualifiedTypeName().equals("java.lang.String");
    }

    public static boolean isMap(Type type) {
        return type.qualifiedTypeName().equals("java.util.Map");
    }

    public static boolean isPrimitiveArray(Type type) {
        return isArray1D(type) && type.isPrimitive();
    }

    public static boolean isStringArray(Type type) {
        return isArray1D(type) && isString(type);
    }

    public static boolean isObjectArray(Type type) {
        return isArray1D(type) && !type.isPrimitive();
    }

    public static String firstCharUp(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String firstCharDown(String s) {
        return s.isEmpty() ? s : Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static boolean isInstance(ProgramElementDoc elementDoc) {
        return !elementDoc.isStatic() && !elementDoc.isConstructor();
    }

    public static String convertToDoxygenDoc(ApiInfo apiInfo, Doc doc) {
        // todo: generate Doxygen-style C documentation string
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


            // Replace Javadoc tags
            //
            strippedLine = strippedLine.replace("@version", "Version: ");
            strippedLine = strippedLine.replace("@since", "Since version: ");
            strippedLine = strippedLine.replace("@author", "Author: ");
            if (strippedLine.startsWith("@param")) {
                strippedLine = strippedLine.substring("@param".length()).trim();
                int i = strippedLine.indexOf(' ');
                if (i < 0) {
                    i = strippedLine.indexOf('\t');
                }
                if (i > 0) {
                    strippedLine = "Parameter " + strippedLine.substring(0, i) + ": " + strippedLine.substring(i).trim();
                } else {
                    strippedLine = strippedLine.replace("@param", "Parameter ");
                }
                strippedLine = "Returns " + strippedLine;
            } else if (strippedLine.startsWith("@return")) {
                strippedLine = strippedLine.substring("@return".length()).trim();
                strippedLine = "Returns " + firstCharDown(strippedLine);
            }

            // Remove known package names, because we don't have them in python
            // todo: instead of using the config, we shall collect *all* packages names from all classes first and use this list.
            //
            for (String packageName : apiInfo.getConfig().getPackages()) {
                strippedLine = strippedLine.replace(packageName + ".", "");
            }

            // Strip HTML elements.
            // Note that it is important to maintain structural and semantic HTML elements such as <li>, <b>, <pre>, ...
            // todo: find better replacements for HTML tags than empty strings
            //
            strippedLine = strippedLine.replace("<p/>", "");
            strippedLine = strippedLine.replace("<p>", "");
            strippedLine = strippedLine.replace("</p>", "");
            strippedLine = strippedLine.replace("<b>", "");
            strippedLine = strippedLine.replace("</b>", "");
            strippedLine = strippedLine.replace("<i>", "");
            strippedLine = strippedLine.replace("</i>", "");
            strippedLine = strippedLine.replace("<code>", "");
            strippedLine = strippedLine.replace("</code>", "");

            strippedLines.add(indent + strippedLine);
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

    private static boolean isArray1D(Type type) {
        return type.dimension().equals("[]");
    }

}
