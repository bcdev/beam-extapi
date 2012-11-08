package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.*;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.esa.beam.extapi.gen.ApiInfoTest.getApiClass;
import static org.esa.beam.extapi.gen.ApiInfoTest.getApiMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Norman Fomferra
 */
public class GeneratorFactoryTest {

    public static final Class<TestClass2> TEST_CLASS_2 = TestClass2.class;
    private static ApiInfo apiInfo;

    @BeforeClass
    public static void setUp() throws Exception {
        ApiGeneratorConfig config = mock(ApiGeneratorConfig.class);
        when(config.isApiClass(TEST_CLASS_2.getName())).thenReturn(true);
        RootDoc rootDoc = DocMock.createRootDoc(TEST_CLASS_2);
        apiInfo = ApiInfo.create(config, rootDoc);
    }

    @Test
    public void testIt() throws GeneratorException {
        ApiClass apiClass2 = getTestClass2();
        List<ApiMethod> methods = apiInfo.getMethodsOf(apiClass2);

        ApiMethod apiMethod = getApiMethod(methods, "getPixel", "(II)F");

        FunctionGenerator functionGenerator = GeneratorFactory.createFunctionGenerator(apiMethod);
        assertNotNull(functionGenerator);
        assertSame(apiMethod.getEnclosingClass(), functionGenerator.getEnclosingClass());
        ParameterGenerator[] parameterGenerators = functionGenerator.getParameterGenerators();
        assertEquals(2, parameterGenerators.length);
        assertEquals("int", parameterGenerators[0].getType().typeName());
        assertEquals("p1", parameterGenerators[0].getName());
        assertEquals("int", parameterGenerators[1].getType().typeName());
        assertEquals("p2", parameterGenerators[1].getName());
    }

    private ApiClass getTestClass2() {
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        ApiClass apiClass = getApiClass(apiClasses, TEST_CLASS_2.getName());
        assertNotNull(apiClass);
        return apiClass;
    }
}
