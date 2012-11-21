package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface GeneratorContext {
    String getFunctionNameFor(ApiMethod apiMethod);

    ApiParameter[] getParametersFor(ApiMethod apiMethod);

    ApiInfo getApiInfo();
}
