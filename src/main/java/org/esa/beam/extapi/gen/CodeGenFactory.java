package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import static org.esa.beam.extapi.gen.Generator.*;

/**
 * @author Norman Fomferra
 */
class CodeGenFactory {

    public static CodeGenCallable createCodeGenCallable(ApiMethod apiMethod) throws GeneratorException {
        ApiClass apiClass = apiMethod.getEnclosingClass();
        CodeGenParameter[] parameters = createCodeGenParameters(apiMethod);
        CodeGenCallable callable;
        if (apiMethod.getMemberDoc() instanceof ConstructorDoc) {
            ConstructorDoc constructorDoc = (ConstructorDoc) apiMethod.getMemberDoc();
            callable = new CodeGenCallable.Constructor(apiClass, constructorDoc, parameters);
        } else {
            MethodDoc methodDoc = (MethodDoc) apiMethod.getMemberDoc();
            Type returnType = apiMethod.getReturnType();
            if (returnType.dimension().isEmpty()) {
                if (isVoid(returnType)) {
                    callable = new CodeGenCallable.VoidMethod(apiClass, parameters, methodDoc);
                } else if (isString(returnType)) {
                    callable = new CodeGenCallable.StringMethod(apiClass, parameters, methodDoc);
                } else if (returnType.isPrimitive()) {
                    callable = new CodeGenCallable.PrimitiveMethod(apiClass, parameters, methodDoc);
                } else {
                    callable = new CodeGenCallable.ObjectMethod(apiClass, parameters, methodDoc);
                }
            } else if (isPrimitiveArray(returnType)) {
                callable = new CodeGenCallable.PrimitiveArrayMethod(apiClass, parameters, methodDoc);
            } else if (isStringArray(returnType)) {
                callable = new CodeGenCallable.StringArrayMethod(apiClass, parameters, methodDoc);
            } else if (isObjectArray(returnType)) {
                callable = new CodeGenCallable.ObjectArrayMethod(apiClass, parameters, methodDoc);
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

    public static CodeGenParameter[] createCodeGenParameters(ApiMethod apiMethod) throws GeneratorException {
        ExecutableMemberDoc memberDoc = apiMethod.getMemberDoc();
        Parameter[] parameters = memberDoc.parameters();
        CodeGenParameter[] codeGenParameters = new CodeGenParameter[parameters.length];
        for (int i = 0; i < codeGenParameters.length; i++) {
            Parameter parameter = parameters[i];
            CodeGenParameter codeGenParameter;
            Type parameterType = parameter.type();
            boolean scalar = parameterType.dimension().equals("");
            if (scalar) {
                if (parameterType.isPrimitive()) {
                    codeGenParameter = new CodeGenParameter.PrimitiveScalar(parameter);
                } else if (parameterType.qualifiedTypeName().equals("java.lang.String")) {
                    codeGenParameter = new CodeGenParameter.StringScalar(parameter);
                } else {
                    codeGenParameter = new CodeGenParameter.ObjectScalar(parameter);
                }
            } else if (isPrimitiveArray(parameterType)) {
                codeGenParameter = new CodeGenParameter.PrimitiveArray(parameter, true);
            } else if (isStringArray(parameterType)) {
                codeGenParameter = new CodeGenParameter.StringArray(parameter);
            } else if (isObjectArray(parameterType)) {
                codeGenParameter = new CodeGenParameter.ObjectArray(parameter, true);
            } else {
                throw new GeneratorException(String.format("member %s#%s(): can't deal with parameter %s of type %s%s (not implemented yet)",
                                                           apiMethod.getEnclosingClass().getJavaName(),
                                                           memberDoc.name(),
                                                           parameter.name(),
                                                           parameterType.typeName(),
                                                           parameterType.dimension()));
            }
            codeGenParameters[i] = codeGenParameter;
        }
        return codeGenParameters;
    }

}
