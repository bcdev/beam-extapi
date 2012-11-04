package org.esa.beam.extapi.gen.c;

import org.esa.beam.extapi.gen.c.FunctionGenerator;
import org.esa.beam.extapi.gen.c.GeneratorContext;

/**
* @author Norman Fomferra
*/
public class MyGeneratorContext implements GeneratorContext {
    @Override
    public String getFunctionName(FunctionGenerator callable) {
        return callable.getEnclosingClass().getType().typeName() + "_" + callable.getApiMethod().getJavaName();
    }
}
