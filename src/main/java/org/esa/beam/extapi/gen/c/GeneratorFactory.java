package org.esa.beam.extapi.gen.c;

import com.sun.javadoc.*;
import org.esa.beam.extapi.gen.ApiClass;
import org.esa.beam.extapi.gen.ApiMethod;

/**
 * @author Norman Fomferra
 */
public class GeneratorFactory {

    public static FunctionGenerator createCodeGenCallable(ApiMethod apiMethod) throws GeneratorException {
        ApiClass apiClass = apiMethod.getEnclosingClass();
        ParameterGenerator[] parameterGenerators = createCodeGenParameters(apiMethod);
        FunctionGenerator callable;
        if (apiMethod.getMemberDoc() instanceof ConstructorDoc) {
            callable = new FunctionGenerator.Constructor(apiMethod, parameterGenerators);
        } else {
            MethodDoc methodDoc = (MethodDoc) apiMethod.getMemberDoc();
            Type returnType = apiMethod.getReturnType();
            if (returnType.dimension().isEmpty()) {
                if (ModuleGenerator.isVoid(returnType)) {
                    callable = new FunctionGenerator.VoidMethod(apiMethod, parameterGenerators);
                } else if (ModuleGenerator.isString(returnType)) {
                    callable = new FunctionGenerator.StringMethod(apiMethod, parameterGenerators);
                } else if (returnType.isPrimitive()) {
                    callable = new FunctionGenerator.PrimitiveMethod(apiMethod, parameterGenerators);
                } else {
                    callable = new FunctionGenerator.ObjectMethod(apiMethod, parameterGenerators);
                }
            } else if (ModuleGenerator.isPrimitiveArray(returnType)) {
                callable = new FunctionGenerator.PrimitiveArrayMethod(apiMethod, parameterGenerators);
            } else if (ModuleGenerator.isStringArray(returnType)) {
                callable = new FunctionGenerator.StringArrayMethod(apiMethod, parameterGenerators);
            } else if (ModuleGenerator.isObjectArray(returnType)) {
                callable = new FunctionGenerator.ObjectArrayMethod(apiMethod, parameterGenerators);
            } else {
                throw new GeneratorException(String.format("member %s#%s(): can't deal with return type %s%s (not implemented yet)",
                                                           apiClass.getJavaName(),
                                                           methodDoc.name(),
                                                           returnType.typeName(),
                                                           returnType.dimension()));
            }
        }
        return callable;
    }

    public static ParameterGenerator[] createCodeGenParameters(ApiMethod apiMethod) throws GeneratorException {
        ExecutableMemberDoc memberDoc = apiMethod.getMemberDoc();
        Parameter[] parameters = memberDoc.parameters();
        ParameterGenerator[] parameterGenerators = new ParameterGenerator[parameters.length];
        for (int i = 0; i < parameterGenerators.length; i++) {
            Parameter parameter = parameters[i];
            ParameterGenerator parameterGenerator;
            Type parameterType = parameter.type();
            boolean scalar = parameterType.dimension().equals("");
            if (scalar) {
                if (parameterType.isPrimitive()) {
                    parameterGenerator = new ParameterGenerator.PrimitiveScalar(parameter);
                } else if (parameterType.qualifiedTypeName().equals("java.lang.String")) {
                    parameterGenerator = new ParameterGenerator.StringScalar(parameter);
                } else {
                    parameterGenerator = new ParameterGenerator.ObjectScalar(parameter);
                }
            } else if (ModuleGenerator.isPrimitiveArray(parameterType)) {
                parameterGenerator = new ParameterGenerator.PrimitiveArray(parameter, true);
            } else if (ModuleGenerator.isStringArray(parameterType)) {
                parameterGenerator = new ParameterGenerator.StringArray(parameter);
            } else if (ModuleGenerator.isObjectArray(parameterType)) {
                parameterGenerator = new ParameterGenerator.ObjectArray(parameter, true);
            } else {
                throw new GeneratorException(String.format("member %s#%s(): can't deal with parameter %s of type %s%s (not implemented yet)",
                                                           apiMethod.getEnclosingClass().getJavaName(),
                                                           memberDoc.name(),
                                                           parameter.name(),
                                                           parameterType.typeName(),
                                                           parameterType.dimension()));
            }
            parameterGenerators[i] = parameterGenerator;
        }
        return parameterGenerators;
    }

}
