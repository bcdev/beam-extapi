package org.esa.beam.extapi.gen;

import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Norman Fomferra
 */
public class ApiInfoTest {

    @Test
    public void testApiInfo() {
        final ApiGeneratorConfig config = new ApiGeneratorConfigMock(TestClass2.class);

        RootDoc rootDoc = DocMock.createRootDoc(TestClass2.class);

        String testClass2Name = TestClass2.class.getName();
        ApiInfo apiInfo = ApiInfo.create(config, rootDoc);
        assertNotNull(apiInfo);

        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        assertEquals(1, apiClasses.size());

        ApiClass apiClass1 = getApiClass(apiClasses, testClass2Name);
        assertNotNull(apiClass1);

        List<ApiMethod> apiMethods = apiInfo.getMethodsOf(apiClass1);
        assertNotNull(apiMethods);
        assertEquals(7, apiMethods.size());

        testMethod(apiMethods, "<init>", "()V");
        testMethod(apiMethods, "getPixel", "(II)F");
        testMethod(apiMethods, "getPixel", "(III)F");
        testMethod(apiMethods, "getPixels", "([FI)[F");
        testMethod(apiMethods, "getName", "()Ljava/lang/String;");
        testMethod(apiMethods, "getTimestamp", "()Ljava/util/Date;");
        testMethod(apiMethods, "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");

        List<ApiConstant> apiConstants = apiInfo.getConstantsOf(apiClass1);
        assertEquals(3, apiConstants.size());
        testConstant(apiConstants, "INT16", "I");
        testConstant(apiConstants, "FLOAT32", "I");
        testConstant(apiConstants, "NOW", "Ljava/util/Date;");

        Set<ApiClass> allClasses = apiInfo.getAllClasses();
        assertEquals(4, allClasses.size());
        assertNotNull(getApiClass(allClasses, testClass2Name));
        assertNotNull(getApiClass(allClasses, String.class.getName()));
        assertNotNull(getApiClass(allClasses, Date.class.getName()));
        assertNotNull(getApiClass(allClasses, File.class.getName()));

        Set<ApiClass> usedNonApiClasses = apiInfo.getUsedNonApiClasses();
        assertEquals(3, usedNonApiClasses.size());
        assertNotNull(getApiClass(usedNonApiClasses, String.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, Date.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, File.class.getName()));

        List<ApiMethod> methodsUsingString = apiInfo.getMethodsUsing(getApiClass(usedNonApiClasses, String.class.getName()));
        assertNotNull(methodsUsingString);
        assertEquals(2, methodsUsingString.size());
        testMethod(methodsUsingString, "getName", "()Ljava/lang/String;");
        testMethod(methodsUsingString, "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");
    }

    private void testConstant(List<ApiConstant> apiMethods, String javaName, String javaSignature) {
        ApiConstant apiConstant = getApiConstant(apiMethods, javaName, javaSignature);
        assertNotNull("Not found: " + javaName, apiConstant);
    }

    private void testMethod(List<ApiMethod> apiMethods, String javaName, String javaSignature) {
        ApiMethod apiMethod = getApiMethod(apiMethods, javaName, javaSignature);
        assertNotNull("Not found: " + javaName + javaSignature, apiMethod);
    }


    public static ApiClass getApiClass(Set<ApiClass> set, String javaName) {
        for (ApiClass elem : set) {
            if (javaName.equals(elem.getJavaName())) {
                return elem;
            }
        }
        return null;
    }

    public static ApiConstant getApiConstant(List<ApiConstant> list, String javaName, String javaSignature) {
        for (ApiConstant elem : list) {
            if (javaName.equals(elem.getJavaName())
                    && javaSignature.equals(elem.getJavaSignature())) {
                return elem;
            }
        }
        return null;
    }

    public static ApiMethod getApiMethod(List<ApiMethod> list, String javaName, String javaSignature) {
        for (ApiMethod elem : list) {
            if (javaName.equals(elem.getJavaName())
                    && javaSignature.equals(elem.getJavaSignature())) {
                return elem;
            }
        }
        return null;
    }

}
