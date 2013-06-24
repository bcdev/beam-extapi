package org.esa.beam.extapi.gen;

import com.sun.javadoc.Type;

/**
 * @author Norman Fomferra
 */
public interface GeneratorContext {
    ApiInfo getApiInfo();

    String getUniqueFunctionNameFor(ApiMethod apiMethod);

    String getComponentCClassVarName(Type type);

    ApiParameter[] getParametersFor(ApiMethod apiMethod);
}
