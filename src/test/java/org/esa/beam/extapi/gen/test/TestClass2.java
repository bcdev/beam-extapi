package org.esa.beam.extapi.gen.test;

import org.junit.Ignore;

import java.util.Date;

/**
 * @author Norman Fomferra
 */
@Ignore
public class TestClass2 extends TestClass1 {
    public float getPixel(int x, int y) {
        return 0;
    }

    public float getPixel(int x, int y, int b) {
        return 0;
    }

    public float[] getPixels(float[] pixels, int size) {
        return null;
    }

    @Override
    public Date getTimestamp() {
        return null;
    }
}
