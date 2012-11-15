package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface ApiGeneratorConfig {
    boolean isApiClass(String className);
    boolean isApiMethod(String className, String methodName, String methodSignature);
    ApiParameter.Modifier[] getParameterModifiers(String className, String methodName, String methodSignature);
    String getFunctionName(String className, String methodName, String methodSignature);
}
