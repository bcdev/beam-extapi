package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiParameter;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.ModuleGenerator.getComponentCTypeName;

/**
 * @author Norman Fomferra
 */
public abstract class ParameterGenerator implements CodeGenerator {
    protected final ApiParameter parameter;

    protected ParameterGenerator(ApiParameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter.getJavaName();
    }

    public Type getType() {
        return parameter.getParameterDoc().type();
    }

    @Override
    public abstract String generateParamListDecl(GeneratorContext context);

    @Override
    public String generateLocalVarDecl(GeneratorContext context) {
        return null;
    }

    @Override
    public String generatePreCallCode(GeneratorContext context) {
        return null;
    }

    public abstract String generateCallCode(GeneratorContext context);

    @Override
    public String generatePostCallCode(GeneratorContext context) {
        return null;
    }

    static class PrimitiveScalar extends ParameterGenerator {
        PrimitiveScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            String typeName = getComponentCTypeName(getType());
            return String.format("%s %s", typeName, getName());
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return getName();
        }
    }

    static class PrimitiveArray extends ParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return eval("${c}${t}* ${p}Elems, int ${p}Length",
                        kv("c", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", getType().simpleTypeName()),
                        kv("p", parameter.getJavaName()));
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("jarray ${p}Array = NULL;\nvoid* ${p}ArrayAddr = NULL;",
                        kv("p", parameter.getJavaName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            String typeName = getType().simpleTypeName();
            typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);

            return eval("${p}Array = (*jenv)->New${t}Array(jenv, ${p}Length);\n" +
                                "${p}ArrayAddr = (*jenv)->GetPrimitiveArrayCritical(jenv, ${p}Array, 0);\n" +
                                "memcpy(${p}ArrayAddr, ${p}Elems, ${p}Length);",
                        kv("t", typeName),
                        kv("p", parameter.getJavaName()));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Array", kv("p", parameter.getJavaName()));
        }

        @Override
        public String generatePostCallCode(GeneratorContext context) {
            return eval("(*jenv)->ReleasePrimitiveArrayCritical(jenv, ${p}Array, ${p}ArrayAddr, 0);",
                        kv("p", parameter.getJavaName()));
        }
    }

    static class StringScalar extends ParameterGenerator {
        StringScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return String.format("const char* %s", getName());
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return String.format("jstring %sString = NULL;", getName());
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            return String.format("%sString = (*jenv)->NewStringUTF(jenv, %s);",
                                 getName(), getName());
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("%sString", getName());
        }
    }

    static class StringArray extends ParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return eval("const char** ${p}Elems, int ${p}Length",
                        kv("p", getName()));
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("jobjectArray ${p}Array = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            return eval("${p}Array = beam_new_jstring_array(${p}Elems, ${p}Length);",
                        kv("p", getName()));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Array",
                        kv("p", getName()));
        }

    }

    static class ObjectScalar extends ParameterGenerator {
        ObjectScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            String typeName = ModuleGenerator.getComponentCClassName(getType());
            return String.format("%s %s", typeName, getName());
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return getName();
        }
    }

    static class ObjectArray extends ParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return eval("${m}${t}* ${p}Elems, int ${p}Length",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", ModuleGenerator.getComponentCClassName(getType())),
                        kv("p", getName()));
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("jarray ${p}Array = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            return eval("${p}Array = beam_new_jobject_array(${p}Elems, ${p}Length, ${c});",
                        kv("p", getName()),
                        kv("c", ModuleGenerator.getComponentCClassVarName(getType())));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Array",
                        kv("p", getName()));
        }

        @Override
        public String generatePostCallCode(GeneratorContext context) {
            return null;
        }
    }


}
