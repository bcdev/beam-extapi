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
import org.apache.commons.lang.StringUtils;
import org.esa.beam.extapi.gen.c.CModuleGenerator;
import org.esa.beam.extapi.gen.py.PyCModuleGenerator;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Norman Fomferra
 */
public class ApiGeneratorDoclet extends Doclet {

    public interface Handler {
        boolean start(RootDoc root);
    }

    public static void main(String[] args) throws JDOMException, IOException {
        final DefaultHandler handler = new DefaultHandler();
        final String sourcePaths = StringUtils.join(handler.config.getSourcePaths(), File.pathSeparatorChar);
        run(handler, sourcePaths, handler.config.getPackages());
    }

    /**
     * This field is required to allow for multi-threaded javadoc invocations which is required for junit.
     */
    private final static Map<Thread, Handler> HANDLER_MAP = new HashMap<Thread, Handler>();

    public static void run(final Handler handler, String sourcePaths, String... packages) {
        final JavadocRunnable runnable = new JavadocRunnable(sourcePaths, packages);
        final Thread thread;
        synchronized (HANDLER_MAP) {
            if (HANDLER_MAP.isEmpty()) {
                thread = Thread.currentThread();
            } else {
                thread = new Thread(runnable);
            }
            HANDLER_MAP.put(Thread.currentThread(), handler);
        }
        if (Thread.currentThread() != thread) {
            thread.start();
        } else {
            runnable.run();
        }
    }

    /**
     * Javadoc entry point.
     *
     * @param root The document root.
     * @return true on success
     */
    @SuppressWarnings("UnusedDeclaration")
    public static boolean start(RootDoc root) {
        final Thread thread = Thread.currentThread();
        final Handler handler = HANDLER_MAP.get(thread);
        if (handler == null) {
            throw new IllegalStateException("no handler for thread " + thread);
        }
        final boolean start = handler.start(root);
        HANDLER_MAP.remove(thread);
        return start;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static int optionLength(String optionName) {
        return 0;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static boolean validOptions(String[][] options,
                                       DocErrorReporter docErrorReporter) {
        return true;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    private static class JavadocRunnable implements Runnable {

        private final String sourcePaths;
        private final String[] packages;

        public JavadocRunnable(String sourcePaths, String[] packages) {
            this.sourcePaths = sourcePaths;
            this.packages = packages;
        }

        @Override
        public void run() {
            Javadoc.run(ApiGeneratorDoclet.class.getName(), sourcePaths, packages);
        }

    }

    private static class DefaultHandler implements Handler {

        private final ApiGeneratorConfig config;

        private DefaultHandler() {
            try {
                config = ApiGeneratorConfigImpl.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean start(RootDoc root) {
            try {
                ApiInfo apiInfo = ApiInfo.create(config, root);
                final CModuleGenerator cModuleGenerator = new CModuleGenerator(apiInfo);
                final PyCModuleGenerator pyCModuleGenerator = new PyCModuleGenerator(cModuleGenerator);
                cModuleGenerator.run();
                pyCModuleGenerator.run();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
