package org.esa.beam.extapi.gen;

import java.io.IOException;

import static org.esa.beam.extapi.gen.TemplateEval.kv;

/**
 * @author Norman Fomferra
 */
public abstract class TargetCHeaderFile extends TargetFile{

    public TargetCHeaderFile(String dirPath, String fileName) throws IOException {
        super(dirPath, fileName);
    }

    protected void writeHeader() throws IOException {
        super.writeHeader();
        writer.write(TemplateEval.eval("#ifndef ${baseName}\n" +
                                               "#define ${baseName}\n" +
                                               "\n" +
                                               "#ifdef __cplusplus\n" +
                                               "extern \"C\" {\n" +
                                               "#endif\n",
                                       kv("baseName", baseName.toUpperCase())));
        writer.write("\n");
    }

    protected void writeFooter() throws IOException {
        writer.write("\n");
        writer.write(TemplateEval.eval("#ifdef __cplusplus\n" +
                                               "} /* extern \"C\" */\n" +
                                               "#endif\n" +
                                               "#endif /* !${baseName} */",
                                       kv("baseName", baseName.toUpperCase())));
    }
}
