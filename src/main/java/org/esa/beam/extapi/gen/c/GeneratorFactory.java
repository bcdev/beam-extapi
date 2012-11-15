package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.*;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;

/**
 * @author Norman Fomferra
 */
public class GeneratorFactory {

    private final ApiInfo apiInfo;

    public GeneratorFactory(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    public FunctionGenerator createFunctionGenerator(ApiMethod apiMethod) throws GeneratorException {
        ApiClass apiClass = apiMethod.getEnclosingClass();
        ParameterGenerator[] parameterGenerators = createParameterGenerators(apiMethod);
        FunctionGenerator functionGenerator;
        if (apiMethod.getMemberDoc() instanceof ConstructorDoc) {
            functionGenerator = new FunctionGenerator.Constructor(apiMethod, parameterGenerators);
        } else {
            MethodDoc methodDoc = (MethodDoc) apiMethod.getMemberDoc();
            Type returnType = apiMethod.getReturnType();
            if (returnType.dimension().isEmpty()) {
                if (TypeHelpers.isVoid(returnType)) {
                    functionGenerator = new FunctionGenerator.VoidMethod(apiMethod, parameterGenerators);
                } else if (TypeHelpers.isString(returnType)) {
                    functionGenerator = new FunctionGenerator.StringMethod(apiMethod, parameterGenerators);
                } else if (returnType.isPrimitive()) {
                    functionGenerator = new FunctionGenerator.PrimitiveMethod(apiMethod, parameterGenerators);
                } else {
                    functionGenerator = new FunctionGenerator.ObjectMethod(apiMethod, parameterGenerators);
                }
            } else if (TypeHelpers.isPrimitiveArray(returnType)) {
                functionGenerator = new FunctionGenerator.PrimitiveArrayMethod(apiMethod, parameterGenerators);
            } else if (TypeHelpers.isStringArray(returnType)) {
                functionGenerator = new FunctionGenerator.StringArrayMethod(apiMethod, parameterGenerators);
            } else if (TypeHelpers.isObjectArray(returnType)) {
                functionGenerator = new FunctionGenerator.ObjectArrayMethod(apiMethod, parameterGenerators);
            } else {
                throw new GeneratorException(String.format("member %s#%s(): can't deal with return type %s%s (not implemented yet)",
                                                           apiClass.getJavaName(),
                                                           methodDoc.name(),
                                                           returnType.typeName(),
                                                           returnType.dimension()));
            }
        }
        return functionGenerator;
    }

    public ParameterGenerator[] createParameterGenerators(ApiMethod apiMethod) throws GeneratorException {
        ExecutableMemberDoc memberDoc = apiMethod.getMemberDoc();
        ApiParameter[] parameters = apiInfo.getParametersFor(apiMethod);
        ParameterGenerator[] parameterGenerators = new ParameterGenerator[parameters.length];
        for (int i = 0; i < parameterGenerators.length; i++) {
            ApiParameter parameter = parameters[i];
            ParameterGenerator parameterGenerator;
            Type parameterType = parameter.getType();
            boolean scalar = parameterType.dimension().equals("");

            if (scalar) {
                if (parameterType.isPrimitive()) {
                    parameterGenerator = new ParameterGenerator.PrimitiveScalar(parameter);
                } else if (parameterType.qualifiedTypeName().equals("java.lang.String")) {
                    parameterGenerator = new ParameterGenerator.StringScalar(parameter);
                } else {
                    parameterGenerator = new ParameterGenerator.ObjectScalar(parameter);
                }
            } else if (TypeHelpers.isPrimitiveArray(parameterType)) {
                parameterGenerator = new ParameterGenerator.PrimitiveArray(parameter);
            } else if (TypeHelpers.isStringArray(parameterType)) {
                parameterGenerator = new ParameterGenerator.StringArray(parameter);
            } else if (TypeHelpers.isObjectArray(parameterType)) {
                parameterGenerator = new ParameterGenerator.ObjectArray(parameter);
            } else {
                throw new GeneratorException(String.format("member %s#%s(): can't deal with parameter %s of type %s%s (not implemented yet)",
                                                           apiMethod.getEnclosingClass().getJavaName(),
                                                           memberDoc.name(),
                                                           parameter.getJavaName(),
                                                           parameterType.typeName(),
                                                           parameterType.dimension()));
            }
            parameterGenerators[i] = parameterGenerator;
        }
        return parameterGenerators;
    }

}
