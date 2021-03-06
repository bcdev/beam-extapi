package org.esa.beam.extapi.gen;

import org.junit.Test;

import static org.esa.beam.extapi.gen.TemplateEval.eval;
import static org.esa.beam.extapi.gen.TemplateEval.kv;
import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class TemplateEvalTest {
    @Test
    public void testStatic() {
        assertEquals("oha", eval("${name}", kv("name", "oha")));
        assertEquals("oha, oha!", eval("${name}, ${name}!", kv("name", "oha")));
        assertEquals("Bibo is now 50", eval("${name} is now ${x}", kv("name", "Bibo"), kv("x", 50)));
        assertEquals("Bibo is now 50", eval("${name} is now ${x}", kv("name", "Bibo"), kv("x", 50)));
    }

    @Test
    public void testObject() {
        TemplateEval templateEval = new TemplateEval(kv("name", "Bibo"));
        templateEval.add(kv("x", 50));
        assertEquals("Bibo is now 50", templateEval.eval("${name} is now ${x}"));
    }
}
