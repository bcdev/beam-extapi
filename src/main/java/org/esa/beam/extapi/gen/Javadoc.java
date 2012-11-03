package org.esa.beam.extapi.gen;

/**
 * Calls the {@code javadoc} tool.
 * @author Norman Fomferra
 */
public class Javadoc {

    public static void run(String docletClass, String sourcePath, String ... packages) {
        int off = 4;
        String[] javadocArgs = new String[off + packages.length];
        javadocArgs[0] = "-doclet";
        javadocArgs[1] = docletClass;
        javadocArgs[2] = "-sourcepath";
        javadocArgs[3] = sourcePath;
        System.arraycopy(packages, 0, javadocArgs, off, packages.length);
        com.sun.tools.javadoc.Main.execute(javadocArgs);
    }
}
