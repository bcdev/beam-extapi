package org.esa.beam.extapi;

/**
 * This is a test class only.
 * I want to know, if we can obtain the
 * JavaVM pointer in C via JVM_OnLoad() defined in the shared library "beam-capi".
 *
 * @author Norman Fomferra
 */
public class CApi {
    static {
        System.loadLibrary("beam-capi");
    }

    public CApi() {
        init();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    /**
     * This will set the JNI instance in "beam-capi".
     * @return true, if ok
     */
    private native boolean init();

    /**
     * This will clear the JNI instance in "beam-capi".
     */
    private native void destroy();
}
