package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiParameter;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.TypeHelpers.getComponentCTypeName;

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

    private static String firstCharToUpperCase(String typeName) {
        typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        return typeName;
    }

    static class ObjectScalar extends ParameterGenerator {
        ObjectScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            String typeName = CModuleGenerator.getComponentCClassName(getType());
            return String.format("%s %s", typeName, getName());
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return getName();
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

     static class PrimitiveArray extends ParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return eval("${c}${t}* ${p}Elems, int ${p}Length",
                        kv("c", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", getType().simpleTypeName()),
                        kv("p", getName()));
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("jarray ${p}Array = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            String typeName = getType().simpleTypeName();
            String typeNameUC = firstCharToUpperCase(typeName);
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                return eval("${p}Array = (*jenv)->New${tuc}Array(jenv, ${p}Length);\n" +
                                    "beam_copy_to_jarray(${p}Array, ${p}Elems, ${p}Length, sizeof (${t}));",
                            kv("t", typeName),
                            kv("tuc", typeNameUC),
                            kv("p", getName()));
            } else {
                return eval("${p}Array = (*jenv)->New${tuc}Array(jenv, ${p}Length);",
                            kv("tuc", typeNameUC),
                            kv("p", getName()));
            }
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Array", kv("p", getName()));
        }

        @Override
        public String generatePostCallCode(GeneratorContext context) {
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                return null;
            } else if (parameter.getModifier() == ApiParameter.Modifier.OUT) {
                return eval("beam_copy_from_jarray(${p}Array, ${p}Elems, ${p}Length, sizeof (${t}));" +
                                    "",
                            kv("p", getName()),
                            kv("t", getType().simpleTypeName()));
            } else {
                return eval("" +
                                    "if (${p}Elems != NULL && (*jenv)->IsSameObject(jenv, ${p}Array, ${r}Array)) {\n" +
                                    "    beam_copy_from_jarray(_resultArray, ${p}Elems, ${p}Length, sizeof (${t}));\n" +
                                    "    ${r} = ${p}Elems;\n" +
                                    "} else {\n" +
                                    "    ${r} = beam_alloc_${t}_array(${r}Array, resultArrayLength);\n" +
                                    "}",
                            kv("r", CModuleGenerator.RESULT_VAR_NAME),
                            kv("p", getName()),
                            kv("t", getType().simpleTypeName()));
            }
        }
    }


    static class ObjectArray extends ParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return eval("${m}${t} ${p}Elems, int ${p}Length",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", CModuleGenerator.getComponentCClassName(getType())),
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
                        kv("c", CModuleGenerator.getComponentCClassVarName(getType())));
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


    static class StringArray extends ParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParamListDecl(GeneratorContext context) {
            return eval("${m}char** ${p}Elems, int ${p}Length",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
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

}
