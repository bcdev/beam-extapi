package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.ParameterGenerator;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

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
                return "p";
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
        public String generateLocalVarDecl(GeneratorContext context) {
            return PyCFunctionGenerator.generateObjectTypeDecl(getName());
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
            return String.format("(%s) %s", CModuleGenerator.getComponentCClassName(getType()), getName());
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
        public String generateLocalVarDecl(GeneratorContext context) {
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
        public String generateLocalVarDecl(GeneratorContext context) {
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
        public String generatePreCallCode(GeneratorContext context) {
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
                                "${p}Obj = beam_getPrimitiveArrayBuffer${bm}(${p}Obj, &${p}Buf, \"${tf}\", ${le});\n" +
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
        public String generateCallCode(GeneratorContext context) {
            return eval("${p}, ${p}Length",
                        kv("p", getName()));
        }

        @Override
        public String generatePostCallCode(GeneratorContext context) {
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
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("${t} ${p};\n" +
                                "int ${p}Length;\n" +
                                "PyObject* ${p}Seq;",
                        kv("t", CModuleGenerator.getComponentCClassName(getType())),
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            return eval("${p} = beam_new_jobject_array_from_pyseq(\"${t}\", ${p}Seq, &${p}Length);",
                        kv("p", getName()),
                        kv("t", getComponentCTypeName(getType())));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
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


    static class StringArray extends PyCParameterGenerator {
        StringArray(ApiParameter parameter) {
            super(parameter);
        }

        @Override
        public String generateLocalVarDecl(GeneratorContext context) {
            return eval("char** ${p};\n" +
                                "int ${p}Length;\n" +
                                "PyObject* ${p}Seq;",
                        kv("p", getName()));
        }

        @Override
        public String generatePreCallCode(GeneratorContext context) {
            return eval("${p} = beam_new_string_array_from_pyseq(${p}Seq, &${p}Length);",
                        kv("p", getName()),
                        kv("c", CModuleGenerator.getComponentCClassVarName(getType())));
        }

        @Override
        public String generateCallCode(GeneratorContext context) {
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
