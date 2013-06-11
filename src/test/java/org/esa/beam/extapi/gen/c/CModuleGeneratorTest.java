package org.esa.beam.extapi.gen.c;

import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.ModuleGeneratorTest;

/**
 * @author Norman Fomferra
 */
public class CModuleGeneratorTest extends ModuleGeneratorTest {
    @Override
    protected ModuleGenerator createModuleGenerator() {
        return new CModuleGenerator(API_INFO);
    }
}
