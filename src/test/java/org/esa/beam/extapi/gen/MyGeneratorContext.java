package org.esa.beam.extapi.gen;

/**
* @author Norman Fomferra
*/
class MyGeneratorContext implements GeneratorContext {
    @Override
    public String getFunctionName(CodeGenCallable callable) {
        return callable.getEnclosingClass().getType().typeName() + "_" + callable.getApiMethod().getJavaName();
    }
}
