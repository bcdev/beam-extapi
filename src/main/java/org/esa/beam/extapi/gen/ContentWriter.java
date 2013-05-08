package org.esa.beam.extapi.gen;

import java.io.IOException;
import java.io.PrintWriter;

/**
* @author Norman Fomferra
*/
public interface ContentWriter {
    void writeContent(PrintWriter writer) throws IOException;
}
