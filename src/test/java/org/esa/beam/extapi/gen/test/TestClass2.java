package org.esa.beam.extapi.gen.test;

import org.junit.Ignore;

import java.awt.geom.Rectangle2D;
import java.util.Date;

/**
 * @author Norman Fomferra
 */
@Ignore
public class TestClass2 extends TestClass1 {
    public static final Date NOW = new Date();
    private static final Date DAY_ONE = new Date(0);

    /*
     * For testing code generation for overloaded methods (getPixel with 2 params).
     */
    public float getPixel(int x, int y) {
        return 0;
    }

    /*
     * For testing code generation for overloaded methods (getPixel with 3 params).
     */
    public float getPixel(int x, int y, int b) {
        return 0;
    }

    /*
     * For testing code generation for primitive arrays return value and parameter (float[]).
     */
    public float[] getPixels(float[] pixels, int size) {
        return null;
    }

    /*
     * For testing code generation for primitive array parameter that acts as return value (float[]).
     */
    public float[] getPixelsWithResultParam(float[] pixels, int size) {
        return null;
    }

    /*
     * For testing code generation for parameters that are a plain type variable T.
     */
    public <T> T getPixelsForType(Class<T> type) {
        return null;
    }

    /*
     * For testing code generation for bound type variable T (extends Rectangle2D).
     */
    public <T extends Rectangle2D> float[] getPixelsForRect(T rect) {
        return null;
    }

    /*
     * For testing code generation for a returned Java object (java.util.Date).
     */
    @Override
    public Date getTimestamp() {
        return null;
    }

    /*
     * For testing code generation for a returned Java long value.
     */
    public long getDuration() {
        return 0;
    }

    /*
     * For testing inner classes.
     */
    public static class Geom {
        public static Geom transform(Geom g) {
            return null;
        }
    }
}
