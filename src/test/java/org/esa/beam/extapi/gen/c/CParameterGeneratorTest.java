package org.esa.beam.extapi.gen.c;

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
public class CParameterGeneratorTest {

    private MyGeneratorContext context;

    @Before
    public void setUp() throws Exception {
        context = new MyGeneratorContext();
    }

    @Test
    public void test_CodeGenParameter_PrimitiveScalar() {
        testPrimitiveScalar("x", Boolean.TYPE, Modifier.IN, "boolean x", "x");
        testPrimitiveScalar("x", Byte.TYPE, Modifier.IN, "byte x", "x");
        testPrimitiveScalar("x", Character.TYPE, Modifier.IN, "char x", "x");
        testPrimitiveScalar("x", Short.TYPE, Modifier.IN, "short x", "x");
        testPrimitiveScalar("x", Integer.TYPE, Modifier.IN, "int x", "x");
        testPrimitiveScalar("x", Long.TYPE, Modifier.IN, "dlong x", "x");
        testPrimitiveScalar("x", Float.TYPE, Modifier.IN, "float x", "x");
        testPrimitiveScalar("x", Double.TYPE, Modifier.IN, "double x", "x");
    }

    @Test
    public void test_CodeGenParameter_ObjectScalar_API() {
        testObjectScalar("product", Product.class, "Product product", "product");
        testObjectScalar("band", Band.class, "Band band", "band");
        testObjectScalar("data", ProductData.UShort.class, "ProductData_UShort data", "data");
    }

    @Test
    public void test_CodeGenParameter_ObjectScalar_nonAPI() {
        testObjectScalar("point", Point2D.Double.class, "Point2D_Double point", "point");
        testObjectScalar("file", File.class, "File file", "file");
        testObjectScalar("parameters", Map.class, "Map parameters", "parameters");
    }

    @Test
    public void test_CodeGenParameter_StringScalar() {
        testGenerators(new CParameterGenerator.StringScalar(createParam("name", String.class, Modifier.IN)),
                       "const char* name",
                       null,
                       "jstring nameString = NULL;",
                       "nameString = (*jenv)->NewStringUTF(jenv, name);",
                       "nameString",
                       null);
    }

    @Test
    public void test_CodeGenParameter_PrimitiveArray() {
        testPrimitiveArray("data", boolean[].class, Modifier.IN,
                           "const boolean* dataElems, int dataLength",
                           null,
                           "jarray dataArray = NULL;",
                           "dataArray = (*jenv)->NewBooleanArray(jenv, dataLength);\n" +
                                   "beam_copyToJArray(dataArray, dataElems, dataLength, sizeof (boolean));",
                           "dataArray",
                           null);

        testPrimitiveArray("data", int[].class, Modifier.OUT,
                           "int* dataElems, int dataLength",
                           null,
                           "jarray dataArray = NULL;",
                           "dataArray = (*jenv)->NewIntArray(jenv, dataLength);",
                           "dataArray",
                           "beam_copyFromJArray(dataArray, dataElems, dataLength, sizeof (int));");

        testPrimitiveArray("data", float[].class, Modifier.RETURN,
                           "float* dataElems, int dataLength",
                           null,
                           "jarray dataArray = NULL;",
                           "dataArray = (*jenv)->NewFloatArray(jenv, dataLength);",
                           "dataArray",
                           "if (dataElems != NULL && (*jenv)->IsSameObject(jenv, dataArray, _resultArray)) {\n" +
                                   "    beam_copyFromJArray(_resultArray, dataElems, dataLength, sizeof (float));\n" +
                                   "    _result = dataElems;\n" +
                                   "    if (resultArrayLength != NULL) {\n" +
                                   "        *resultArrayLength = dataLength;\n" +
                                   "    }\n" +
                                   "} else {\n" +
                                   "    _result = beam_newCFloatArray(_resultArray, resultArrayLength);\n" +
                                   "}");
    }

