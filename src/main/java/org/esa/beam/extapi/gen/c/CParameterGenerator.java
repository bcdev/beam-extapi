package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.ParameterGenerator;

import static org.esa.beam.extapi.gen.JavadocHelpers.getComponentCTypeName;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.c.CModuleGenerator.RESULT_VAR_NAME;

/**
 * @author Norman Fomferra
 */
public abstract class CParameterGenerator implements ParameterGenerator {
    protected final ApiParameter parameter;

    protected CParameterGenerator(ApiParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getName() {
        return parameter.getJavaName();
    }

    @Override
    public Type getType() {
        return ApiInfo.unfoldType(parameter.getParameterDoc().type());
    }

    @Override
    public String generateJniArgDeclaration(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateTargetArgDeclaration(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateJniArgDeref(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
        return null;
    }

    private static String firstCharToUpperCase(String typeName) {
        typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        return typeName;
    }

    static class PrimitiveScalar extends CParameterGenerator {

        PrimitiveScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateFunctionParamDeclaration(GeneratorContext context) {
            String typeName = getComponentCTypeName(getType());
            return String.format("%s %s", typeName, getName());
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return getName();
        }

    }

    static class ObjectScalar extends CParameterGenerator {
        ObjectScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateFunctionParamDeclaration(GeneratorContext context) {
            String typeName = ModuleGenerator.getComponentCClassName(getType());
            return eval("${t} ${p}",
                        kv("t", typeName),
                        kv("p", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return getName();
        }
    }


    static class StringScalar extends CParameterGenerator {
        StringScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateFunctionParamDeclaration(GeneratorContext context) {
            return eval("const char* ${p}",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jstring ${p}String = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}String",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${p}String = (*jenv)->NewStringUTF(jenv, ${p});",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${p}String);",
                        kv("p", getName()));
        }
    }

    static class PrimitiveArray extends CParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateFunctionParamDeclaration(GeneratorContext context) {
            return eval("${c}${t}* ${p}Elems, int ${p}Length",
                        kv("c", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", getComponentCTypeName(getType())),
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${p}Array = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            String typeName = getType().simpleTypeName();
            String typeNameUC = firstCharToUpperCase(typeName);
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                return eval("${p}Array = (*jenv)->New${tuc}Array(jenv, ${p}Length);\n" +
                                    "beam_copyToJArray(${p}Array, ${p}Elems, ${p}Length, sizeof (${t}));",
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
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}Array", kv("p", getName()));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                return null;
            } else if (parameter.getModifier() == ApiParameter.Modifier.OUT) {
                return eval("beam_copyFromJArray(${p}Array, ${p}Elems, ${p}Length, sizeof (${t}));" +
                                    "",
                            kv("p", getName()),
                            kv("t", getType().simpleTypeName()));
            } else if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                String typeName = getType().simpleTypeName();
                return eval("" +
                                    "if (${p}Elems != NULL && (*jenv)->IsSameObject(jenv, ${p}Array, ${r}Array)) {\n" +
                                    "    beam_copyFromJArray(${r}Array, ${p}Elems, ${p}Length, sizeof (${t}));\n" +
                                    "    ${r} = ${p}Elems;\n" +
                                    "    if (resultArrayLength != NULL) {\n" +
                                    "        *resultArrayLength = ${p}Length;\n" +
                                    "    }\n" +
                                    "} else {\n" +
                                    "    ${r} = beam_newC${tuc}Array(${r}Array, resultArrayLength);\n" +
                                    "}",
                            kv("r", RESULT_VAR_NAME),
                            kv("p", getName()),
                            kv("t", typeName),
                            kv("tuc", firstCharToUpperCase(typeName)));
            } else {
                throw new IllegalStateException("Unknown modifier: " + parameter.getModifier());
            }
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${p}Array);",
                        kv("p", getName()));
        }

    }


    static class ObjectArray extends CParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateFunctionParamDeclaration(GeneratorContext context) {
            return eval("${m}${t} ${p}Elems, int ${p}Length",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("t", ModuleGenerator.getComponentCClassName(getType())),
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${p}Array = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${p}Array = beam_newJObjectArray(${p}Elems, ${p}Length, ${c});",
                        kv("p", getName()),
                        kv("c", ModuleGenerator.getComponentCClassVarName(getType())));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}Array",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${p}Array);",
                        kv("p", getName()));
        }
    }


    static class StringArray extends CParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateFunctionParamDeclaration(GeneratorContext context) {
            return eval("${m}char** ${p}Elems, int ${p}Length",
                        kv("m", parameter.getModifier() == ApiParameter.Modifier.IN ? "const " : ""),
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jobjectArray ${p}Array = NULL;",
                        kv("p", getName()));
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${p}Array = beam_newJStringArray(${p}Elems, ${p}Length);",
                        kv("p", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}Array",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${p}Array);",
                        kv("p", getName()));
        }
    }

}
