package org.esa.beam.extapi.gen;

import java.util.HashMap;

/**
 * @author Norman Fomferra
 */
public class ApiGeneratorConfigMock implements ApiGeneratorConfig {
    private final HashMap<String, Class> classMap = new HashMap<String, Class>();
    private final HashMap<String, ApiParameter.Modifier[]> modifiersMap = new HashMap<String, ApiParameter.Modifier[]>();

    public ApiGeneratorConfigMock(Class<?>... classes) {
        for (Class<?> aClass : classes) {
            this.classMap.put(aClass.getName(), aClass);
        }
    }

    public void addModifiers(Class<?> aClass, String methodName, String methodSignature, ApiParameter.Modifier[] modifiers) {
        modifiersMap.put(aClass.getName() + methodName + methodSignature, modifiers);
    }

    @Override
    public boolean getIncludeDeprecatedClasses() {
        return false;
    }

    @Override
    public boolean getIncludeDeprecatedMethods() {
        return false;
    }

    @Override
    public boolean isApiClass(String className) {
        return classMap.containsKey(className);
    }

    @Override
    public boolean isApiMethod(String className, String methodName, String methodSignature) {
        return isApiClass(className);
    }

    @Override
    public ApiParameter.Modifier[] getParameterModifiers(String className, String methodName, String methodSignature) {
        return modifiersMap.get(className + methodName + methodSignature);
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
