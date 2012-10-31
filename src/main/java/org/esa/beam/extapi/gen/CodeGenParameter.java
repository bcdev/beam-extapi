package org.esa.beam.extapi.gen;

import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
abstract class CodeGenParameter {
    protected final Parameter parameter;

    protected CodeGenParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter.name();
    }

    public Type getType() {
        return parameter.type();
    }

    public abstract String generateParamListDecl(CodeGenContext context);

    public String generateLocalVarDecl(CodeGenContext context) {
        return null;
    }

    public String generatePreCallCode(CodeGenContext context) {
        return null;
    }

    public abstract String generateCallArgExpr(CodeGenContext context);

    public String generatePostCallCode(CodeGenContext context) {
        return null;
    }

    static class PrimitiveScalar extends CodeGenParameter {
        PrimitiveScalar(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(CodeGenContext context) {
            final String typeName;
            if (parameter.typeName().equals("long")) {
                typeName = "dlong";
            } else {
                typeName = parameter.typeName();
            }
            return String.format("%s %s", typeName, getName());
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return getName();
        }
    }

    static class PrimitiveArray extends CodeGenParameter {
        private final boolean readOnly;

        PrimitiveArray(Parameter parameter, boolean readOnly) {
            super(parameter);
            this.readOnly = readOnly;
        }

        @Override
        public String generateParamListDecl(CodeGenContext context) {
            return eval("${c}${t}* ${p}Elems, size_t ${p}Length",
                        kv("c", readOnly ? "const " : ""),
                        kv("t", getType().simpleTypeName()),
                        kv("p", parameter.name()));
        }

        @Override
        public String generateLocalVarDecl(CodeGenContext context) {
            return eval("jarray ${p}Array = NULL;\nvoid* ${p}ArrayAddr = NULL;",
                        kv("p", parameter.name()));
        }

        @Override
        public String generatePreCallCode(CodeGenContext context) {
            return eval("${p}Array = (*jenv)->NewBooleanArray(jenv, ${p}Length);\n" +
                                "${p}ArrayAddr = (*env)->GetPrimitiveArrayCritical(jenv, ${p}Array, 0);\n" +
                                "memcpy(${p}ArrayAddr, ${p}Elems, ${p}Length);",
                        kv("p", parameter.name()));
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return eval("${p}Array", kv("p", parameter.name()));
        }

        @Override
        public String generatePostCallCode(CodeGenContext context) {
            return eval("(*env)->ReleasePrimitiveArrayCritical(jenv, ${p}Array, ${p}ArrayAddr, 0);",
                        kv("p", parameter.name()));
        }
    }

    static class StringScalar extends CodeGenParameter {
        StringScalar(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(CodeGenContext context) {
            return String.format("const char* %s", getName());
        }

        @Override
        public String generateLocalVarDecl(CodeGenContext context) {
            return String.format("jstring %sString = NULL;", getName());
        }

        @Override
        public String generatePreCallCode(CodeGenContext context) {
            return String.format("%sString = (*jenv)->NewStringUTF(jenv, %s);",
                                 getName(), getName());
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return String.format("%sString", getName());
        }
    }

    static class StringArray extends CodeGenParameter {
        StringArray(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(CodeGenContext context) {
            return String.format("const char** %sElems, int %sLength",
                                 getName(), getName());
        }

        @Override
        public String generateLocalVarDecl(CodeGenContext context) {
            return String.format("jobjectArray %sArray = NULL;", getName());
        }

        @Override
        public String generatePreCallCode(CodeGenContext context) {
            return String.format("%sArray = beam_new_jstring_array(%sElems, %sLength);",
                                 getName(), getName(), getName());
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return String.format("%sArray", getName());
        }

    }

    static class ObjectScalar extends CodeGenParameter {
        ObjectScalar(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(CodeGenContext context) {
            String typeName = getType().typeName().replace('.', '_');
            return String.format("%s %s", typeName, getName());
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return getName();
        }
    }

}
