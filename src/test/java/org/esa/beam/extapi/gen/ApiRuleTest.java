package org.esa.beam.extapi.gen;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Norman Fomferra
 */
public class ApiRuleTest {
    @Test
    public void testParse() {
        ApiRule apiRule = ApiRule.parse("org.esa.beam.extapi.gen.test.TestClass3#readPixels(IIII[F)[F", "iiiir");
        assertEquals("org.esa.beam.extapi.gen.test.TestClass3", apiRule.getClassName());
        assertEquals("readPixels", apiRule.getMethodName());
        assertEquals("(IIII[F)[F", apiRule.getMethodSignature());
        assertEquals(5, apiRule.getModifiers().length);
        assertEquals(ApiRule.Modifier.IN, apiRule.getModifiers()[0]);
        assertEquals(ApiRule.Modifier.IN, apiRule.getModifiers()[1]);
        assertEquals(ApiRule.Modifier.IN, apiRule.getModifiers()[2]);
        assertEquals(ApiRule.Modifier.IN, apiRule.getModifiers()[3]);
        assertEquals(ApiRule.Modifier.RETURN, apiRule.getModifiers()[4]);
    }


    public static class ApiRule {

        enum Modifier {
            IN,
            OUT,
            RETURN
        }

        private final String className;
        private final String methodName;
        private final String methodSignature;
        private final Modifier[] modifiers;

        public static ApiRule parse(String key, String value) {
            int pos1 = key.indexOf('#');
            if (pos1 <= 0) {
                throw new IllegalArgumentException("'#' expected");
            }
            int pos2 = key.indexOf('(', pos1);
            if (pos2 <= 0) {
                throw new IllegalArgumentException("'(' expected after '#'");
            }
            String className = key.substring(0, pos1);
            String methodName = key.substring(pos1 + 1, pos2);
            String methodSignature = key.substring(pos2);
            Modifier[] modifiers = new Modifier[value.length()];
            for (int i = 0; i < modifiers.length; i++) {
                Modifier modifier;
                char c = value.charAt(i);
                if (c == 'i') {
                    modifier = Modifier.IN;
                } else if (c == 'o') {
                    modifier = Modifier.OUT;
                } else if (c == 'r') {
                    modifier = Modifier.RETURN;
                } else {
                    throw new IllegalArgumentException("unknown modifier: " + c);
                }
                modifiers[i] = modifier;
            }
            return new ApiRule(className, methodName, methodSignature, modifiers);
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getMethodSignature() {
            return methodSignature;
        }

        public Modifier[] getModifiers() {
            return modifiers;
        }

        private ApiRule(String className, String methodName, String methodSignature, Modifier[] modifiers) {
            this.className = className;
            this.methodName = methodName;
            this.methodSignature = methodSignature;
            this.modifiers = modifiers;
        }
    }

}
