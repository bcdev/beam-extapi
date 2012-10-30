package org.esa.beam.extapi.gen;

import org.esa.beam.framework.datamodel.Band;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;

/**
 * @author Norman Fomferra
 */
public class CodeGenParameterTest {

    private MyCodeGenContext context;

    @Before
    public void setUp() throws Exception {
        context = new MyCodeGenContext();
    }

    @Test
    public void testGenerateCallArgExpr() {
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Boolean.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Byte.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Character.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Short.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Integer.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Long.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Float.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("x", new CodeGenParameter.PrimitiveScalar(new MyParameter("x", Double.TYPE)).generateCallArgExpr(context));
        Assert.assertEquals("nameString", new CodeGenParameter.StringScalar(new MyParameter("name", String.class)).generateCallArgExpr(context));
        Assert.assertEquals("file", new CodeGenParameter.ObjectScalar(new MyParameter("file", File.class)).generateCallArgExpr(context));
        Assert.assertEquals("band", new CodeGenParameter.ObjectScalar(new MyParameter("band", Band.class)).generateCallArgExpr(context));
        Assert.assertEquals("point", new CodeGenParameter.ObjectScalar(new MyParameter("point", Point2D.Double.class)).generateCallArgExpr(context));
    }
}
