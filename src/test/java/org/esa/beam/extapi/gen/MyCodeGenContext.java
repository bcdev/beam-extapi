package org.esa.beam.extapi.gen;

/**
* @author Norman Fomferra
*/
class MyCodeGenContext implements CodeGenContext {
    @Override
    public String getTargetFunctionName(CodeGenCallable callable) {
        return callable.getEnclosingClass().getType().typeName() + "_" + callable.getJavaName();
    }
}
