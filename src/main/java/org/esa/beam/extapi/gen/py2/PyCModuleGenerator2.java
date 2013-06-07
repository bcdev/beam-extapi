package org.esa.beam.extapi.gen.py2;

import org.esa.beam.extapi.gen.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.esa.beam.extapi.gen.JavadocHelpers.convertToPythonDoc;
import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGenerator2 {
    public static final String BEAM_PYAPI_C_TARGET_DIR = "src/main/c/gen2";
    public static final String BEAM_PYAPI_NAME = "beampy";

    private final ApiInfo apiInfo;

    public PyCModuleGenerator2(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    public void run() throws IOException {
        new File(BEAM_PYAPI_C_TARGET_DIR).mkdirs();
        writeWinDef();
        writeCHeader();
        writeCSource();
    }

    private void writeWinDef() throws IOException {
        new TargetFile(BEAM_PYAPI_C_TARGET_DIR, BEAM_PYAPI_NAME + ".def") {
            @Override
            protected void writeContent() throws IOException {
                writer.write(eval("" +
                                          "LIBRARY \"${L}.pyd\"\n" +
                                          "\n" +
                                          "EXPORTS\n" +
                                          "\tPyInit_${L}",
                                  kv("L", BEAM_PYAPI_NAME)));
            }
        }.create();
    }

    private void writeCHeader() throws IOException {
        new TargetCHeaderFile(BEAM_PYAPI_C_TARGET_DIR, BEAM_PYAPI_NAME + ".h") {
            @Override
            protected void writeContent() throws IOException {
                writer.printf("#include \"Python.h\"\n");
                writer.printf("#include \"structmember.h\"\n");
                writer.printf("#include \"../beampy_carray.h\"\n");
                writer.printf("#include \"../beampy_jobject.h\"\n");
                writer.printf("\n");

                Set<ApiClass> apiClasses = apiInfo.getApiClasses();
                for (ApiClass apiClass : apiClasses) {
                    String className = apiClass.getType().simpleTypeName();
                    writer.printf("PyAPI_DATA(PyTypeObject) %s_Type;\n",
                                  className);
                }
            }
        }.create();
    }

    private void writeCSource() throws IOException {
        new TargetCFile(BEAM_PYAPI_C_TARGET_DIR, BEAM_PYAPI_NAME + ".c") {
            @Override
            protected void writeContent() throws IOException {
                writer.printf("#include \"beampy.h\"\n");

                Set<ApiClass> apiClasses = apiInfo.getApiClasses();
                for (ApiClass apiClass : apiClasses) {
                    String className = apiClass.getType().simpleTypeName();
                    templateEval.add(kv("className", className),
                                     kv("classDoc", convertToPythonDoc(apiInfo, apiClass.getType().asClassDoc(), "", true)));

                    writeText("static PyMethodDef ${className}_Methods[] = \n" +
                                      "{\n");
                    List<ApiMethod> methods = apiInfo.getMethodsOf(apiClass);
                    for (ApiMethod method : methods) {
                        String methodName = method.getJavaName();
                        templateEval.add(kv("methodName", methodName),
                                         kv("methodDoc", convertToPythonDoc(apiInfo, method.getMemberDoc(), "", true)),
                                         kv("methodFlags", method.getMemberDoc().isStatic() ? "METH_VARARGS | METH_STATIC" : "METH_VARARGS"));

                        writeText("    {\"${methodName}\", (PyCFunction) ${className}_${methodName}, ${methodFlags}, \"${methodDoc}\"},\n");
                    }
                    writeText("    {NULL, NULL, 0, NULL}  /* Sentinel */\n");
                    writeText("};\n");
                    writeText("\n");

                    writeResource("pytypedef.templ.c");

                    writeText("\n");

                }
            }
        }.create();
    }

}
