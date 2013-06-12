package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.ParameterGenerator;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.esa.beam.extapi.gen.JavadocHelpers.getComponentCTypeName;

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
    public String generateJniArgDeclaration(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateTargetArgDeclaration(GeneratorContext context) {
        String typeName = getComponentCTypeName(getType());
        return String.format("%s %s;", typeName, getName());
    }

    @Override
    public String generateJniCallArgs(GeneratorContext context) {
        // todo - Python-C code shall directly call JNI, but we still call the C-API here
        return getName();
    }

    @Override
    public String generateJniArgDeref(GeneratorContext context) {
        // todo - Python-C code shall directly call JNI, but we still call the C-API here
        return null;
    }

    @Override
    public String generateFunctionParamDeclaration(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
        return null;
    }

    @Override
    public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
        return null;
    }

    public abstract String generateParseFormat(GeneratorContext context);

    public abstract String generateParseArgs(GeneratorContext context);

    static class PrimitiveScalar extends PyCParameterGenerator {
        PrimitiveScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateParseFormat(GeneratorContext context) {
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
        public String generateParseArgs(GeneratorContext context) {
            return "&" + getName();
        }
    }

    static class ObjectScalar extends PyCParameterGenerator {
        ObjectScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return PyCFunctionGenerator.generateObjectTypeDecl(getName());
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return String.format("(%s) %s", ModuleGenerator.getComponentCClassName(getType()), getName());
        }

        @Override
        public String generateParseFormat(GeneratorContext context) {
            return "(sK)";
        }

        @Override
        public String generateParseArgs(GeneratorContext context) {
            return String.format("&%sType, &%s", getName(), getName());
        }
    }

    static class StringScalar extends PyCParameterGenerator {
        StringScalar(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return String.format("const char* %s;", getName());
        }

        @Override
        public String generateParseFormat(GeneratorContext context) {
            return "s";
        }

        @Override
        public String generateParseArgs(GeneratorContext context) {
            return "&" + getName();
        }
    }

    static class PrimitiveArray extends PyCParameterGenerator {

        PrimitiveArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("${t}* ${p};\n" +
                                "int ${p}Length;\n" +
                                "PyObject* ${p}Obj;\n" +
                                "Py_buffer ${p}Buf;\n",
                        kv("t", getComponentCTypeName(getType())),
                        kv("p", getName()));
/* Sequence solution:
            return eval("${t}* ${p};\n" +
                                "int ${p}Length;\n" +
                                "PyObject* ${p}Seq;",
                        kv("t", getComponentCTypeName(getType())),
                        kv("p", getName()));
*/
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            String typeName = getComponentCTypeName(getType());
            String format = PyCFunctionGenerator.CARRAY_TYPE_CODES.get(typeName);
            if (format == null) {
                throw new IllegalStateException("format == null for typeName == " + typeName);
            }
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
            return eval("" +
                                "${p}Obj = beampy_getPrimitiveArrayBuffer${bm}(${p}Obj, &${p}Buf, \"${tf}\", ${le});\n" +
                                "if (${p}Obj == NULL) {\n" +
                                "    return NULL;\n" +
                                "}\n" +
                                "${p} = (${t}*) ${p}Buf.buf;\n" +
                                "${p}Length = ${p}Buf.len / ${p}Buf.itemsize;",
                        kv("p", getName()),
                        kv("t", typeName),
                        kv("le", lengthExpr),
                        kv("bm", bufferMode),
                        kv("tf", format));

/* Sequence solution:
            return eval("${p} = beam_new_${t}_array_from_pyseq(${p}Seq, &${p}Length);",
                        kv("p", getName()),
                        kv("t", getComponentCTypeName(getType())));
*/
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}, ${p}Length",
                        kv("p", getName()));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return eval("PyBuffer_Release(&${p}Buf);",
                        kv("p", getName()));
        }

        @Override
        public String generateParseArgs(GeneratorContext context) {
            return eval("&${p}Obj", kv("p", getName()));
        }

        @Override
        public String generateParseFormat(GeneratorContext context) {
            return "O";
        }
    }


    static class ObjectArray extends PyCParameterGenerator {

        ObjectArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("${t} ${p};\n" +
                                "int ${p}Length;\n" +
                                "PyObject* ${p}Seq;",
                        kv("t", ModuleGenerator.getComponentCClassName(getType())),
                        kv("p", getName()));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}, ${p}Length",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${p} = beampy_newCObjectArrayFromPySeq(\"${t}\", ${p}Seq, &${p}Length);",
                        kv("p", getName()),
                        kv("t", getComponentCTypeName(getType())));
        }

        @Override
        public String generateParseArgs(GeneratorContext context) {
            return eval("&${p}Seq", kv("p", getName()));
        }

        @Override
        public String generateParseFormat(GeneratorContext context) {
            return "O";
        }
    }


    static class StringArray extends PyCParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateTargetArgDeclaration(GeneratorContext context) {
            return eval("char** ${p};\n" +
                                "int ${p}Length;\n" +
                                "PyObject* ${p}Seq;",
                        kv("p", getName()));
        }

        @Override
        public String generateJniArgFromTransformedTargetArgAssignment(GeneratorContext context) {
            return eval("${p} = beampy_newCStringArrayFromPySeq(${p}Seq, &${p}Length);",
                        kv("p", getName()),
                        kv("c", ModuleGenerator.getComponentCClassVarName(getType())));
        }

        @Override
        public String generateJniCallArgs(GeneratorContext context) {
            return eval("${p}, ${p}Length",
                        kv("p", getName()));
        }

        @Override
        public String generateParseArgs(GeneratorContext context) {
            return eval("&${p}Seq", kv("p", getName()));
        }

        @Override
        public String generateParseFormat(GeneratorContext context) {
            return "O";
        }
    }
}
