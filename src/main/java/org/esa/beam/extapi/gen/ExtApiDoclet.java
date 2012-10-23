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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author Norman Fomferra
 * @version $Revision$ $Date$
 */
public class ExtApiDoclet extends Doclet {
    private RootDoc rootDoc;
    private Set<ApiClass> wrappedClasses;
    private Set<ApiClass> usedClasses;
    private Set<String> wrappedClassNames;


    public static void main(String[] args) {

        com.sun.tools.javadoc.Main.main(new String[]{
                "-doclet", ExtApiDoclet.class.getName(),
                "-sourcepath", "" +
                "../beam/beam/beam-core/src/main/java;" +
                "../beam/beam/beam-gpf/src/main/java",
                "org.esa.beam.framework.datamodel",
                "org.esa.beam.framework.dataio",
                "org.esa.beam.framework.gpf",
        });
    }

    private ExtApiDoclet(RootDoc rootDoc) throws IOException {
        this.rootDoc = rootDoc;
        wrappedClasses = new HashSet<ApiClass>();
        usedClasses = new HashSet<ApiClass>();

        final Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("ExtApiClasses.txt"));
        wrappedClassNames = properties.stringPropertyNames();
    }

    private void start() throws Exception {
        final ClassDoc[] classDocs = rootDoc.classes();
        for (ClassDoc classDoc : classDocs) {

            final MethodDoc[] methodDocs = classDoc.methods();

            if (classDoc.isPublic() && wrappedClassNames.contains(classDoc.qualifiedTypeName())) {

                wrappedClasses.add(new ApiClass(classDoc));

                for (MethodDoc methodDoc : methodDocs) {
                    if (methodDoc.isPublic()) {
                        final Type retType = methodDoc.returnType();

                        if (!retType.isPrimitive()) {
                            usedClasses.add(new ApiClass(retType));
                        }

                        final Parameter[] parameters = methodDoc.parameters();
                        StringBuilder paramList = new StringBuilder();
                        for (Parameter parameter : parameters) {
                            final Type type = parameter.type();
                            final String name = parameter.name();
                            if (paramList.length() > 0) {
                                paramList.append(", ");
                            }
                            paramList.append(mapTypeName(type));
                            paramList.append(" ");
                            paramList.append(name);

                            if (!type.isPrimitive()) {
                                usedClasses.add(new ApiClass(type));
                            }
                        }

                        //System.out.printf("%s %s_%s(%s) {%n}%n", mapTypeName(retType), classDoc.name(), methodDoc.name(), paramList);
                    }
                }
            } else {
                System.out.println("Ignored non-API class: " + classDoc.qualifiedTypeName());
            }
        }

        final ArrayList<ApiClass> wrappedClasses = new ArrayList<ApiClass>();
        Arrays.sort(wrappedClasses);

        VelocityContext velocityContext = new VelocityContext(System.getProperties());
        velocityContext.put("wrappedClasses", wrappedClasses);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        final FileWriter fileWriter = new FileWriter("beam_wrappers.c");
        final boolean b = velocityEngine.resourceExists("CWrappers.vm");
        velocityEngine.mergeTemplate("CWrappers.vm", velocityContext, fileWriter);


        fileWriter.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean start(RootDoc root) {
        try {
            new ExtApiDoclet(root).start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String mapTypeName(Type type) {
        final String simpleTypeName = type.simpleTypeName();
        if (simpleTypeName.equals("String")) {
            return "char*";
        }
        return type.isPrimitive() ? simpleTypeName : simpleTypeName + "*";
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
