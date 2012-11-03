package org.esa.beam.extapi.gen;

import com.sun.javadoc.RootDoc;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class ApiInfoTest {

    private ApiInfo apiInfo;

    @Before
    public void setUp() throws Exception {
        final GeneratorDoclet.Handler handler = new GeneratorDoclet.Handler() {
            @Override
            public boolean start(RootDoc root) {
                apiInfo = ApiInfo.create(root, new ApiInfo.DefaultFilter(ApiInfoTest.class.getName()));
                return true;
            }
        };
        GeneratorDoclet.run(handler, "src/test/java", "org.esa.beam.extapi.gen");
    }

    @Test
    public void testApiInfo() {
        Set<ApiClass> apiClasses = apiInfo.getApiClasses();
        assertEquals(1, apiClasses.size());
    }
}
