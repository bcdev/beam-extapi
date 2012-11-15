package org.esa.beam.extapi.gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Norman Fomferra
 */
public class TemplateEval {

    private final Map<String, Object> pairs;

    public TemplateEval(KV... pairs) {
        this.pairs = new HashMap<String, Object>();
        for (KV pair : pairs) {
            this.pairs.put(pair.key, pair.value);
        }
    }

    private TemplateEval(Map<String, Object> pairs) {
        this.pairs = new HashMap<String, Object>(pairs);
    }

    public static TemplateEval create(Map<String, Object> map) {
        return new TemplateEval(map);
    }

    public static TemplateEval create(KV... pairs) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (KV pair : pairs) {
            map.put(pair.key, pair.value);
        }
        return create(map);
    }

    public TemplateEval add(String key, Object value) {
        this.pairs.put(key, value);
        return this;
    }

    public TemplateEval add(KV... pairs) {
        for (KV pair : pairs) {
            this.pairs.put(pair.key, pair.value);
        }
        return this;
    }

    public String eval(String pattern) {
        String text = pattern;
        for (Map.Entry<String, Object> pair : pairs.entrySet()) {
            text = evalKV(text, pair.getKey(), pair.getValue());
        }
        return text;
    }

    public TemplateEval eval(Reader reader, Writer writer) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            writer.write(eval(line));
            writer.write("\n");
        }
        return this;
    }

    public static String eval(String pattern, KV... pairs) {
        String text = pattern;
        for (KV pair : pairs) {
            final String key = pair.key;
            final Object value = pair.value;
            text = evalKV(text, key, value);
        }
        return text;
    }

    private static String evalKV(String text, String key, Object value) {
        if (value != null) {
            text = text.replace("${" + key + "}", value.toString());
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
