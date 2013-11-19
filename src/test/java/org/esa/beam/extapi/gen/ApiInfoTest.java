package org.esa.beam.extapi.gen;

import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.junit.Test;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Norman Fomferra
 */
public class ApiInfoTest {
    // Not really a test: NF wants to know how generics are represented in the Reflection API.
    @Test
    public void testClass2() throws Exception {

        Class<TestClass2> c = TestClass2.class;

        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            System.out.println();
            System.out.println("method.name = " + name);

            Class returnType = method.getReturnType();
            System.out.println("method.returnType = " + returnType);

            Class[] parameterTypes = method.getParameterTypes();
            for (Class parameterType : parameterTypes) {
                System.out.println("method.parameterType = " + parameterType);
            }

            TypeVariable<Method>[] typeParameters = method.getTypeParameters();
            if (typeParameters.length > 0) {
                for (TypeVariable<Method> typeParameter : typeParameters) {
                    String name1 = typeParameter.getName();
                    System.out.println("method.typeParameter.name = " + name1);
                    Type[] bounds = typeParameter.getBounds();
                    for (Type bound : bounds) {
                        System.out.println("method.typeParameter.bound = " + bound);
                    }
                }

                Type genericReturnType = method.getGenericReturnType();
                System.out.println("method.genericReturnType = " + genericReturnType);

                Type[] genericParameterTypes = method.getGenericParameterTypes();
                for (Type genericParameterType : genericParameterTypes) {
                    System.out.println("method.genericParameterType = " + genericParameterType);
                }
            }
        }

    }

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
        assertEquals(12, apiMethods.size());

        testMethod(apiMethods, "<init>", "()V");
        testMethod(apiMethods, "getPixel", "(II)F");
        testMethod(apiMethods, "getPixel", "(III)F");
        testMethod(apiMethods, "getPixels", "([FI)[F");
        testMethod(apiMethods, "getPixelsWithResultParam", "([FI)[F");
        testMethod(apiMethods, "getName", "()Ljava/lang/String;");
        testMethod(apiMethods, "getTimestamp", "()Ljava/util/Date;");
        testMethod(apiMethods, "getDuration", "()J");
        testMethod(apiMethods, "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");
        testMethod(apiMethods, "getPixelsForType", "(Ljava/lang/Class;)Ljava/lang/Object;");
        testMethod(apiMethods, "getPixelsForRect", "(Ljava/awt/geom/Rectangle2D;)[F");
        testMethod(apiMethods, "transformCoordinates", "([D)[D");

        List<ApiConstant> apiConstants = apiInfo.getConstantsOf(apiClass1);
        assertEquals(3, apiConstants.size());
        testConstant(apiConstants, "INT16", "I");
        testConstant(apiConstants, "FLOAT32", "I");
        testConstant(apiConstants, "NOW", "Ljava/util/Date;");

        Set<ApiClass> allClasses = apiInfo.getAllClasses();
        assertEquals(7, allClasses.size());
        assertNotNull(getApiClass(allClasses, testClass2Name));
        assertNotNull(getApiClass(allClasses, String.class.getName()));
        assertNotNull(getApiClass(allClasses, Date.class.getName()));
        assertNotNull(getApiClass(allClasses, File.class.getName()));

        Set<ApiClass> usedNonApiClasses = apiInfo.getUsedNonApiClasses();
        assertEquals(6, usedNonApiClasses.size());
        assertNotNull(getApiClass(usedNonApiClasses, String.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, Date.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, File.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, Rectangle2D.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, Class.class.getName()));
        assertNotNull(getApiClass(usedNonApiClasses, Object.class.getName()));

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

    private ApiMethod testMethod(List<ApiMethod> apiMethods, String javaName, String javaSignature) {
        ApiMethod apiMethod = getApiMethod(apiMethods, javaName, javaSignature);
        assertNotNull("Not found: " + javaName + javaSignature, apiMethod);
        return apiMethod;
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
