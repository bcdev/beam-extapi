package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.ParameterGenerator;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.TypeHelpers.getComponentCTypeName;

/**
 * @author Norman Fomferra
 */
public abstract class PyCParameterGenerator implements ParameterGenerator {
    protected final ApiParameter parameter;

    protected PyCParameterGenerator(ApiParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getName() {
        return parameter.getJavaName();
    }

    @Override
    public Type getType() {
        return parameter.getParameterDoc().type();
    }

    @Override
    public String generateLocalVarDecl(GeneratorContext context) {
        String typeName = getComponentCTypeName(getType());
        return String.format("%s %s;", typeName, getName());
    }

    @Override
    public String generateCallCode(GeneratorContext context) {
        return getName();
    }

    @Override
    public String generateParamListDecl(GeneratorContext context) {
        return null;
    }

    @Override
    public String generatePreCallCode(GeneratorContext context) {
        return null;
    }

    @Override
    public String generatePostCallCode(GeneratorContext context) {
        return null;
    }

    static class PrimitiveScalar extends PyCParameterGenerator {
        PrimitiveScalar(ApiParameter parameter) {
            super(parameter);
        }

    }

    static class ObjectScalar extends PyCParameterGenerator {
        ObjectScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return PyCFunctionGenerator.generateObjectTypeDecl(getName());
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("(%s) %s", CModuleGenerator.getComponentCClassName(getType()), getName());
        }
    }

    static class StringScalar extends PyCParameterGenerator {
        StringScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return String.format("const char* %s;", getName());
        }
    }

     static class PrimitiveArray extends PyCParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("${c}${t}* ${p}Elems;\n" +
                                "int ${p}Length;",
                        kv("c", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", getType().simpleTypeName()),
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            /*
            String typeName = getType().simpleTypeName();
            String typeNameUC = TypeHelpers.firstCharToUpperCase(typeName);
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
            */
            return null;
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Elems, ${p}Length",
                        kv("p", getName()));
        }

        @Override
        public String generatePostCallCode(GeneratorContext context) {
            /*
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
            */
            return null;
        }
    }


    static class ObjectArray extends PyCParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("${m}${t} ${p}Elems;\n" +
                                "int ${p}Length;",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", CModuleGenerator.getComponentCClassName(getType())),
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            /*
            return eval("${p}Array = beam_new_jobject_array(${p}Elems, ${p}Length, ${c});",
                        kv("p", getName()),
                        kv("c", CModuleGenerator.getComponentCClassVarName(getType())));
                        */
            return null;
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Elems, ${p}Length",
                        kv("p", getName()));
        }
    }


    static class StringArray extends PyCParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("${m}char** ${p}Elems;\n" +
                                "int ${p}Length;",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            /*
            return eval("${p}Array = beam_new_jstring_array(${p}Elems, ${p}Length);",
                        kv("p", getName()));
                        */
            return null;
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}Elems, ${p}Length",
                        kv("p", getName()));
        }
    }

}
