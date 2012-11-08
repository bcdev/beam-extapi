package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface ApiGeneratorConfig {
    boolean isApiClass(String className);
    ApiParameter.Modifier[] getParameterModifiers(String className, String methodName, String methodSignature);
    String getMethodCName(String className, String methodName, String methodSignature);
}
