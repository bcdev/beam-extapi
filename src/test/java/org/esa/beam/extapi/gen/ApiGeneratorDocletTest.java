package org.esa.beam.extapi.gen;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.esa.beam.extapi.gen.test.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Norman Fomferra
 */
public class ApiGeneratorDocletTest {

    @Test
    public void testThatAllPublicClassesAreFound() {
        final Set<String> classNames = new HashSet<String>();
        final ApiGeneratorDoclet.Handler handler = new ApiGeneratorDoclet.Handler() {
            @Override
            public boolean start(RootDoc root) {
                for (ClassDoc c : root.classes()) {
                    String name = c.qualifiedName();
                    classNames.add(name);
                }
                return true;
            }
        };


        ApiGeneratorDoclet.run(handler, "src/test/java", "org.esa.beam.extapi.gen.test");
        assertEquals(5, classNames.size());
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestClass1"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestClass2"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestClass3"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestEnum1"));
        // In Javadoc, nested / inner classes use '.'  as namespace separators instead of the correct '$'
        assertFalse(classNames.contains("org.esa.beam.extapi.gen.test.TestClass2$Geom"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestClass2.Geom"));
    }
}
