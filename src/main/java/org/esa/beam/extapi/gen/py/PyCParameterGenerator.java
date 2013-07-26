package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.ParameterGenerator;

import static org.esa.beam.extapi.gen.JavadocHelpers.firstCharUp;
import static org.esa.beam.extapi.gen.ModuleGenerator.getComponentCClassName;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

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
        return ApiInfo.unfoldType(parameter.getParameterDoc().type());
    }

    @Override
    public String generateFunctionParamDeclaration(GeneratorContext context) {
        // We have a fixed function signature: PyObject* <function>(PyObject* self, PyObject* args)
        return null;
    }

    /**
     * @return The format string for the ParseTuple() Python function.
     */
    public abstract String getParseFormat(GeneratorContext context);

    /**
     * @return The argument(s) for the ParseTuple() Python function.
     */
    public abstract String getParseArgs(GeneratorContext context);

    static class PrimitiveScalar extends PyCParameterGenerator {
        PrimitiveScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            // Not needed, because primitive parameter type should automatically match JNI type
            return null;
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            // Not needed, because primitive parameter type should automatically match JNI type
            return eval("j${type} ${arg} = (j${type}) 0;",
                        kv("type", getType().simpleTypeName()),
                        kv("arg", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            String s = getType().typeName();
            if (s.equals("boolean")) {
                return "b";
            } else if (s.equals("char")) {
                return "C";
            } else if (s.equals("byte")) {
                return "b";
            } else if (s.equals("short")) {
                return "h";
            } else if (s.equals("int")) {
                return "i";
            } else if (s.equals("long")) {
                return "L";
            } else if (s.equals("float")) {
                return "f";
            } else if (s.equals("double")) {
                return "d";
            } else {
                throw new IllegalArgumentException("can't deal with type '" + s + "'");
            }
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${arg}", kv("arg", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            // Not needed, because primitive parameter type should automatically match JNI type
            return null;
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return getName();
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            // Not needed, because parameter is primitive
            return null;
        }
    }

    static class ObjectScalar extends PyCParameterGenerator {
        ObjectScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("PyObject* ${arg}PyObj = NULL;",
                        kv("arg", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jobject ${arg}JObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${arg}PyObj", kv("arg", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("{\n"
                                + "    jboolean ok = 1;\n"
                                + "    ${arg}JObj = BPy_ToJObjectT(${arg}PyObj, ${classVarName}, &ok);\n"
                                + "    if (!ok) {\n"
                                + "        return NULL;\n"
                                + "    }\n"
                                + "}",
                        kv("arg", getName()),
                        kv("typeName", getComponentCClassName(getType())),
                        kv("classVarName", context.getComponentCClassVarName(getType())));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${arg}JObj", kv("arg", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            // not needed, because we parameter is a JNI global reference
            return null;
        }
    }

    static class StringScalar extends PyCParameterGenerator {
        StringScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("const char* ${arg} = NULL;", kv("arg", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jstring ${arg}JObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "s";
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${arg}", kv("arg", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${arg}JObj =(*jenv)->NewStringUTF(jenv, ${arg});",
                        kv("arg", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${arg}JObj", kv("arg", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${arg}JObj);", kv("arg", getName()));
        }
    }

    static class PrimitiveArray extends PyCParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("" +
                                "j${type}* ${arg}Data = NULL;\n" +
                                "int ${arg}Length = 0;\n" +
                                "PyObject* ${arg}PyObj = NULL;\n" +
                                "Py_buffer ${arg}Buf;",
                        kv("type", getType().simpleTypeName()),
                        kv("arg", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${arg}JObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${arg}PyObj", kv("arg", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            String typeName = getType().simpleTypeName();
            String format = PyCModuleGenerator.getCArrayFormat(typeName);
            String bufferMode;
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                bufferMode = "ReadOnly";
            } else {
                bufferMode = "Writable";
            }
            String lengthExpr = parameter.getLengthExpr();
            if (lengthExpr == null) {
                lengthExpr = "-1";
            }
            // todo - we should actually check if the Python parameter supports the buffer protocol using PyObject_CheckBuffer().
            //        If it is not a buffer, but a sequence, we could try converting the sequence into a primitive Java array.
            //        If bufferMode="Writable", we could check if the Python parameter is a list of appropriate size an set its items.
            final String primType = firstCharUp(typeName);
            return eval("" +
                                "${arg}PyObj = BPy_ToPrimitiveArrayBuffer${bufferMode}(${arg}PyObj, &${arg}Buf, \"${format}\", ${lengthExpr});\n" +
                                "if (${arg}PyObj == NULL) {\n" +
                                "    return NULL;\n" +  // todo - goto error label and release all buffers and JNI vars
                                "}\n" +
                                "${arg}Data = (j${type}*) ${arg}Buf.buf;\n" +
                                "${arg}Length = ${arg}Buf.len / ${arg}Buf.itemsize;\n" +
                                "${arg}JObj = BPy_NewJ${primType}ArrayFromBuffer(${arg}Data, ${arg}Length);\n" +
                                "if (${arg}JObj == NULL) {\n" +
                                "    return NULL;\n" +  // todo - goto error label and release all buffers and JNI vars
                                "}",
                        kv("arg", getName()),
                        kv("type", typeName),
                        kv("primType", primType),
                        kv("lengthExpr", lengthExpr),
                        kv("bufferMode", bufferMode),
                        kv("format", format));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${arg}JObj", kv("arg", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                return null;
            }

            String primType = firstCharUp(getType().simpleTypeName());
            if (parameter.getModifier() == ApiParameter.Modifier.OUT) {
                return eval("BPy_CopyJ${primType}ArrayToBuffer((jarray) ${arg}JObj, ${arg}Data, ${arg}Length, ${arg}PyObj);",
                            kv("primType", primType),
                            kv("arg", getName()));
            } else if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                return eval("" +
                                    "if (${arg}Data != NULL && (*jenv)->IsSameObject(jenv, ${res}JObj, ${arg}JObj)) {\n" +
                                    "    ${res}PyObj = BPy_CopyJ${primType}ArrayToBuffer((jarray) ${arg}JObj, ${arg}Data, ${arg}Length, ${arg}PyObj);\n" +
                                    "} else {\n" +
                                    "    ${res}PyObj = BPy_FromJ${primType}Array((jarray) ${arg}JObj);\n" +
                                    "}",
                            kv("primType", primType),
                            kv("arg", getName()),
                            kv("res", PyCModuleGenerator.RESULT_VAR_NAME));
            } else {
                throw new IllegalStateException("Unknown modifier: " + parameter.getModifier());
            }
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval(""
                                + "PyBuffer_Release(&${arg}Buf);\n"
                                + "(*jenv)->DeleteLocalRef(jenv, ${arg}JObj);",
                        kv("arg", getName()));
        }
    }


    static class ObjectArray extends PyCParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("PyObject* ${arg}PyObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${arg}JObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${arg}PyObj", kv("arg", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("{\n"
                                + "    jboolean ok = 1;\n"
                                + "    ${arg}JObj = BPy_ToJObjectArrayT(${arg}PyObj, ${classVarName}, &ok);\n"
                                + "    if (!ok) {\n"
                                + "        return NULL;\n" // todo - goto error label and deref all JNI vars
                                + "    }\n"
                                + "}",
                        kv("arg", getName()),
                        kv("typeName", getComponentCClassName(getType())),
                        kv("classVarName", context.getComponentCClassVarName(getType())));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${arg}JObj", kv("arg", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${arg}JObj);", kv("arg", getName()));
        }
    }


    static class StringArray extends PyCParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("PyObject* ${arg}PyObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${arg}JObj = NULL;", kv("arg", getName()));
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${arg}PyObj", kv("arg", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("{\n"
                                + "    jboolean ok = 1;\n"
                                + "    ${arg}JObj = BPy_ToJStringArray(${arg}PyObj, &ok);\n"
                                + "    if (!ok) {\n"
                                + "        return NULL;\n" // todo - goto error label and deref all JNI vars
                                + "    }\n"
                                + "}",
                        kv("arg", getName()),
                        kv("type", getType().simpleTypeName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${arg}JObj", kv("arg", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${arg}JObj);", kv("arg", getName()));
        }

    }

}
