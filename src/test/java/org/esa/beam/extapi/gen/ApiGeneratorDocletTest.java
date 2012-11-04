package org.esa.beam.extapi.gen;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Norman Fomferra
 */
public class ApiGeneratorDocletTest {

    @Test
    public void test_CodeGenCallable_PrimitiveMethod() {
        final Set<String> classNames = new HashSet<String>();
        final ApiGeneratorDoclet.Handler handler = new ApiGeneratorDoclet.Handler() {
            @Override
            public boolean start(RootDoc root) {
                for (ClassDoc c : root.classes()) {
                    classNames.add(c.qualifiedName());
                }
                return true;
            }
        };
        ApiGeneratorDoclet.run(handler, "src/test/java", "org.esa.beam.extapi.gen.test");
        assertEquals(2, classNames.size());
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestClass1"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.test.TestClass2"));
    }
}
