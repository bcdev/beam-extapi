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
        testPrimitiveScalar("x", Boolean.TYPE, Modifier.IN, "boolean x;", "x");
        testPrimitiveScalar("x", Byte.TYPE, Modifier.IN, "byte x;", "x");
        testPrimitiveScalar("x", Character.TYPE, Modifier.IN, "char x;", "x");
        testPrimitiveScalar("x", Short.TYPE, Modifier.IN, "short x;", "x");
        testPrimitiveScalar("x", Integer.TYPE, Modifier.IN, "int x;", "x");
        testPrimitiveScalar("x", Long.TYPE, Modifier.IN, "dlong x;", "x");
        testPrimitiveScalar("x", Float.TYPE, Modifier.IN, "float x;", "x");
        testPrimitiveScalar("x", Double.TYPE, Modifier.IN, "double x;", "x");
    }

    @Test
    public void test_CodeGenParameter_ObjectScalar_API() {
        testObjectScalar("product", Product.class, "const char* productType;\n" +
                "unsigned PY_LONG_LONG product;", "(Product) product");
        testObjectScalar("band", Band.class, "const char* bandType;\n" +
                "unsigned PY_LONG_LONG band;", "(Band) band");
        testObjectScalar("data", ProductData.UShort.class, "const char* dataType;\n" +
                "unsigned PY_LONG_LONG data;", "(ProductData_UShort) data");
    }

    @Test
    public void test_CodeGenParameter_ObjectScalar_nonAPI() {
        testObjectScalar("point", Point2D.Double.class, "const char* pointType;\n" +
                "unsigned PY_LONG_LONG point;", "(Point2D_Double) point");
        testObjectScalar("file", File.class, "const char* fileType;\n" +
                "unsigned PY_LONG_LONG file;", "(File) file");
        testObjectScalar("parameters", Map.class, "const char* parametersType;\n" +
                "unsigned PY_LONG_LONG parameters;", "(Map) parameters");
    }

    @Test
    public void test_CodeGenParameter_StringScalar() {
        testGenerators(new PyCParameterGenerator.StringScalar(createParam("name", String.class, Modifier.IN)),
                       "const char* name;",
                       null,
                       "name"
        );
    }

    @Test
    public void test_CodeGenParameter_PrimitiveArray() {
        testPrimitiveArray("data", boolean[].class, Modifier.IN,
                           "boolean* data;\n" +
                                   "int dataLength;\n" +
                                   "PyObject* dataSeq;",
                           "data = beam_new_boolean_array_from_pyseq(dataSeq, &dataLength);",
                           "data, dataLength"
        );

        testPrimitiveArray("data", int[].class, Modifier.OUT,
                           "int* data;\n" +
                                   "int dataLength;\n" +
                                   "PyObject* dataSeq;",
                           "data = beam_new_int_array_from_pyseq(dataSeq, &dataLength);",
                           "data, dataLength"
        );

        testPrimitiveArray("data", float[].class, Modifier.RETURN,
                           "float* data;\n" +
                                   "int dataLength;\n" +
                                   "PyObject* dataSeq;",
                           "data = beam_new_float_array_from_pyseq(dataSeq, &dataLength);",
                           "data, dataLength"
        );
    }

    @Test
    public void test_CodeGenParameter_ObjectArray() {
        testObjectArray("bands", Band[].class, Modifier.IN,
                        "Band bands;\n" +
                                "int bandsLength;\n" +
                                "PyObject* bandsSeq;",
                        "bands = beam_new_jobject_array_from_pyseq(\"Band\", bandsSeq, &bandsLength);",
                        "bands, bandsLength"
        );

        testObjectArray("bands", Band[].class, Modifier.OUT,
                        "Band bands;\n" +
                                "int bandsLength;\n" +
                                "PyObject* bandsSeq;",
                        "bands = beam_new_jobject_array_from_pyseq(\"Band\", bandsSeq, &bandsLength);",
                        "bands, bandsLength"
        );

        testObjectArray("bands", Band[].class, Modifier.RETURN,
                        "Band bands;\n" +
                                "int bandsLength;\n" +
                                "PyObject* bandsSeq;",
                        "bands = beam_new_jobject_array_from_pyseq(\"Band\", bandsSeq, &bandsLength);",
                        "bands, bandsLength"
        );
    }

    @Test
    public void test_CodeGenParameter_StringArray() {
        testStringArray("names", String[].class, Modifier.IN,
                        "char** names;\n" +
                                "int namesLength;\n" +
                                "PyObject* namesSeq;",
                        "names = beam_new_string_array_from_pyseq(namesSeq, &namesLength);",
                        "names, namesLength"
        );

        testStringArray("names", String[].class, Modifier.OUT,
                        "char** names;\n" +
                                "int namesLength;\n" +
                                "PyObject* namesSeq;",
                        "names = beam_new_string_array_from_pyseq(namesSeq, &namesLength);",
                        "names, namesLength"
        );

        testStringArray("names", String[].class, Modifier.RETURN,
                        "char** names;\n" +
                                "int namesLength;\n" +
                                "PyObject* namesSeq;",
                        "names = beam_new_string_array_from_pyseq(namesSeq, &namesLength);",
                        "names, namesLength"
        );
    }

    private void testPrimitiveScalar(String name, Class<?> type, Modifier modifier, String localVarDecl, String callArgExpr) {
        testGenerators(new PyCParameterGenerator.PrimitiveScalar(createParam(name, type, modifier)),
                       localVarDecl,
                       null,
                       callArgExpr
        );
    }

    private void testPrimitiveArray(String name, Class<?> type, Modifier modifier,
                                    String localVarDecl,
                                    String preCallCode,
                                    String callArgExpr) {
        testGenerators(new PyCParameterGenerator.PrimitiveArray(createParam(name, type, modifier)),
                       localVarDecl,
                       preCallCode,
                       callArgExpr
        );
    }

    private void testStringArray(String name, Class<?> type, Modifier modifier,
                                 String localVarDecl,
                                 String preCallCode,
                                 String callArgExpr) {
        testGenerators(new PyCParameterGenerator.StringArray(createParam(name, type, modifier)),
                       localVarDecl,
                       preCallCode,
                       callArgExpr
        );
    }

    private void testObjectArray(String name, Class<?> type, Modifier modifier,
                                 String localVarDecl,
                                 String preCallCode,
                                 String callArgExpr) {
        testGenerators(new PyCParameterGenerator.ObjectArray(createParam(name, type, modifier)),
                       localVarDecl,
                       preCallCode,
                       callArgExpr
        );
    }

    private void testObjectScalar(String name, Class<?> type, String localVarDecl, String callArgExpr) {
        testGenerators(new PyCParameterGenerator.ObjectScalar(createParam(name, type, Modifier.IN)),
                       localVarDecl, null, callArgExpr);
    }

    private void testGenerators(ParameterGenerator parameterGenerator,
                                String localVarDecl,
                                String preCallCode,
                                String callArgExpr) {
        // We don't generate parameters because the Python API uses PyObject* args
        assertEquals(null, parameterGenerator.generateParamListDecl(context));
        assertEquals(localVarDecl, parameterGenerator.generateLocalVarDecl(context));
        assertEquals(preCallCode, parameterGenerator.generatePreCallCode(context));
        assertEquals(callArgExpr, parameterGenerator.generateCallCode(context));
        assertEquals(null, parameterGenerator.generatePostCallCode(context));
    }

    private ApiParameter createParam(String name, Class<?> type, Modifier modifier) {
        return new ApiParameter(DocMock.createParameter(name, type), modifier);
    }

}
