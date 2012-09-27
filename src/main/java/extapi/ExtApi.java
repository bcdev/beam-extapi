package extapi;

/**
 * This is a test class only.
 * I want to know, if we can obtain the
 * JavaVM pointer in C via JVM_OnLoad() defined in the shared library "beam-extapi".
 *
 * @author Norman Fomferra
 */
public class ExtApi {
    static {
        System.loadLibrary("beam-capi");
    }

    public ExtApi() {
        init();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    public native boolean init();

    public native boolean destroy();
}
