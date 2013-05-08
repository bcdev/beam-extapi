package org.esa.beam.extapi.gen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Norman Fomferra
 */
public abstract class TargetFile {
    protected final File file;
    protected final PrintWriter writer;
    protected final String baseName;

    public TargetFile(String dirPath, String fileName) throws IOException {
        file = new File(dirPath, fileName);
        baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        writer = new PrintWriter(new FileWriter(file));
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

}
