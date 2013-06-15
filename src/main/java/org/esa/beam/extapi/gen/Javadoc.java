package org.esa.beam.extapi.gen;

/**
 * Calls the {@code javadoc} tool.
 *
 * @author Norman Fomferra
 */
public class Javadoc {

    public static void run(String docletClass, String sourcePath, String... packages) {
        String[] javadocArgs = join(join("-doclet", docletClass,
                                         "-sourcepath", sourcePath),
                                    packages);
        com.sun.tools.javadoc.Main.execute(javadocArgs);
    }

    public static String[] join(String... strings) {
        return strings;
    }

    public static String[] join(String[] base, String... more) {
        String[] strings = new String[base.length + more.length];
        System.arraycopy(base, 0, strings, 0, base.length);
        System.arraycopy(more, 0, strings, base.length, more.length);
        return strings;
    }
}
