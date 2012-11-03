package org.esa.beam.extapi.gen;

import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.esa.beam.extapi.gen.ApiInfoTest.getApiClass;
import static org.esa.beam.extapi.gen.ApiInfoTest.getApiMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * @author Norman Fomferra
 */
public class CodeGenFactoryTest {

    public static final Class<TestClass2> TEST_CLASS_2 = TestClass2.class;
    private static ApiInfo apiInfo;

    @BeforeClass
    public static void setUp() throws Exception {
        RootDoc rootDoc = DocFactory.createRootDoc(TEST_CLASS_2);
        apiInfo = ApiInfo.create(rootDoc, new ApiInfo.DefaultFilter(TEST_CLASS_2.getName()));
    }

    @Test
    public void testIt() throws GeneratorException {
        ApiClass apiClass2 = getTestClass2();
        List<ApiMethod> methods = apiInfo.getMethodsOf(apiClass2);

        ApiMethod apiMethod = getApiMethod(methods, "getPixel", "(II)F");

        CodeGenCallable callable = CodeGenFactory.createCodeGenCallable(apiMethod);
        assertNotNull(callable);
        assertSame(apiMethod.getEnclosingClass(), callable.getEnclosingClass());
        assertEquals("TestClass2_getPixel", callable.getFunctionBaseName());
        CodeGenParameter[] parameters = callable.getParameters();
        assertEquals(2, parameters.length);
        assertEquals("int", parameters[0].getType().typeName());
        assertEquals("p1", parameters[0].getName());
        assertEquals("int", parameters[1].getType().typeName());
        assertEquals("p2", parameters[1].getName());
    }

    private ApiClass getTestClass2() {
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        ApiClass apiClass = getApiClass(apiClasses, TEST_CLASS_2.getName());
        assertNotNull(apiClass);
        return apiClass;
    }
}
