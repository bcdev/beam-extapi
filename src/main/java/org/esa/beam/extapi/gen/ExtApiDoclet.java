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
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Norman Fomferra
 * @version $Revision$ $Date$
 */
public class ExtApiDoclet extends Doclet {
    private RootDoc rootDoc;
    private Set<ApiClass> wrappedClasses;
    private Set<ApiClass> usedClasses;
    private Set<String> wrappedClassNames;
    private Map<String, List<ApiMethod>> wrappedClassMethods;


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
        wrappedClasses = new HashSet<ApiClass>(100);
        wrappedClassMethods  = new HashMap<String, List<ApiMethod>>(100);
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

                ApiClass apiClass = new ApiClass(classDoc);
                wrappedClasses.add(apiClass);
                ArrayList<ApiMethod> apiMethods = new ArrayList<ApiMethod>(64);
                wrappedClassMethods.put(apiClass.getJavaName(), apiMethods);

                for (MethodDoc methodDoc : methodDocs) {
                    if (methodDoc.isPublic()) {
                        final Type retType = methodDoc.returnType();

                        apiMethods.add(new ApiMethod(apiClass, methodDoc));

                        if (!retType.isPrimitive()) {
                            usedClasses.add(new ApiClass(retType));
                        }

                        final Parameter[] parameters = methodDoc.parameters();
                        for (Parameter parameter : parameters) {
                            final Type type = parameter.type();
                            if (!type.isPrimitive()) {
                                usedClasses.add(new ApiClass(type));
                            }
                        }
                    }
                }

                Collections.sort(apiMethods);

            } else {
                System.out.println("Ignored non-API class: " + classDoc.qualifiedTypeName());
            }
        }

        final ArrayList<ApiClass> wrappedClasses = new ArrayList<ApiClass>(this.wrappedClasses);
        Collections.sort(wrappedClasses);

        VelocityContext velocityContext = new VelocityContext(System.getProperties());
        velocityContext.put("wrappedClasses", wrappedClasses);
        velocityContext.put("wrappedClassMethods", wrappedClassMethods);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        FileWriter writer = new FileWriter("beam_wrappers.c");
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("CWrappers.vm"));
        velocityEngine.evaluate(velocityContext, writer, "gen", reader);

        writer.close();
        reader.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean start(RootDoc root) {
        try {
            new ExtApiDoclet(root).start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
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
