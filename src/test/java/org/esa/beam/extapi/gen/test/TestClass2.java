package org.esa.beam.extapi.gen.test;

import org.junit.Ignore;

/**
 * @author Norman Fomferra
 */
@Ignore
public class TestClass2 extends TestClass1 {
    public float getPixel(int x, int y) {
        return 0;
    }

    public float[] getPixels(float[] pixels, int size) {
        return null;
    }
}
