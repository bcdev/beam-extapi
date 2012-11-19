package org.esa.beam.extapi.gen.c;

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
import org.esa.beam.extapi.gen.JavadocHelpers;
import org.esa.beam.extapi.gen.ParameterGenerator;

/**
 * @author Norman Fomferra
 */
public class CFunctionGeneratorFactory implements FunctionGeneratorFactory {

    private final ApiInfo apiInfo;

    public CFunctionGeneratorFactory(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    @Override
    public FunctionGenerator createFunctionGenerator(ApiMethod apiMethod) throws GeneratorException {
        ApiClass apiClass = apiMethod.getEnclosingClass();
        ParameterGenerator[] parameterGenerators = createParameterGenerators(apiMethod);
        FunctionGenerator functionGenerator;
        if (apiMethod.getMemberDoc() instanceof ConstructorDoc) {
            functionGenerator = new CFunctionGenerator.Constructor(apiMethod, parameterGenerators);
        } else {
            MethodDoc methodDoc = (MethodDoc) apiMethod.getMemberDoc();
            Type returnType = apiMethod.getReturnType();
            if (returnType.dimension().isEmpty()) {
                if (JavadocHelpers.isVoid(returnType)) {
                    functionGenerator = new CFunctionGenerator.VoidMethod(apiMethod, parameterGenerators);
                } else if (JavadocHelpers.isString(returnType)) {
                    functionGenerator = new CFunctionGenerator.StringMethod(apiMethod, parameterGenerators);
                } else if (returnType.isPrimitive()) {
                    functionGenerator = new CFunctionGenerator.PrimitiveMethod(apiMethod, parameterGenerators);
                } else {
                    functionGenerator = new CFunctionGenerator.ObjectMethod(apiMethod, parameterGenerators);
                }
            } else if (JavadocHelpers.isPrimitiveArray(returnType)) {
                functionGenerator = new CFunctionGenerator.PrimitiveArrayMethod(apiMethod, parameterGenerators);
            } else if (JavadocHelpers.isStringArray(returnType)) {
                functionGenerator = new CFunctionGenerator.StringArrayMethod(apiMethod, parameterGenerators);
            } else if (JavadocHelpers.isObjectArray(returnType)) {
                functionGenerator = new CFunctionGenerator.ObjectArrayMethod(apiMethod, parameterGenerators);
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
    public ParameterGenerator[] createParameterGenerators(ApiMethod apiMethod) throws GeneratorException {
        ExecutableMemberDoc memberDoc = apiMethod.getMemberDoc();
        ApiParameter[] parameters = apiInfo.getParametersFor(apiMethod);
        CParameterGenerator[] parameterGenerators = new CParameterGenerator[parameters.length];
        for (int i = 0; i < parameterGenerators.length; i++) {
            ApiParameter parameter = parameters[i];
            CParameterGenerator parameterGenerator;
            Type parameterType = parameter.getType();
            boolean scalar = parameterType.dimension().equals("");

            if (scalar) {
                if (parameterType.isPrimitive()) {
                    parameterGenerator = new CParameterGenerator.PrimitiveScalar(parameter);
                } else if (parameterType.qualifiedTypeName().equals("java.lang.String")) {
                    parameterGenerator = new CParameterGenerator.StringScalar(parameter);
                } else {
                    parameterGenerator = new CParameterGenerator.ObjectScalar(parameter);
                }
            } else if (JavadocHelpers.isPrimitiveArray(parameterType)) {
                parameterGenerator = new CParameterGenerator.PrimitiveArray(parameter);
            } else if (JavadocHelpers.isStringArray(parameterType)) {
                parameterGenerator = new CParameterGenerator.StringArray(parameter);
            } else if (JavadocHelpers.isObjectArray(parameterType)) {
                parameterGenerator = new CParameterGenerator.ObjectArray(parameter);
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
