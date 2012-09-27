package org.esa.beam.extapi;

/**
 * This is a test class only.
 * I want to know, if we can obtain the
 * JavaVM pointer in C via JVM_OnLoad() defined in the shared library "beam-pyapi".
 *
 * @author Norman Fomferra
 */
public class PythonApi {

    private final CApi cApi;


    static {
        System.loadLibrary("beam-pyapi");
    }

    public PythonApi() {
        cApi = new CApi();
        init();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    /**
     * This will create the Python VM in shared library "beam-pyapi".
     * @return true, if ok
     */
    public native boolean init();

    /**
     * This will destroy the Python VM in shared library "beam-pyapi".
     */
    public native void destroy();
}
