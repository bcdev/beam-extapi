package org.esa.beam.extapi.gen;

import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
abstract class CodeGenParameter {
    protected final Parameter parameter;

    protected CodeGenParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getName() {
        return parameter.name();
    }

    public Type getType() {
        return parameter.type();
    }

    public abstract String generateLocalVarDecl(CodeGenContext context);

    public abstract String generatePreCallCode(CodeGenContext context);

    public abstract String generatePostCallCode(CodeGenContext context);

    public abstract String generateCallArgExpr(CodeGenContext context);

    public abstract String generateTargetVarName(CodeGenContext context);

    public abstract String generateTargetVarType(CodeGenContext context);

    public abstract String generateCallVarName(CodeGenContext context);

    static class PrimitiveScalar extends CodeGenParameter {
        PrimitiveScalar(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateLocalVarDecl(CodeGenContext context) {
            return null;
        }

        @Override
        public String generatePreCallCode(CodeGenContext context) {
            return null;
        }

        @Override
        public String generatePostCallCode(CodeGenContext context) {
            return null;
        }

        @Override
        public String generateTargetVarName(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateCallVarName(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateTargetVarType(CodeGenContext context) {
            if (parameter.typeName().equals("long")) {
                return "dlong";
            }
            return parameter.typeName();
        }
    }

    static class StringScalar extends CodeGenParameter {
        StringScalar(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return generateCallVarName(context);
        }

        @Override
        public String generateLocalVarDecl(CodeGenContext context) {
            return String.format("jstring %s = NULL;", generateCallVarName(context));
        }

        @Override
        public String generatePreCallCode(CodeGenContext context) {
            return String.format("%s = (*jenv)->NewStringUTF(jenv, %s);",
                                 generateCallVarName(context), parameter.name());
        }

        @Override
        public String generatePostCallCode(CodeGenContext context) {
            return null;
        }

        @Override
        public String generateTargetVarName(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateCallVarName(CodeGenContext context) {
            return parameter.name() + "String";
        }

        @Override
        public String generateTargetVarType(CodeGenContext context) {
            return "const char*";
        }
    }


    static class ObjectScalar extends CodeGenParameter {
        ObjectScalar(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String generateCallArgExpr(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateLocalVarDecl(CodeGenContext context) {
            return null;
        }

        @Override
        public String generatePreCallCode(CodeGenContext context) {
            return null;
        }

        @Override
        public String generatePostCallCode(CodeGenContext context) {
            return null;
        }

        @Override
        public String generateCallVarName(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateTargetVarName(CodeGenContext context) {
            return parameter.name();
        }

        @Override
        public String generateTargetVarType(CodeGenContext context) {
            return getType().typeName().replace('.', '_');
        }
    }

}
