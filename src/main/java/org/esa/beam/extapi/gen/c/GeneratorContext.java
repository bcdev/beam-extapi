package org.esa.beam.extapi.gen.c;

import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;

/**
 * @author Norman Fomferra
 */
public interface GeneratorContext {
    String getFunctionNameFor(ApiMethod apiMethod);

    ApiParameter[] getParametersFor(ApiMethod apiMethod);
}
