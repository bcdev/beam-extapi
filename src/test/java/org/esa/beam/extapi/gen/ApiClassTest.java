package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.test.TestClass2;
import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Date;

/**
 * @author Norman Fomferra
 */
public class ApiClassTest {

    @Test(expected = IllegalArgumentException.class)
    public void testThatArgMustNotBePrimitive() {
        new ApiClass(DocMock.createType(Integer.TYPE));
    }

    @Test
    public void testTopLevelClass() {
        ApiClass apiClass = new ApiClass(DocMock.createType(Date.class));
        Assert.assertEquals("java.util.Date", apiClass.getJavaName());
        Assert.assertEquals("java/util/Date", apiClass.getResourceName());
        Assert.assertEquals("java.util.Date", apiClass.toString());
    }

    @Test
    public void testNestedClass() {
        Type type = DocMock.createType(Point2D.Double.class);
        ApiClass apiClass = new ApiClass(type);
        Assert.assertEquals("java.awt.geom.Point2D$Double", apiClass.getJavaName());
        Assert.assertEquals("java/awt/geom/Point2D$Double", apiClass.getResourceName());
    }

    @Test
    public void testAnotherNestedClass() {
        Type type = DocMock.createType(TestClass2.Geom.class);
        ApiClass apiClass = new ApiClass(type);
        Assert.assertEquals("org.esa.beam.extapi.gen.test.TestClass2$Geom", apiClass.getJavaName());
        Assert.assertEquals("org/esa/beam/extapi/gen/test/TestClass2$Geom", apiClass.getResourceName());
    }
}
