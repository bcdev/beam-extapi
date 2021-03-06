package org.esa.beam.extapi.gen;

import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.esa.beam.util.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Norman Fomferra
 */
public abstract class ModuleGeneratorTest {
    public static final Class<TestClass2> TEST_CLASS_2 = TestClass2.class;
    public static ApiInfo apiInfo;
    private ModuleGenerator moduleGenerator;
    private ApiClass testClass2;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ApiGeneratorConfigMock config = new ApiGeneratorConfigMock(TEST_CLASS_2);
        config.addModifiers("getPixelsWithResultParam", "([FI)[F", new ApiParameter.Modifier[]{
                ApiParameter.Modifier.RETURN,
                ApiParameter.Modifier.IN
        });
        RootDoc rootDoc = DocMock.createRootDoc(TEST_CLASS_2);
        apiInfo = ApiInfo.create(config, rootDoc);
    }

    @Before
    public void before() throws Exception {
        testClass2 = findApiClass(TEST_CLASS_2.getName());
        moduleGenerator = createModuleGenerator();
    }

    protected abstract ModuleGenerator createModuleGenerator();

    @Test
    public void testFunctionGenerators_TestClass2_init() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_init.c", "<init>", "()V");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getName() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getName.c", "getName", "()Ljava/lang/String;");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getFiles() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getFiles.c", "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getPixel1() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getPixel1.c", "getPixel", "(II)F");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getPixel2() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getPixel2.c", "getPixel", "(III)F");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getTimestamp() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getTimestamp.c", "getTimestamp", "()Ljava/util/Date;");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getDuration() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getDuration.c", "getDuration", "()J");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getPixels() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getPixels.c", "getPixels", "([FI)[F");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getPixelsWithResultParam() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getPixelsWithResultParam.c", "getPixelsWithResultParam", "([FI)[F");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getPixelsForType() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getPixelsForType.c", "getPixelsForType", "(Ljava/lang/Class;)Ljava/lang/Object;");
    }

    @Test
    public void testFunctionGenerators_TestClass2_getPixelsForRect() throws Exception {
        testFunctionGenerator(testClass2, "TestClass2_getPixelsForRect.c", "getPixelsForRect", "(Ljava/awt/geom/Rectangle2D;)[F");
    }

    private void testFunctionGenerator(ApiClass apiClass, String resourceName, String name, String sig) throws IOException {
        FunctionGenerator generator = findFunctionGenerator(apiClass, name, sig);
        assertNotNull(generator);
        dumpGeneratedFunction(generator);
        assertEquals(loadTextResource(resourceName), generateFunctionDefinition(generator));
    }

    private String loadTextResource(String resourceName) {
        try {
            InputStream stream = getClass().getResourceAsStream(resourceName);
            if (stream == null) {
                throw new FileNotFoundException(resourceName);
            }
            InputStreamReader reader = new InputStreamReader(stream);
            try {
                return FileUtils.readText(reader);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void dumpAllMethods(ApiClass apiClass) throws IOException {
        List<FunctionGenerator> functionGenerators = moduleGenerator.getFunctionGenerators(apiClass);
        for (FunctionGenerator functionGenerator : functionGenerators) {
            dumpGeneratedFunction(functionGenerator);
        }
    }

    private void dumpGeneratedFunction(FunctionGenerator functionGenerator) throws IOException {
        final String className = functionGenerator.getApiMethod().getEnclosingClass().getType().simpleTypeName();
        String javaName = functionGenerator.getApiMethod().getJavaName();
        String javaSignature = functionGenerator.getApiMethod().getJavaSignature();
        System.out.println("\n//////// " + className + "." + javaName + " " + javaSignature + " ////////\n");
        System.out.println(generateFunctionDefinition(functionGenerator));
    }

    private String generateFunctionDefinition(FunctionGenerator functionGenerator) throws IOException {
        StringWriter writer = new StringWriter();
        final FunctionWriter functionWriter = new FunctionWriter(moduleGenerator, new PrintWriter(writer));
        functionWriter.writeFunctionDefinition(functionGenerator);
        return writer.toString();
    }


    public static ApiClass findApiClass(String name) {
        Set<ApiClass> apiClasses1 = apiInfo.getApiClasses();
        for (ApiClass apiClass : apiClasses1) {
            if (apiClass.getJavaName().equals(name)) {
                return apiClass;
            }
        }
        return null;
    }

    public FunctionGenerator findFunctionGenerator(ApiClass apiClass, String name, String sig) {
        ApiMethod method = findApiMethod(apiClass, name, sig);
        List<FunctionGenerator> functionGenerators = moduleGenerator.getFunctionGenerators(apiClass);
        for (FunctionGenerator functionGenerator : functionGenerators) {
            if (functionGenerator.getApiMethod() == method) {
                return functionGenerator;
            }
        }
        return null;
    }


    public static ApiMethod findApiMethod(ApiClass apiClass, String name, String sig) {
        List<ApiMethod> apiMethods = apiInfo.getMethodsOf(apiClass);
        for (ApiMethod apiMethod : apiMethods) {
            if (apiMethod.getJavaName().equals(name)
                    && apiMethod.getJavaSignature().equals(sig)) {
                return apiMethod;
            }
        }
        return null;
    }
}
