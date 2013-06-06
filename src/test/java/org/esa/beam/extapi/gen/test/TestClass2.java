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
     * For testing overloading (getPixel with 2 params).
     */
    public float getPixel(int x, int y) {
        return 0;
    }

    /*
     * For testing overloading (getPixel with 3 params).
     */
    public float getPixel(int x, int y, int b) {
        return 0;
    }

    /*
     * For testing primitive arrays (float[]).
     */
    public float[] getPixels(float[] pixels, int size) {
        return null;
    }

    /*
     * For testing plain type variable T.
     */
    public <T> T getPixelsForType(Class<T> type) {
        return null;
    }

    /*
     * For testing bound type variable T (extends Rectangle2D).
     */
    public <T extends Rectangle2D> float[] getPixelsForRect(T rect) {
        return null;
    }

    /*
     * For testing AWT class Date.
     */
    @Override
    public Date getTimestamp() {
        return null;
    }
}
