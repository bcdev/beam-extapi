package org.esa.beam.extapi.gen;

import org.esa.beam.extapi.gen.test.TestClass2;

/**
* @author Norman Fomferra
*/
public class ApiGeneratorConfigMock implements ApiGeneratorConfig {
    private final Class<TestClass2> aClass;

    public ApiGeneratorConfigMock(Class<TestClass2> aClass) {
        this.aClass = aClass;
    }

    @Override
    public boolean isApiClass(String className) {
        return aClass.getName().equals(className);
    }

    @Override
    public boolean isApiMethod(String className, String methodName, String methodSignature) {
        return isApiClass(className);
    }

    @Override
    public ApiParameter.Modifier[] getParameterModifiers(String className, String methodName, String methodSignature) {
        return null;
    }

    @Override
    public String getFunctionName(String className, String methodName, String methodSignature) {
        return methodName;
    }

    @Override
    public String getLengthExpr(String className, String methodName, String methodSignature) {
        return null;
    }

    @Override
    public String[] getSourcePaths() {
        return new String[0];
    }

    @Override
    public String[] getPackages() {
        return new String[0];
    }
}
