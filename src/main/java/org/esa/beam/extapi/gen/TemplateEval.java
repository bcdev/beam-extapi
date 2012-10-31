package org.esa.beam.extapi.gen;

/**
* @author Norman Fomferra
*/
public class TemplateEval {

    public static String eval(String pattern, KV... pairs) {
        String text = pattern;
        for (KV pair : pairs) {
            if (pair.value != null) {
                text = text.replace("${" + pair.key + "}", pair.value.toString());
            }
        }
        return text;
    }

    public static KV kv(String key, Object value) {
        return new KV(key, value);
    }

    public static class KV {
        public final String key;
        public final Object value;

        public KV(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
