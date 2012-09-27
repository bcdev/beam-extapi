package extapi;

import org.junit.Assert;
import org.junit.Test;

/**
 * Don't forget to set "java.library.path":
 * Windows: -Djava.library.path=.\msvc\Release
 * Unix: TODO
 *
 * @author Norman Fomferra
 */
public class ExtApiTest {
    /**
     * Should print to stdout:
     * <pre>
     * beam-extapi: JNI_OnLoad called
     * beam-extapi: Java_extapi_ExtApi_init called
     * </pre>
     */
    @Test
    public void testThatNativeInitIsCalled() {
        try {
            new ExtApi();
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail("ExtApi instance creation failed");
        }
    }
}
