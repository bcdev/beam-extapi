package org.esa.beam.extapi.gen;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class ApiMethodTest {

    @Test
    public void testNestedMethod() {
        ClassDoc classDoc = DocMock.createClassDoc(TestClass2.Geom.class);
        MethodDoc[] methods = classDoc.methods();
        assertEquals(1, methods.length);
        ApiClass apiClass = new ApiClass(classDoc);
        ApiMethod apiMethod = new ApiMethod(apiClass, methods[0]);

        assertEquals("transform", apiMethod.getJavaName());
        assertEquals("(Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;)Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;", apiMethod.getJavaSignature());
    }

}
