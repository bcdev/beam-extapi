package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.ParameterGenerator;

import static org.esa.beam.extapi.gen.JavadocHelpers.firstCharToUpperCase;
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
            return eval("j${type} ${par} = (j${type}) 0;\n",
                        kv("type", getType().simpleTypeName()),
                        kv("par", getName()));
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
            return eval("&${par}", kv("par", getName()));
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
            return PyCFunctionGenerator.generateJObjectTargetArgDecl(getName());
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jobject ${par}JObj = NULL;", kv("par", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "(sK)";
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${par}Type, &${par}", kv("par", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${par}JObj = (jobject) ${par};", kv("par", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${par}JObj", kv("par", getName()));
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
            return eval("const char* ${par} = NULL;", kv("par", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jstring ${par}JObj = NULL;", kv("par", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "s";
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${par}", kv("par", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${par}JObj =(*jenv)->NewStringUTF(jenv, ${par});", kv("par", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${par}JObj", kv("par", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${par}JObj);", kv("par", getName()));
        }
    }

    static class PrimitiveArray extends PyCParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("" +
                                "j${type}* ${par}Data = NULL;\n" +
                                "int ${par}Length = 0;\n" +
                                "PyObject* ${par}PyObj = NULL;\n" +
                                "Py_buffer ${par}Buf;\n",
                        kv("type", getType().simpleTypeName()),
                        kv("par", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${par}JObj = NULL;", kv("par", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${par}PyObj", kv("par", getName()));
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
            // todo - we should actually check if the Python parameter supports the buffer protocol.
            //        If it is not a buffer, but a sequence, we could try converting the sequence into a primitive Java array.
            //        If bufferMode="Writable", we could check if the Python parameter is a list of appropriate size an set its items.
            return eval("" +
                                "${par}PyObj = beampy_getPrimitiveArrayBuffer${bufferMode}(${par}PyObj, &${par}Buf, \"${format}\", ${lengthExpr});\n" +
                                "if (${par}PyObj == NULL) {\n" +
                                "    return NULL;\n" +  // todo - goto error label and release all buffers and JNI vars
                                "}\n" +
                                "${par}Data = (j${type}*) ${par}Buf.buf;\n" +
                                "${par}Length = ${par}Buf.len / ${par}Buf.itemsize;\n" +
                                "${par}JObj = beampy_newJ${typeUC}ArrayFromBuffer(${par}Data, ${par}Length);\n" +
                                "if (${par}JObj == NULL) {\n" +
                                "    return NULL;\n" +  // todo - goto error label and release all buffers and JNI vars
                                "}",
                        kv("par", getName()),
                        kv("type", typeName),
                        kv("typeUC", firstCharToUpperCase(typeName)),
                        kv("lengthExpr", lengthExpr),
                        kv("bufferMode", bufferMode),
                        kv("format", format));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${par}JObj", kv("par", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            if (parameter.getModifier() == ApiParameter.Modifier.IN) {
                return null;
            } else if (parameter.getModifier() == ApiParameter.Modifier.OUT) {
                String typeName = getType().simpleTypeName();
                return eval("beampy_copyJ${typeUC}ArrayToBuffer((jarray) ${par}JObj, ${par}Data, ${par}Length, ${par}PyObj);",
                            kv("typeUC", firstCharToUpperCase(typeName)),
                            kv("par", getName()));
            } else if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                String typeName = getType().simpleTypeName();
                return eval("" +
                                    "if (${par}Data != NULL && (*jenv)->IsSameObject(jenv, ${res}JObj, ${par}JObj)) {\n" +
                                    "    ${res}PyObj = beampy_copyJ${typeUC}ArrayToBuffer((jarray) ${par}JObj, ${par}Data, ${par}Length, ${par}PyObj);\n" +
                                    "} else {\n" +
                                    "    ${res}PyObj = beampy_newPyObjectFromJ${typeUC}Array((jarray) ${par}JObj);\n" +
                                    "}",
                            kv("typeUC", firstCharToUpperCase(typeName)),
                            kv("par", getName()),
                            kv("res", PyCModuleGenerator.RESULT_VAR_NAME));
            } else {
                throw new IllegalStateException("Unknown modifier: " + parameter.getModifier());
            }
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval(""
                                + "PyBuffer_Release(&${par}Buf);\n"
                                + "(*jenv)->DeleteLocalRef(jenv, ${par}JObj);",
                        kv("par", getName()));
        }
    }


    static class ObjectArray extends PyCParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("PyObject* ${par}PyObj = NULL;", kv("par", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${par}JObj = NULL;", kv("par", getName()));
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${par}PyObj", kv("par", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval(""
                                + "${par}JObj = beampy_newJObjectArrayFromPySeq(${par}PyObj, \"${type}\");\n"
                                + "if (${par}JObj == NULL) {\n"
                                + "    return NULL;\n" // todo - goto error label and deref all JNI vars
                                + "}",
                        kv("par", getName()),
                        kv("type", getType().simpleTypeName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${par}JObj", kv("par", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${par}JObj);", kv("par", getName()));
        }
    }


    static class StringArray extends PyCParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("PyObject* ${par}PyObj = NULL;", kv("par", getName()));
        }

        @Override
        public String generateJniArgDeclaration(GeneratorContext context) {
            return eval("jarray ${par}JObj = NULL;", kv("par", getName()));
        }

        @Override
        public String getParseArgs(GeneratorContext context) {
            return eval("&${par}PyObj", kv("par", getName()));
        }

        @Override
        public String getParseFormat(GeneratorContext context) {
            return "O";
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval(""
                                + "${par}JObj = beampy_newJStringArrayFromPySeq(${par}PyObj);\n"
                                + "if (${par}JObj == NULL) {\n"
                                + "    return NULL;\n" // todo - goto error label and deref all JNI vars
                                + "}",
                        kv("par", getName()),
                        kv("type", getType().simpleTypeName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${par}JObj", kv("par", getName()));
        }

        @Override
        public String generateTargetArgFromTransformedJniArgAssignment(GeneratorContext context) {
            return null;
        }

        @Override
        public String generateJniArgDeref(GeneratorContext context) {
            return eval("(*jenv)->DeleteLocalRef(jenv, ${par}JObj);", kv("par", getName()));
        }

    }
}
