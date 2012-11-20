/*
 * Copyright (C) 2011 Brockmann Consult GmbH (info@brockmann-consult.de)
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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Norman Fomferra
 * @author Thomas Storm
 */
public class ApiGeneratorConfigImplTest {

    @Test
    public void testParseSourcePaths() throws Exception {
        final ApiGeneratorConfig config = ApiGeneratorConfigImpl.load();
        String[] sourcePaths = config.getSourcePaths();
        assertArrayEquals(new String[]{
                "../beam/beam-core/src/main/java",
                "../beam/beam-gpf/src/main/java"
        }, sourcePaths);
    }

    @Test
    public void testPackages() throws Exception {
        final ApiGeneratorConfig config = ApiGeneratorConfigImpl.load();
        String[] sourcePaths = config.getPackages();
        assertArrayEquals(new String[]{
                "org.esa.beam.framework.datamodel",
                "org.esa.beam.framework.dataio",
                "org.esa.beam.framework.gpf",
                "org.esa.beam.util"
        }, sourcePaths);
    }
}
