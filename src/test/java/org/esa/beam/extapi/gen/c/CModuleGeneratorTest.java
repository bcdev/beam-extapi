package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.*;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.esa.beam.util.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Norman Fomferra
 */
public class CModuleGeneratorTest {
    public static final Class<TestClass2> TEST_CLASS_2 = TestClass2.class;
    private static ApiInfo apiInfo;
    private CModuleGenerator moduleGenerator;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ApiGeneratorConfig config = new ApiGeneratorConfigMock(TEST_CLASS_2);
        RootDoc rootDoc = DocMock.createRootDoc(TEST_CLASS_2);
        apiInfo = ApiInfo.create(config, rootDoc);
    }

    @Before
    public void before() throws Exception {
        moduleGenerator = new CModuleGenerator(apiInfo);
    }

    @Test
    public void testFunctionGeneratorsForTestClass2() throws Exception {
        ApiClass apiClass = findApiClass(TEST_CLASS_2.getName());

        testFunctionGenerator(apiClass, "TestClass2_init.c", "<init>", "()V");
        testFunctionGenerator(apiClass, "TestClass2_getPixel1.c", "getPixel", "(II)F");
        testFunctionGenerator(apiClass, "TestClass2_getPixel2.c", "getPixel", "(III)F");
        testFunctionGenerator(apiClass, "TestClass2_getTimestamp.c", "getTimestamp", "()Ljava/util/Date;");
        testFunctionGenerator(apiClass, "TestClass2_getPixels.c", "getPixels", "([FI)[F");
        testFunctionGenerator(apiClass, "TestClass2_getName.c", "getName", "()Ljava/lang/String;");
        testFunctionGenerator(apiClass, "TestClass2_getFiles.c", "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");
        testFunctionGenerator(apiClass, "TestClass2_getPixelsForType.c", "getPixelsForType", "(Ljava/lang/Class;)Ljava/lang/Object;");
        testFunctionGenerator(apiClass, "TestClass2_getPixelsForRect.c", "getPixelsForRect", "(Ljava/awt/geom/Rectangle2D;)[F");

        dumpAllMethods(apiClass);
    }

    private void testFunctionGenerator(ApiClass apiClass, String resourceName, String name, String sig) throws IOException {
        FunctionGenerator fg1 = findFunctionGenerator(apiClass, name, sig);
        assertNotNull(fg1);
        assertEquals(loadTextResource(resourceName), generateFunctionDefinition(fg1));
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
            String javaName = functionGenerator.getApiMethod().getJavaName();
            String javaSignature = functionGenerator.getApiMethod().getJavaSignature();
            System.out.println("\nMethod " + javaName + " " + javaSignature + "\n");
            System.out.println(generateFunctionDefinition(functionGenerator));
        }
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
