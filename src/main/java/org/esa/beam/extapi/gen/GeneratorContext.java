package org.esa.beam.extapi.gen;

/**
 * @author Norman Fomferra
 */
public interface GeneratorContext {
    ApiInfo getApiInfo();

    String getFunctionNameFor(ApiMethod apiMethod);

    ApiParameter[] getParametersFor(ApiMethod apiMethod);
}
