package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;
import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.Date;

/**
 * @author Norman Fomferra
 */
public class DocMockTest {

    @Test
    public void testTopLevelClass() {
        Type type = DocMock.createType(Date.class);
        Assert.assertEquals("Date", type.typeName());
        Assert.assertEquals("Date", type.simpleTypeName());
        Assert.assertEquals("java.util.Date", type.qualifiedTypeName());
    }

    @Test
    public void testNestedClass() {
        Type type = DocMock.createType(Point2D.Double.class);
        Assert.assertEquals("Point2D.Double", type.typeName());
        Assert.assertEquals("Double", type.simpleTypeName());
        Assert.assertEquals("java.awt.geom.Point2D.Double", type.qualifiedTypeName());
    }
}
