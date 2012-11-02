package org.esa.beam.extapi.gen;

import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.ProductData;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class CodeGenParameterTest {

    private MyGeneratorContext context;

    @Before
    public void setUp() throws Exception {
        context = new MyGeneratorContext();
    }

    @Test
    public void test_CodeGenParameter_PrimitiveScalar() {
        testPrimitiveScalar("x", Boolean.TYPE, "boolean x", "x");
        testPrimitiveScalar("x", Byte.TYPE, "byte x", "x");
        testPrimitiveScalar("x", Character.TYPE, "char x", "x");
        testPrimitiveScalar("x", Short.TYPE, "short x", "x");
        testPrimitiveScalar("x", Integer.TYPE, "int x", "x");
        testPrimitiveScalar("x", Long.TYPE, "dlong x", "x");
        testPrimitiveScalar("x", Float.TYPE, "float x", "x");
        testPrimitiveScalar("x", Double.TYPE, "double x", "x");
    }

    @Test
    public void test_CodeGenParameter_PrimitiveArray() {
        testPrimitiveArray("data", boolean[].class,
                           "const boolean* dataElems, int dataLength",
                           "jarray dataArray = NULL;\n" +
                                   "void* dataArrayAddr = NULL;",
                           "dataArray = (*jenv)->NewBooleanArray(jenv, dataLength);\n" +
                                   "dataArrayAddr = (*jenv)->GetPrimitiveArrayCritical(jenv, dataArray, 0);\n" +
                                   "memcpy(dataArrayAddr, dataElems, dataLength);",
                           "dataArray",
                           "(*jenv)->ReleasePrimitiveArrayCritical(jenv, dataArray, dataArrayAddr, 0);");
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
    public void test_CodeGenParameter_ObjectArray() {
        testObjectArray("bands", Band[].class,
                        "const Band* bandsElems, int bandsLength",
                        "jarray bandsArray = NULL;",
                        "bandsArray = beam_new_jobject_array(bandsElems, bandsLength, classBand);",
                        "bandsArray",
                        null);
    }

    @Test
    public void test_CodeGenParameter_StringScalar() {
        final CodeGenParameter.StringScalar stringScalar = new CodeGenParameter.StringScalar(new MyParameter("name", String.class));
        assertEquals("const char* name", stringScalar.generateParamListDecl(context));
        assertEquals("jstring nameString = NULL;", stringScalar.generateLocalVarDecl(context));
        assertEquals("nameString = (*jenv)->NewStringUTF(jenv, name);", stringScalar.generatePreCallCode(context));
        assertEquals("nameString", stringScalar.generateCallCode(context));
    }

    @Test
    public void test_CodeGenParameter_StringArray() {
        final CodeGenParameter.StringArray stringArray = new CodeGenParameter.StringArray(new MyParameter("names", String[].class));
        assertEquals("const char** namesElems, int namesLength", stringArray.generateParamListDecl(context));
        assertEquals("jobjectArray namesArray = NULL;", stringArray.generateLocalVarDecl(context));
        assertEquals("namesArray = beam_new_jstring_array(namesElems, namesLength);", stringArray.generatePreCallCode(context));
        assertEquals("namesArray", stringArray.generateCallCode(context));
    }

    private void testPrimitiveScalar(String name, Class<?> type, String paramListDecl, String callArgExpr) {
        testGenerators(new CodeGenParameter.PrimitiveScalar(new MyParameter(name, type)), paramListDecl, null, null, callArgExpr, null);
    }

    private void testPrimitiveArray(String name, Class<?> type,
                                    String paramListDecl,
                                    String localVarDecl,
                                    String preCallCode,
                                    String callArgExpr,
                                    String postCallCode) {
        testGenerators(new CodeGenParameter.PrimitiveArray(new MyParameter(name, type), true),
                       paramListDecl,
                       localVarDecl,
                       preCallCode,
                       callArgExpr,
                       postCallCode);
    }
    private void testObjectArray(String name, Class<?> type,
                                    String paramListDecl,
                                    String localVarDecl,
                                    String preCallCode,
                                    String callArgExpr,
                                    String postCallCode) {
        testGenerators(new CodeGenParameter.ObjectArray(new MyParameter(name, type), true),
                       paramListDecl,
                       localVarDecl,
                       preCallCode,
                       callArgExpr,
                       postCallCode);
    }

    private void testObjectScalar(String name, Class<?> type, String paramListDecl, String callArgExpr) {
        testGenerators(new CodeGenParameter.ObjectScalar(new MyParameter(name, type)), paramListDecl, null, null, callArgExpr, null);
    }

    private void testGenerators(CodeGenParameter parameter,
                                String paramListDecl,
                                String localVarDecl,
                                String preCallCode,
                                String callArgExpr,
                                String postCallCode) {
        assertEquals(paramListDecl, parameter.generateParamListDecl(context));
        assertEquals(localVarDecl, parameter.generateLocalVarDecl(context));
        assertEquals(preCallCode, parameter.generatePreCallCode(context));
        assertEquals(callArgExpr, parameter.generateCallCode(context));
        assertEquals(postCallCode, parameter.generatePostCallCode(context));
    }

}
