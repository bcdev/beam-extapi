package org.esa.beam.extapi;

import org.junit.Assert;
import org.junit.Test;

/**
 * Don't forget to set "java.library.path":
 * Windows: -Djava.library.path=.\msvc\Release
 * Unix: TODO
 *
 * @author Norman Fomferra
 */
public class CApiTest {
    /**
     * Should print to stdout:
     * <pre>
     * beam-extapi: JNI_OnLoad called
     * beam-extapi: Java_org_esa_beam_extapi_CApi_init called
     * </pre>
     */
    @Test
    public void testThatNativeInitIsCalled() {
        try {
            new CApi();
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail("ExtApi instance creation failed");
        }
    }
}
