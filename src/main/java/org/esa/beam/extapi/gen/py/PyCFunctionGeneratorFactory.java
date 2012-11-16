package org.esa.beam.extapi.gen.py;

import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiInfo;
import org.esa.beam.extapi.gen.ApiMethod;
import org.esa.beam.extapi.gen.ApiParameter;
import org.esa.beam.extapi.gen.FunctionGenerator;
import org.esa.beam.extapi.gen.FunctionGeneratorFactory;
import org.esa.beam.extapi.gen.GeneratorException;
import org.esa.beam.extapi.gen.TypeHelpers;

/**
 * @author Norman Fomferra
 */
public class PyCFunctionGeneratorFactory implements FunctionGeneratorFactory {

    private final ApiInfo apiInfo;

    public PyCFunctionGeneratorFactory(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    @Override
    public FunctionGenerator createFunctionGenerator(ApiMethod apiMethod) throws GeneratorException {
        ApiClass apiClass = apiMethod.getEnclosingClass();
        PyCParameterGenerator[] parameterGenerators = createParameterGenerators(apiMethod);
        FunctionGenerator functionGenerator;
        if (apiMethod.getMemberDoc() instanceof ConstructorDoc) {
            functionGenerator = new PyCFunctionGenerator.Constructor(apiMethod, parameterGenerators);
        } else {
            MethodDoc methodDoc = (MethodDoc) apiMethod.getMemberDoc();
            Type returnType = apiMethod.getReturnType();
            if (returnType.dimension().isEmpty()) {
                if (TypeHelpers.isVoid(returnType)) {
                    functionGenerator = new PyCFunctionGenerator.VoidMethod(apiMethod, parameterGenerators);
                } else if (TypeHelpers.isString(returnType)) {
                    functionGenerator = new PyCFunctionGenerator.StringMethod(apiMethod, parameterGenerators);
                } else if (returnType.isPrimitive()) {
                    functionGenerator = new PyCFunctionGenerator.PrimitiveMethod(apiMethod, parameterGenerators);
                } else {
                    functionGenerator = new PyCFunctionGenerator.ObjectMethod(apiMethod, parameterGenerators);
                }
            } else if (TypeHelpers.isPrimitiveArray(returnType)) {
                functionGenerator = new PyCFunctionGenerator.PrimitiveArrayMethod(apiMethod, parameterGenerators);
            } else if (TypeHelpers.isStringArray(returnType)) {
                functionGenerator = new PyCFunctionGenerator.StringArrayMethod(apiMethod, parameterGenerators);
            } else if (TypeHelpers.isObjectArray(returnType)) {
                functionGenerator = new PyCFunctionGenerator.ObjectArrayMethod(apiMethod, parameterGenerators);
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

    @Override
    public PyCParameterGenerator[] createParameterGenerators(ApiMethod apiMethod) throws GeneratorException {
        ExecutableMemberDoc memberDoc = apiMethod.getMemberDoc();
        ApiParameter[] parameters = apiInfo.getParametersFor(apiMethod);
        PyCParameterGenerator[] parameterGenerators = new PyCParameterGenerator[parameters.length];
        for (int i = 0; i < parameterGenerators.length; i++) {
            ApiParameter parameter = parameters[i];
            PyCParameterGenerator parameterGenerator;
            Type parameterType = parameter.getType();
            boolean scalar = parameterType.dimension().equals("");

            if (scalar) {
                if (parameterType.isPrimitive()) {
                    parameterGenerator = new PyCParameterGenerator.PrimitiveScalar(parameter);
                } else if (parameterType.qualifiedTypeName().equals("java.lang.String")) {
                    parameterGenerator = new PyCParameterGenerator.StringScalar(parameter);
                } else {
                    parameterGenerator = new PyCParameterGenerator.ObjectScalar(parameter);
                }
            } else if (TypeHelpers.isPrimitiveArray(parameterType)) {
                parameterGenerator = new PyCParameterGenerator.PrimitiveArray(parameter);
            } else if (TypeHelpers.isStringArray(parameterType)) {
                parameterGenerator = new PyCParameterGenerator.StringArray(parameter);
            } else if (TypeHelpers.isObjectArray(parameterType)) {
                parameterGenerator = new PyCParameterGenerator.ObjectArray(parameter);
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
