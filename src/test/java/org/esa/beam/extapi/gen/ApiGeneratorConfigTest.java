package org.esa.beam.extapi.gen;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author Norman Fomferra
 */
public class ApiGeneratorConfigTest {

    private ApiGeneratorConfig config;

    @Before
    public void setUp() throws Exception {
        config = ApiGeneratorConfigImpl.load();
        assertNotNull(config);
    }

    @Test
    public void testGetParameterModifiers() {
        ApiParameter.Modifier[] modifiers1 = config.getParameterModifiers("org.esa.beam.framework.datamodel.Band", "writePixels", "(IIII[F)V");
        assertNull(modifiers1);

        ApiParameter.Modifier[] modifiers2 = config.getParameterModifiers("org.esa.beam.framework.datamodel.Band", "readPixels", "(IIII[F)[F");
        assertNotNull(modifiers2);
        assertEquals(5, modifiers2.length);
        assertEquals(ApiParameter.Modifier.IN, modifiers2[0]);
        assertEquals(ApiParameter.Modifier.IN, modifiers2[0]);
        assertEquals(ApiParameter.Modifier.IN, modifiers2[1]);
        assertEquals(ApiParameter.Modifier.IN, modifiers2[2]);
        assertEquals(ApiParameter.Modifier.IN, modifiers2[3]);
        assertEquals(ApiParameter.Modifier.RETURN, modifiers2[4]);
    }

    @Test
    public void testGetMethodCName() {
        assertEquals("getName", config.getMethodCName("org.esa.beam.framework.datamodel.Band", "getName", "()Ljava/lang/String;"));
        assertEquals("writePixelsInt", config.getMethodCName("org.esa.beam.framework.datamodel.Band", "writePixels", "(IIII[I)V"));
        assertEquals("readPixelsFloat", config.getMethodCName("org.esa.beam.framework.datamodel.Band", "readPixels", "(IIII[F)[F"));
    }

}