    @Test
    public void test_CodeGenParameter_ObjectArray() {
        testObjectArray("bands", Band[].class, Modifier.IN,
                        "const Band bandsElems, int bandsLength",
                        null,
                        "jarray bandsArray = NULL;",
                        "bandsArray = beam_newJObjectArray(bandsElems, bandsLength, classBand);",
                        "bandsArray",
                        null);

        testObjectArray("bands", Band[].class, Modifier.OUT,
                        "Band bandsElems, int bandsLength",
                        null,
                        "jarray bandsArray = NULL;",
                        "bandsArray = beam_newJObjectArray(bandsElems, bandsLength, classBand);",
                        "bandsArray",
                        null);

        testObjectArray("bands", Band[].class, Modifier.RETURN,
                        "Band bandsElems, int bandsLength",
                        null,
                        "jarray bandsArray = NULL;",
                        "bandsArray = beam_newJObjectArray(bandsElems, bandsLength, classBand);",
                        "bandsArray",
                        null);
    }

    @Test
    public void test_CodeGenParameter_StringArray() {
        testStringArray("names", String[].class, Modifier.IN,
                        "const char** namesElems, int namesLength",
                        null,
                        "jobjectArray namesArray = NULL;",
                        "namesArray = beam_newJStringArray(namesElems, namesLength);",
                        "namesArray",
                        null);

        testStringArray("names", String[].class, Modifier.OUT,
                        "char** namesElems, int namesLength",
                        null,
                        "jobjectArray namesArray = NULL;",
                        "namesArray = beam_newJStringArray(namesElems, namesLength);",
                        "namesArray",
                        null);

        testStringArray("names", String[].class, Modifier.RETURN,
                        "char** namesElems, int namesLength",
                        null,
                        "jobjectArray namesArray = NULL;",
                        "namesArray = beam_newJStringArray(namesElems, namesLength);",
                        "namesArray",
                        null);
    }

    private void testPrimitiveScalar(String name, Class<?> type, Modifier modifier, String paramListDecl, String callArgExpr) {
        testGenerators(new CParameterGenerator.PrimitiveScalar(createParam(name, type, modifier)),
                       paramListDecl,
                       null,
                       null, null,
                       callArgExpr,
                       null);
    }

    private void testPrimitiveArray(String name, Class<?> type, Modifier modifier,
                                    String paramListDecl,
                                    String targetArgDecl,
                                    String jniArgDecl,
                                    String preCallCode, String callArgExpr, String postCallCode) {
        testGenerators(new CParameterGenerator.PrimitiveArray(createParam(name, type, modifier)),
                       paramListDecl,
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       callArgExpr,
                       postCallCode);
    }

    private void testStringArray(String name, Class<?> type, Modifier modifier,
                                 String paramListDecl,
                                 String targetArgDecl,
                                 String jniArgDecl,
                                 String preCallCode,
                                 String callArgExpr,
                                 String postCallCode) {
        testGenerators(new CParameterGenerator.StringArray(createParam(name, type, modifier)),
                       paramListDecl,
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       callArgExpr,
                       postCallCode);
    }

    private void testObjectArray(String name, Class<?> type, Modifier modifier,
                                 String paramListDecl,
                                 String targetArgDecl,
                                 String jniArgDecl,
                                 String preCallCode,
                                 String callArgExpr,
                                 String postCallCode) {
        testGenerators(new CParameterGenerator.ObjectArray(createParam(name, type, modifier)),
                       paramListDecl,
                       targetArgDecl,
                       jniArgDecl,
                       preCallCode,
                       callArgExpr,
                       postCallCode);
    }

    private void testObjectScalar(String name, Class<?> type, String paramListDecl, String callArgExpr) {
        testGenerators(new CParameterGenerator.ObjectScalar(createParam(name, type, Modifier.IN)),
                       paramListDecl, null, null, null, callArgExpr, null);
    }

    private void testGenerators(ParameterGenerator parameterGenerator,
                                String paramListDecl,
                                String targetArgDecl,
                                String jniArgDecl,
                                String preCallCode,
                                String callArgExpr,
                                String postCallCode) {
        assertEquals(paramListDecl, parameterGenerator.generateFunctionParamDeclaration(context));
        assertEquals(targetArgDecl, parameterGenerator.generateTargetArgDeclaration(context));
        assertEquals(jniArgDecl, parameterGenerator.generateJniArgDeclaration(context));
        assertEquals(preCallCode, parameterGenerator.generateJniArgFromTransformedTargetArgAssignment(context));
        assertEquals(callArgExpr, parameterGenerator.generateJniCallArgs(context));
        assertEquals(postCallCode, parameterGenerator.generateTargetResultFromTransformedJniResultAssignment(context));
    }

    private ApiParameter createParam(String name, Class<?> type, Modifier modifier) {
        return new ApiParameter(DocMock.createParameter(name, type), modifier);
    }

}
