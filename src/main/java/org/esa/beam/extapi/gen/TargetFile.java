package org.esa.beam.extapi.gen;

import java.io.*;

/**
 * @author Norman Fomferra
 */
public abstract class TargetFile {
    protected final File file;
    protected final PrintWriter writer;
    protected final String baseName;
    protected final TemplateEval templateEval;

    public TargetFile(String dirPath, String fileName) throws IOException {
        file = new File(dirPath, fileName);
        baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        writer = new PrintWriter(new FileWriter(file));
        templateEval = new TemplateEval();
    }

    public void create() throws IOException {
        try {
            writeHeader();
            writeContent();
            writeFooter();
        } finally {
            writer.close();
        }
    }

    protected abstract void writeContent() throws IOException;

    protected void writeHeader() throws IOException {}

    protected void writeFooter() throws IOException {}

    protected void writeResource(String resourceName, TemplateEval.KV... pairs) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceName)));
        try {
            templateEval.add(pairs).eval(bufferedReader, writer);
        } finally {
            bufferedReader.close();
        }
    }

    protected void writeText(String text, TemplateEval.KV... pairs) throws IOException {
        writer.print(templateEval.add(pairs).eval(text));
    }
}
