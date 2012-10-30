/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.beam.extapi.gen;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author Norman Fomferra
 */
public class GeneratorDoclet extends Doclet {

    public static void main(String[] args) {

        com.sun.tools.javadoc.Main.main(new String[]{
                "-doclet", GeneratorDoclet.class.getName(),
                "-sourcepath", "" +
                "../beam/beam/beam-core/src/main/java;" +
                "../beam/beam/beam-gpf/src/main/java",
                "org.esa.beam.framework.datamodel",
                "org.esa.beam.framework.dataio",
                "org.esa.beam.framework.gpf",
                "org.esa.beam.util",
        });
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean start(RootDoc root) {
        try {
            final Properties properties = new Properties();
            properties.load(GeneratorDoclet.class.getResourceAsStream("Generator-classes.txt"));
            Set<String> wrappedClassNames = properties.stringPropertyNames();
            GeneratorInfo generatorInfo = GeneratorInfo.create(root, wrappedClassNames);
            new Generator(generatorInfo).run();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static int optionLength(String optionName) {
        return 0;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean validOptions(String[][] options,
                                       DocErrorReporter docErrorReporter) {
        for (int i = 0; i < options.length; i++) {
            for (int j = 0; j < options[i].length; j++) {
                docErrorReporter.printWarning("options[" + i + "][" + j + "] = " + options[i][j]);
            }
        }
        return true;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

 }
