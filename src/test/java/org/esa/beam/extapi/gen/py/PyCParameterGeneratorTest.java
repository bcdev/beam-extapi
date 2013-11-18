package org.esa.beam.extapi.gen.py;

import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.DocMock;
import org.esa.beam.extapi.gen.MyGeneratorContext;
import org.esa.beam.extapi.gen.ParameterGenerator;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.ProductData;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Map;

import static org.esa.beam.extapi.gen.ApiParameter.Modifier;
import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class PyCParameterGeneratorTest {

    private MyGeneratorContext context;

    @Before
    public void setUp() throws Exception {
        context = new MyGeneratorContext();
    }

    @Test
    public void test_CodeGenParameter_PrimitiveScalar() {
        testPrimitiveScalar("x", Boolean.TYPE, Modifier.IN, "jboolean x = (jboolean) 0;", "x");
        testPrimitiveScalar("x", Byte.TYPE, Modifier.IN, "jbyte x = (jbyte) 0;", "x");
        testPrimitiveScalar("x", Character.TYPE, Modifier.IN, "jchar x = (jchar) 0;", "x");
        testPrimitiveScalar("x", Short.TYPE, Modifier.IN, "jshort x = (jshort) 0;", "x");
        testPrimitiveScalar("x", Integer.TYPE, Modifier.IN, "jint x = (jint) 0;", "x");
        testPrimitiveScalar("x", Long.TYPE, Modifier.IN, "jlong x = (jlong) 0;", "x");
        testPrimitiveScalar("x", Float.TYPE, Modifier.IN, "jfloat x = (jfloat) 0;", "x");
        testPrimitiveScalar("x", Double.TYPE, Modifier.IN, "jdouble x = (jdouble) 0;", "x");
    }

    @Test
    public void test_CodeGenParameter_ObjectScalar() {
        testObjectScalar("product", Product.class, Modifier.IN,
                         "PyObject* productPyObj = NULL;",
                         "jobject productJObj = NULL;",
                         "{\n" +
                                 "    jboolean ok = 1;\n" +
                                 "    productJObj = BPy_ToJObjectT(productPyObj, _ProductClass, &ok);\n" +
                                 "    if (!ok) {\n" +
                                 "        return NULL;\n" +
                                 "    }\n" +
                                 "}",
                         "productJObj", null, null);
        testObjectScalar("band", Band.class, Modifier.IN,
                         "PyObject* bandPyObj = NULL;",
                         "jobject bandJObj = NULL;",
                         "{\n" +
                                 "    jboolean ok = 1;\n" +
                                 "    bandJObj = BPy_ToJObjectT(bandPyObj, _BandClass, &ok);\n" +
                                 "    if (!ok) {\n" +
                                 "        return NULL;\n" +
                                 "    }\n" +
                                 "}",
                         "bandJObj", null, null);
        testObjectScalar("data", ProductData.UShort.class, Modifier.IN,
                         "PyObject* dataPyObj = NULL;",
                         "jobject dataJObj = NULL;",
                         "{\n" +
                                 "    jboolean ok = 1;\n" +
                                 "    dataJObj = BPy_ToJObjectT(dataPyObj, _UShortClass, &ok);\n" +
                                 "    if (!ok) {\n" +
                                 "        return NULL;\n" +
                                 "    }\n" +
                                 "}",
                         "dataJObj", null, null);
    }

    @Test
    public void test_CodeGenParameter_ObjectScalar_nonAPI() {
        testObjectScalar("point", Point2D.Double.class, Modifier.IN,
                         "PyObject* pointPyObj = NULL;",
                         "jobject pointJObj = NULL;",
                         "{\n" +
                                 "    jboolean ok = 1;\n" +
                                 "    pointJObj = BPy_ToJObjectT(pointPyObj, _DoubleClass, &ok);\n" +
                                 "    if (!ok) {\n" +
                                 "        return NULL;\n" +
                                 "    }\n" +
                                 "}",
                         "pointJObj",
                         null,
                         null);
        testObjectScalar("file", File.class, Modifier.IN,
                         "PyObject* filePyObj = NULL;",
                         "jobject fileJObj = NULL;",
                         "{\n" +
                                 "    jboolean ok = 1;\n" +
                                 "    fileJObj = BPy_ToJObjectT(filePyObj, _FileClass, &ok);\n" +
                                 "    if (!ok) {\n" +
                                 "        return NULL;\n" +
                                 "    }\n" +
                                 "}",
                         "fileJObj",
                         null,
                         null);
        testObjectScalar("parameters", Map.class, Modifier.IN,
                         "PyObject* parametersPyObj = NULL;",
                         "jobject parametersJObj = NULL;",
                         "{\n" +
                                 "    jboolean ok = 1;\n" +
                                 "    parametersJObj = BPy_ToJObjectT(parametersPyObj, _MapClass, &ok);\n" +
                                 "    if (!ok) {\n" +
                                 "        return NULL;\n" +
                                 "    }\n" +
                                 "}",
                         "parametersJObj",
                         null,
                         null);
    }

    @Test
    public void test_CodeGenParameter_StringScalar() {
        testGenerators(new PyCParameterGenerator.StringScalar(createParam("name", String.class, Modifier.IN)),
                       "const char* name = NULL;",
                       "jstring nameJObj = NULL;",
                       "nameJObj =(*jenv)->NewStringUTF(jenv, name);",
                       "nameJObj",
                       null,
                       "(*jenv)->DeleteLocalRef(jenv, nameJObj);");
    }

    @Test
    public void test_CodeGenParameter_PrimitiveArray() {
        testPrimitiveArray("data", boolean[].class, Modifier.IN,
                           "jboolean* dataData = NULL;\n" +
                                   "int dataLength = 0;\n" +
                                   "PyObject* dataPyObj = NULL;\n" +
                                   "Py_buffer dataBuf;",
                           "jarray dataJObj = NULL;",
                           "dataPyObj = BPy_ToPrimitiveArrayBufferReadOnly(dataPyObj, &dataBuf, \"b\", -1);\n" +
                                   "if (dataPyObj == NULL) {\n" +
                                   "    return NULL;\n" +
                                   "}\n" +
                                   "dataData = (jboolean*) dataBuf.buf;\n" +
                                   "dataLength = dataBuf.len / dataBuf.itemsize;\n" +
                                   "dataJObj = BPy_NewJBooleanArrayFromBuffer(dataData, dataLength);\n" +
                                   "if (dataJObj == NULL) {\n" +
                                   "    return NULL;\n" +
                                   "}",
                           "dataJObj",
                           null,
                           "PyBuffer_Release(&dataBuf);\n" +
                                   "(*jenv)->DeleteLocalRef(jenv, dataJObj);");

        testPrimitiveArray("data", int[].class, Modifier.OUT,
                           "jint* dataData = NULL;\n" +
                                   "int dataLength = 0;\n" +
                                   "PyObject* dataPyObj = NULL;\n" +
                                   "Py_buffer dataBuf;",
                           "jarray dataJObj = NULL;",
                           "dataPyObj = BPy_ToPrimitiveArrayBufferWritable(dataPyObj, &dataBuf, \"i\", -1);\n" +
                                   "if (dataPyObj == NULL) {\n" +
                                   "    return NULL;\n" +
                                   "}\n" +
                                   "dataData = (jint*) dataBuf.buf;\n" +
                                   "dataLength = dataBuf.len / dataBuf.itemsize;\n" +
                                   "dataJObj = BPy_NewJIntArrayFromBuffer(dataData, dataLength);\n" +
                                   "if (dataJObj == NULL) {\n" +
                                   "    return NULL;\n" +
                                   "}",
                           "dataJObj",
                           "BPy_CopyJIntArrayToBuffer((jarray) dataJObj, dataData, dataLength, dataPyObj);",
                           "PyBuffer_Release(&dataBuf);\n" +
                                   "(*jenv)->DeleteLocalRef(jenv, dataJObj);");

        testPrimitiveArray("data", double[].class, Modifier.RETURN,
                           "jdouble* dataData = NULL;\n" +
                                   "int dataLength = 0;\n" +
                                   "PyObject* dataPyObj = NULL;\n" +
                                   "Py_buffer dataBuf;",
                           "jarray dataJObj = NULL;",
                           "dataPyObj = BPy_ToPrimitiveArrayBufferWritable(dataPyObj, &dataBuf, \"d\", -1);\n" +
                                   "if (dataPyObj == NULL) {\n" +
                                   "    return NULL;\n" +
                                   "}\n" +
                                   "dataData = (jdouble*) dataBuf.buf;\n" +
                                   "dataLength = dataBuf.len / dataBuf.itemsize;\n" +
                                   "dataJObj = BPy_NewJDoubleArrayFromBuffer(dataData, dataLength);\n" +
                                   "if (dataJObj == NULL) {\n" +
                                   "    return NULL;\n" +
                                   "}",
                           "dataJObj",
                           "if (dataData != NULL && (*jenv)->IsSameObject(jenv, _resultJObj, dataJObj)) {\n" +
                                   "    _resultPyObj = BPy_CopyJDoubleArrayToBuffer((jarray) dataJObj, dataData, dataLength, dataPyObj);\n" +
                                   "} else {\n" +
                                   "    _resultPyObj = BPy_FromJDoubleArray((jarray) dataJObj);\n" +
                                   "}",
                           "PyBuffer_Release(&dataBuf);\n" +
                                   "(*jenv)->DeleteLocalRef(jenv, dataJObj);");
    }

    @Test
    public void test_CodeGenParameter_ObjectArray() {
        testObjectArray("bands", Band[].class, Modifier.IN,
                        "PyObject* bandsPyObj = NULL;",
                        "jarray bandsJObj = NULL;",
                        "{\n" +
                                "    jboolean ok = 1;\n" +
                                "    bandsJObj = BPy_ToJObjectArrayT(bandsPyObj, _BandClass, &ok);\n" +
                                "    if (!ok) {\n" +
                                "        return NULL;\n" +
                                "    }\n" +
                                "}",
                        "bandsJObj",
                        null,
                        "(*jenv)->DeleteLocalRef(jenv, bandsJObj);"
        );

        testObjectArray("bands", Band[].class, Modifier.OUT,
                        "PyObject* bandsPyObj = NULL;",
                        "jarray bandsJObj = NULL;",
                        "{\n" +
                                "    jboolean ok = 1;\n" +
                                "    bandsJObj = BPy_ToJObjectArrayT(bandsPyObj, _BandClass, &ok);\n" +
                                "    if (!ok) {\n" +
                                "        return NULL;\n" +
                                "    }\n" +
                                "}",
                        "bandsJObj",
                        null,
                        "(*jenv)->DeleteLocalRef(jenv, bandsJObj);"
        );

        testObjectArray("bands", Band[].class, Modifier.RETURN,
                        "PyObject* bandsPyObj = NULL;",
                        "jarray bandsJObj = NULL;",
                        "{\n" +
                                "    jboolean ok = 1;\n" +
                                "    bandsJObj = BPy_ToJObjectArrayT(bandsPyObj, _BandClass, &ok);\n" +
                                "    if (!ok) {\n" +
                                "        return NULL;\n" +
                                "    }\n" +
                                "}",
                        "bandsJObj",
                        null,
                        "(*jenv)->DeleteLocalRef(jenv, bandsJObj);"
        );
    }

    @Test
    public void test_CodeGenParameter_StringArray() {
        testStringArray("names", String[].class, Modifier.IN,
                        "PyObject* namesPyObj = NULL;",
                        "jarray namesJObj = NULL;",
                        "{\n" +
                                "    jboolean ok = 1;\n" +
                                "    namesJObj = BPy_ToJStringArray(namesPyObj, &ok);\n" +
                                "    if (!ok) {\n" +
                                "        return NULL;\n" +
                                "    }\n" +
                                "}",
                        "namesJObj",
                        null,
                        "(*jenv)->DeleteLocalRef(jenv, namesJObj);"
        );

        testStringArray("names", String[].class, Modifier.OUT,
                        "PyObject* namesPyObj = NULL;",
                        "jarray namesJObj = NULL;",
                        "{\n" +
                                "    jboolean ok = 1;\n" +
                                "    namesJObj = BPy_ToJStringArray(namesPyObj, &ok);\n" +
                                "    if (!ok) {\n" +
                                "        return NULL;\n" +
                                "    }\n" +
                                "}",
                        "namesJObj",
                        null,
                        "(*jenv)->DeleteLocalRef(jenv, namesJObj);"
        );

        testStringArray("names", String[].class, Modifier.RETURN,
                        "PyObject* namesPyObj = NULL;",
                        "jarray namesJObj = NULL;",
                        "{\n" +
                                "    jboolean ok = 1;\n" +
                                "    namesJObj = BPy_ToJStringArray(namesPyObj, &ok);\n" +
                                "    if (!ok) {\n" +
                                "        return NULL;\n" +
                                "    }\n" +
                                "}",
                        "namesJObj",
                        null,
                        "(*jenv)->DeleteLocalRef(jenv, namesJObj);"
        );
    }

    private void testPrimitiveScalar(String name, Class<?> type,
                                     Modifier modifier,
                                     String jniArgDecl,
                                     String jniCallArgExpr) {
        testGenerators(new PyCParameterGenerator.PrimitiveScalar(createParam(name, type, modifier)),
                       null,
                       jniArgDecl,
                       null,
                       jniCallArgExpr,
                       null,
                       null);
    }

    private void testPrimitiveArray(String name, Class<?> type, Modifier modifier,
                                    String targetArgDecl,
                                    String jniArgDecl,
                                    String preCallCode,
                                    String jniCallArgExpr,
                                    String postCallCode,
                                    String cleanUpCode) {
        testGenerators(new PyCParameterGenerator.PrimitiveArray(createParam(name, type, modifier)),
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       jniCallArgExpr,
                       postCallCode,
                       cleanUpCode);
    }

    private void testStringArray(String name, Class<?> type, Modifier modifier,
                                 String targetArgDecl,
                                 String jniArgDecl,
                                 String preCallCode,
                                 String jniCallArgExpr,
                                 String postCallCode,
                                 String cleanUpCode) {
        testGenerators(new PyCParameterGenerator.StringArray(createParam(name, type, modifier)),
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       jniCallArgExpr,
                       postCallCode,
                       cleanUpCode);
    }

    private void testObjectArray(String name, Class<?> type, Modifier modifier,
                                 String targetArgDecl,
                                 String jniArgDecl,
                                 String preCallCode,
                                 String jniCallArgExpr,
                                 String postCallCode,
                                 String cleanUpCode) {
        testGenerators(new PyCParameterGenerator.ObjectArray(createParam(name, type, modifier)),
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       jniCallArgExpr,
                       postCallCode,
                       cleanUpCode);
    }

    private void testObjectScalar(String name, Class<?> type, Modifier modifier,
                                  String targetArgDecl,
                                  String jniArgDecl,
                                  String preCallCode,
                                  String jniCallArgExpr,
                                  String postCallCode,
                                  String cleanUpCode) {
        testGenerators(new PyCParameterGenerator.ObjectScalar(createParam(name, type, modifier)),
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       jniCallArgExpr,
                       postCallCode,
                       cleanUpCode);
    }

    private void testGenerators(ParameterGenerator parameterGenerator,
                                String targetArgDecl,
                                String jniArgDecl,
                                String preCallCode,
                                String jniCallArgExpr,
                                String postCallCode,
                                String cleanUpCode) {
        // We don't generate parameters because the Python API uses PyObject* args
        assertEquals(null, parameterGenerator.generateFunctionParamDeclaration(context));
        assertEquals(targetArgDecl, parameterGenerator.generateTargetArgDeclaration(context));
        assertEquals(jniArgDecl, parameterGenerator.generateJniArgDeclaration(context));
        assertEquals(preCallCode, parameterGenerator.generateJniArgFromTransformedTargetArgAssignment(context));
        assertEquals(jniCallArgExpr, parameterGenerator.generateJniCallArgs(context));
        assertEquals(postCallCode, parameterGenerator.generateTargetArgFromTransformedJniArgAssignment(context));
        assertEquals(cleanUpCode, parameterGenerator.generateJniArgDeref(context));
    }

    private ApiParameter createParam(String name, Class<?> type, Modifier modifier) {
        return new ApiParameter(DocMock.createParameter(name, type), modifier);
    }

}
