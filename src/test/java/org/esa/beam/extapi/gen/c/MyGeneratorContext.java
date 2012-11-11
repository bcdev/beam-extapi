package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.Parameter;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;

/**
* @author Norman Fomferra
*/
public class MyGeneratorContext implements GeneratorContext {
    @Override
    public String getFunctionNameFor(ApiMethod apiMethod) {
        return apiMethod.getEnclosingClass().getType().typeName() + "_" + apiMethod.getJavaName();
    }

    @Override
    public ApiParameter[] getParametersFor(ApiMethod apiMethod) {
        Parameter[] parameters = apiMethod.getMemberDoc().parameters();
        ApiParameter[] apiParameters = new  ApiParameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            apiParameters[i] = new ApiParameter(parameters[i], ApiParameter.Modifier.IN);
        }
        return apiParameters;
    }
}
