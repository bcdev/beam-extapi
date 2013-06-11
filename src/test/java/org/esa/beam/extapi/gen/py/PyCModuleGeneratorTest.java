package org.esa.beam.extapi.gen.py;

import org.esa.beam.extapi.gen.ModuleGenerator;
import org.esa.beam.extapi.gen.ModuleGeneratorTest;
import org.esa.beam.extapi.gen.c.CModuleGenerator;

/**
 * @author Norman Fomferra
 */
public class PyCModuleGeneratorTest extends ModuleGeneratorTest {

    @Override
    protected ModuleGenerator createModuleGenerator() {
        return new PyCModuleGenerator(new CModuleGenerator(API_INFO));
    }
}
