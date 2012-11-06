package org.esa.beam.extapi.gen;

import org.junit.Test;

/**
 * @author Norman Fomferra
 */
public class ApiRuleTest {
    @Test
    public void testParse() {

    }

    public static class ApiRule {
        private final ApiClass enclosingClass;
        private final String javaName;
        private final String javaSignature;

        public ApiRule(ApiClass enclosingClass, String javaName, String javaSignature) {
            this.enclosingClass = enclosingClass;
            this.javaName = javaName;
            this.javaSignature = javaSignature;
        }
    }

}
