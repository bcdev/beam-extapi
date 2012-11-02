package org.esa.beam.extapi.gen;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Norman Fomferra
 */
public class GeneratorDocletTest {

    @Test
    public void test_CodeGenCallable_PrimitiveMethod() {
        final Set<String> classNames = new HashSet<String>();
        final GeneratorDoclet.Handler handler = new GeneratorDoclet.Handler() {
            @Override
            public boolean start(RootDoc root) {
                for (ClassDoc c : root.classes()) {
                    classNames.add(c.qualifiedName());
                }
                return true;
            }
        };
        GeneratorDoclet.run(handler, "src/test/java", "org.esa.beam.extapi.gen");
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.TemplateEvalTest"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.CodeGenParameterTest"));
        assertTrue(classNames.contains("org.esa.beam.extapi.gen.CodeGenCallableTest"));
    }
}
