package org.esa.beam.extapi.gen.py;

import org.esa.beam.extapi.gen.AbstractFunctionGenerator;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.GeneratorContext;
import org.esa.beam.extapi.gen.JavadocHelpers;
import org.esa.beam.extapi.gen.ModuleGenerator;

import java.util.HashMap;

import static org.esa.beam.extapi.gen.JavadocHelpers.firstCharToUpperCase;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public abstract class PyCFunctionGenerator extends AbstractFunctionGenerator {

    final static HashMap<String, String> CARRAY_FORMATS = new HashMap<String, String>();

    static {
        CARRAY_FORMATS.put(Byte.TYPE.getName(), "b");
        CARRAY_FORMATS.put(Boolean.TYPE.getName(), "b");
        CARRAY_FORMATS.put(Character.TYPE.getName(), "h");
        CARRAY_FORMATS.put(Short.TYPE.getName(), "h");
        CARRAY_FORMATS.put(Integer.TYPE.getName(), "i");
        CARRAY_FORMATS.put(Long.TYPE.getName(), "l");
        CARRAY_FORMATS.put(Long.TYPE.getName(), "l");
        CARRAY_FORMATS.put(Float.TYPE.getName(), "f");
        CARRAY_FORMATS.put(Double.TYPE.getName(), "d");
    }

    protected PyCFunctionGenerator(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
        super(apiMethod, parameterGenerators);
        templateEval.add(kv("res", ModuleGenerator.RESULT_VAR_NAME),
                         kv("this", ModuleGenerator.THIS_VAR_NAME));
    }

    @Override
    public final PyCParameterGenerator[] getParameterGenerators() {
        return (PyCParameterGenerator[]) super.getParameterGenerators();
    }

    @Override
    public String generateDocText(GeneratorContext context) {
        // TODO: generate Python-style documentation
        final String text = JavadocHelpers.encodeCCodeString(apiMethod.getMemberDoc().getRawCommentText());
        if (isInstanceMethod()) {
            final String thisParamText = String.format("@param this The %s object.", apiMethod.getEnclosingClass().getType().simpleTypeName());
            final int i = text.indexOf("@param");
            if (i > 0) {
                return String.format("%s\\n%s\\n%s", text.substring(0, i), thisParamText, text.substring(i));
            } else {
                return String.format("%s\\n%s", text, thisParamText);
            }
        }
        return text;
    }

    @Override
    public final String generateFunctionName(GeneratorContext context) {
        return context.getFunctionNameFor(getApiMethod());
    }

    @Override
    public final String generateFunctionSignature(GeneratorContext context) {
        // We have a fixed function signature: PyObject* <function>(PyObject* self, PyObject* args)
        return String.format("PyObject* %s(PyObject* self, PyObject* args)",
                             context.getFunctionNameFor(getApiMethod()));
    }

    @Override
    public final String generateExtraFunctionParamDeclaration(GeneratorContext context) {
        // We have a fixed function signature: PyObject* <function>(PyObject* self, PyObject* args)
        return null;
    }


    @Override
    public final String generateLocalVarDeclarations(GeneratorContext context) {
        final String methodVarDecl = super.generateLocalVarDeclarations(context);
        if (isInstanceMethod()) {
            return format("" +
                                  "${methodVarDecl}\n" +
                                  "${thisVarDecl}\n" +
                                  "jobject ${this}JObj = NULL;",
                          kv("methodVarDecl", methodVarDecl),
                          kv("thisVarDecl", generateJObjectTargetArgDecl(ModuleGenerator.THIS_VAR_NAME)));
        } else {
            return methodVarDecl;
        }
    }

    static String generateJObjectTargetArgDecl(String varName) {
        return eval("" +
                            "const char* ${var}Type = NULL;\n" +
                            "unsigned PY_LONG_LONG ${var} = 0;",
                    kv("var", varName));
    }

    @Override
    public String generateEnterCode(GeneratorContext context) {
        final ApiMethod apiMethod = getApiMethod();

        return eval(""
                            + "if (!beampy_initJMethod(&${method}, ${classVar}, \"${class}\", \"${methodName}\", \"${methodSig}\", ${static})) {\n"
                            + "    return NULL;\n"
                            + "}\n",
                    kv("method", ModuleGenerator.METHOD_VAR_NAME),
                    kv("static", apiMethod.getMemberDoc().isStatic() ? "1" : "0"),
                    kv("classVar", ModuleGenerator.getComponentCClassVarName(apiMethod.getEnclosingClass().getType())),
                    kv("class", apiMethod.getEnclosingClass().getType().qualifiedTypeName()),
                    kv("methodName", apiMethod.getJavaName()),
                    kv("methodSig", apiMethod.getJavaSignature()));
    }


    @Override
    public final String generateTargetArgsFromParsedParamsAssignment(GeneratorContext context) {
        StringBuilder formatString = new StringBuilder();
        StringBuilder argumentsStrings = new StringBuilder();
        if (isInstanceMethod()) {
            formatString.append("(sK)");
            argumentsStrings.append(format("&${this}Type, &${this}"));
        }
        for (PyCParameterGenerator pyCParameterGenerator : getParameterGenerators()) {
            String format = pyCParameterGenerator.getParseFormat(context);
            if (format != null) {
                formatString.append(format);
            }
            String args = pyCParameterGenerator.getParseArgs(context);
            if (args != null) {
                if (argumentsStrings.length() > 0) {
                    argumentsStrings.append(", ");
                }
                argumentsStrings.append(args);
            }
        }
        if (argumentsStrings.length() > 0) {
            return String.format("if (!PyArg_ParseTuple(args, \"%s:%s\", %s)) {\n" +
                                         "    return NULL;\n" +
                                         "}",
                                 formatString,
                                 context.getFunctionNameFor(apiMethod),
                                 argumentsStrings);
        }
        return null;
    }

    static String getCArrayFormat(com.sun.javadoc.Type type) {
        String typeName = type.simpleTypeName();
        String format = PyCFunctionGenerator.CARRAY_FORMATS.get(typeName);
        if (format == null) {
            throw new IllegalArgumentException("Illegal type: " + type.qualifiedTypeName());
        }
        return format;
    }

    final ApiParameter getReturnParameter(GeneratorContext context) {
        for (ApiParameter parameter : context.getParametersFor(getApiMethod())) {
            if (parameter.getModifier() == ApiParameter.Modifier.RETURN) {
                return parameter;
            }
        }
        return null;
    }

    protected boolean hasReturnParameter(GeneratorContext context) {
        return getReturnParameter(context) != null;
    }

    /*
    protected String generateCApiCall(GeneratorContext context) {

        final String functionName = getCApiFunctionName(context);

        final StringBuilder argumentList = new StringBuilder();
        if (isInstanceMethod()) {
            argumentList.append(String.format("(%s) %s",
                                              getComponentCTypeName(getEnclosingClass().getType()),
                                              PyCModuleGenerator.THIS_VAR_NAME));
        }
        for (PyCParameterGenerator parameterGenerator : getParameterGenerators()) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(parameterGenerator.generateJniCallArgs(context));
        }

        String extraArgs = generateExtraArgs();
        if (extraArgs != null) {
            if (argumentList.length() > 0) {
                argumentList.append(", ");
            }
            argumentList.append(extraArgs);
        }

        return String.format("%s(%s)", functionName,
                             argumentList);
    }
    protected String generateExtraArgs() {
        return null;
    }

    private String getCApiFunctionName(GeneratorContext context) {
        final PyCModuleGenerator pyCModuleGenerator = (PyCModuleGenerator) context;
        final CModuleGenerator cModuleGenerator = pyCModuleGenerator.getCModuleGenerator();
        return cModuleGenerator.getFunctionNameFor(apiMethod);
    }
     */

    static class VoidMethod extends PyCFunctionGenerator {

        VoidMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            // void
            return null;
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            // void
            return null;
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${call};",
                          kv("call", getJniMethodCall(context, "Void")));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return "return Py_BuildValue(\"\");";
        }
    }

    static class PrimitiveMethod extends PyCFunctionGenerator {
        PrimitiveMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            // Not needed, because primitive parameter type should automatically match JNI type
            return null;
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return format("j${type} ${res} = (j${type}) 0;",
                          kv("type", getReturnType().simpleTypeName()));
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            String typeName = firstCharToUpperCase(getReturnType().simpleTypeName());
            return format("${res} = ${call};",
                          kv("call", getJniMethodCall(context, typeName)));
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {

            String s = getReturnType().typeName();
            if (s.equals("boolean")) {
                return format("return PyBool_FromLong(${res});");
            } else if (s.equals("char")) {
                return format("return PyUnicode_FromFormat(\"%c\", ${res});");
            } else if (s.equals("byte")) {
                return format("return PyLong_FromLong(${res});");
            } else if (s.equals("short")) {
                return format("return PyLong_FromLong(${res});");
            } else if (s.equals("int")) {
                return format("return PyLong_FromLong(${res});");
            } else if (s.equals("long")) {
                return format("return PyLong_FromLongLong(${res});");
            } else if (s.equals("float")) {
                return format("return PyFloat_FromDouble(${res});");
            } else if (s.equals("double")) {
                return format("return PyFloat_FromDouble(${res});");
            } else {
                throw new IllegalArgumentException("can't deal with type '" + s + "'");
            }
        }

    }

    static class ObjectMethod extends PyCFunctionGenerator {
        ObjectMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateJniResultDeclaration(GeneratorContext context) {
            return format("jobject ${res}JObj = NULL;");
        }

        @Override
        public String generateTargetResultDeclaration(GeneratorContext context) {
            return format("PyObject* ${res}PyObj = NULL;");
        }

        @Override
        public String generateJniResultFromJniCallAssignment(GeneratorContext context) {
            return format("${res}JObj = ${call};",
                          kv("call", getJniMethodCall(context, "Object")));
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res}PyObj = beampy_newPyObjectFromJObject(${res}JObj);");
        }

        @Override
        public String generateJniResultDeref(GeneratorContext context) {
            return format("(*jenv)->DeleteLocalRef(jenv, ${res}JObj);");
        }

        @Override
        public String generateReturnStatement(GeneratorContext context) {
            return format("return ${res}PyObj;");
        }
    }

    static class StringMethod extends ObjectMethod {
        StringMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res}PyObj = beampy_newPyStringFromJString((jstring) ${res}JObj);");
        }
    }

    static abstract class ArrayMethod extends ObjectMethod {
        ArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }
    }

    static class PrimitiveArrayMethod extends ArrayMethod {
        PrimitiveArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            String carrayFormat = getCArrayFormat(getReturnType());
            ApiParameter returnParameter = getReturnParameter(context);
            String typeUC = firstCharToUpperCase(getReturnType().simpleTypeName());
            if (returnParameter != null) {
                return format("${res}PyObj = beampy_copyJ${typeUC}ArrayToPyObject((jarray) ${res}JObj, \"${carrayTypeCode}\", ${par}PyObj);",
                              kv("typeUC", typeUC),
                              kv("carrayFormat", carrayFormat),
                              kv("par", returnParameter.getJavaName()));
            } else {
                return format("${res}PyObj = beampy_newPyObjectFromJ${typeUC}Array((jarray) ${res}JObj, \"${carrayTypeCode}\");",
                              kv("typeUC", typeUC),
                              kv("carrayFormat", carrayFormat));
            }
        }
    }

    static class ObjectArrayMethod extends ArrayMethod {
        ObjectArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            String typeName = getReturnType().simpleTypeName();
            return format("${res}PyObj = beampy_newPySeqFromJObjectArray((jarray) ${res}JObj, \"${type}\");",
                          kv("type", typeName));
        }
    }

    static class StringArrayMethod extends ArrayMethod {
        StringArrayMethod(ApiMethod apiMethod, PyCParameterGenerator[] parameterGenerators) {
            super(apiMethod, parameterGenerators);
        }

        @Override
        public String generateTargetResultFromTransformedJniResultAssignment(GeneratorContext context) {
            return format("${res}PyObj = beampy_newPySeqFromJStringArray((jstringArray) ${res}JObj);");
        }
    }
}
