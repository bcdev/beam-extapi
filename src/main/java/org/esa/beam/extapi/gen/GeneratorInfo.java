package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.util.*;

import static org.esa.beam.extapi.gen.Generator.isObjectArray;
import static org.esa.beam.extapi.gen.Generator.isPrimitiveArray;
import static org.esa.beam.extapi.gen.Generator.isString;
import static org.esa.beam.extapi.gen.Generator.isStringArray;
import static org.esa.beam.extapi.gen.Generator.isVoid;

/**
 * @author Norman Fomferra
 */
class GeneratorInfo {

    private final List<ApiClass> apiClasses;
    private final Set<ApiClass> usedApiClasses;
    private final Map<CodeGenCallable, String> targetFunctionNames;

    public List<ApiClass> getApiClasses() {
        return apiClasses;
    }

    public String getExternalFunctionName(CodeGenCallable apiMethod) {
        return targetFunctionNames.get(apiMethod);
    }

    public Set<ApiClass> getUsedApiClasses() {
        return usedApiClasses;
    }

    private GeneratorInfo(List<ApiClass> apiClasses, Set<ApiClass> usedApiClasses, Map<CodeGenCallable, String> targetFunctionNames) {
        this.apiClasses = apiClasses;
        this.usedApiClasses = usedApiClasses;
        this.targetFunctionNames = targetFunctionNames;
    }

    public static GeneratorInfo create(RootDoc rootDoc, Set<String> wrappedClassNames) throws ClassNotFoundException, NoSuchMethodException {

        ArrayList<ApiClass> apiClasses = new ArrayList<ApiClass>(100);
        HashSet<ApiClass> usedApiClasses = new HashSet<ApiClass>();
        final ClassDoc[] classDocs = rootDoc.classes();

        Map<String, List<CodeGenCallable>> sameTargetFunctionNames = new HashMap<String, List<CodeGenCallable>>(1000);

        for (ClassDoc classDoc : classDocs) {

            if (classDoc.isPublic() && wrappedClassNames.contains(classDoc.qualifiedTypeName())) {

                ApiClass apiClass = new ApiClass(classDoc);
                apiClasses.add(apiClass);

                ConstructorDoc[] constructors = classDoc.constructors();
                for (ConstructorDoc constructorDoc : constructors) {
                    if (isApiElement(constructorDoc)) {
                        try {
                            CodeGenParameter[] parameters = createParameterCodeGens(apiClass, constructorDoc);
                            CodeGenCallable apiMethod = new CodeGenCallable.Constructor(apiClass, constructorDoc, parameters);
                            collect(apiClass, apiMethod, usedApiClasses, sameTargetFunctionNames);
                        } catch (GeneratorException e) {
                            System.out.printf("Skipping constructor: %s\n", e.getMessage());
                        }
                    } else {
                        System.out.printf("Ignoring deprecated constructor: %s#%s()\n", classDoc.qualifiedTypeName(), constructorDoc.name());
                    }
                }

                final MethodDoc[] methodDocs = classDoc.methods();
                for (MethodDoc methodDoc : methodDocs) {
                    if (isApiElement(methodDoc)) {
                        try {
                            CodeGenParameter[] parameters = createParameterCodeGens(apiClass, methodDoc);
                            CodeGenCallable apiMethod = createMethodCallable(apiClass, methodDoc, parameters);
                            collect(apiClass, apiMethod, usedApiClasses, sameTargetFunctionNames);
                        } catch (GeneratorException e) {
                            System.out.printf("Skipping method: %s\n", e.getMessage());
                        }
                    } else {
                        System.out.printf("Ignoring deprecated method: %s#%s()\n", classDoc.qualifiedTypeName(), methodDoc.name());
                    }
                }

            } else {
                System.out.printf("Ignoring non-API class: %s\n", classDoc.qualifiedTypeName());
            }
        }
        Collections.sort(apiClasses);

        Map<CodeGenCallable, String> targetFunctionNames = new HashMap<CodeGenCallable, String>(apiClasses.size() * 100);
        for (ApiClass apiClass : apiClasses) {
            for (CodeGenCallable callable : apiClass.getCallableList()) {
                List<CodeGenCallable> apiMethods = sameTargetFunctionNames.get(callable.getFunctionBaseName());
                if (apiMethods.size() > 1) {
                    for (int i = 0; i < apiMethods.size(); i++) {
                        targetFunctionNames.put(apiMethods.get(i), apiMethods.get(i).getFunctionBaseName() + (i + 1));
                    }
                } else {
                    targetFunctionNames.put(apiMethods.get(0), apiMethods.get(0).getFunctionBaseName());
                }
            }
        }

        return new GeneratorInfo(apiClasses, usedApiClasses, targetFunctionNames);
    }

    private static CodeGenCallable createMethodCallable(ApiClass apiClass, MethodDoc methodDoc, CodeGenParameter[] parameters) throws GeneratorException {
        CodeGenCallable apiMethod;
        Type returnType = methodDoc.returnType();
        if (returnType.dimension().isEmpty()) {
            if (isVoid(returnType)) {
                apiMethod = new CodeGenCallable.VoidMethod(apiClass, parameters, methodDoc);
            } else if (isString(returnType)) {
                apiMethod = new CodeGenCallable.StringMethod(apiClass, parameters, methodDoc);
            } else if (returnType.isPrimitive()) {
                apiMethod = new CodeGenCallable.PrimitiveMethod(apiClass, parameters, methodDoc);
            } else {
                apiMethod = new CodeGenCallable.ObjectMethod(apiClass, parameters, methodDoc);
            }
        } else if (isPrimitiveArray(returnType)) {
            apiMethod = new CodeGenCallable.PrimitiveArrayMethod(apiClass, parameters, methodDoc);
        } else if (isStringArray(returnType)) {
            apiMethod = new CodeGenCallable.StringArrayMethod(apiClass, parameters, methodDoc);
        } else if (isObjectArray(returnType)) {
            apiMethod = new CodeGenCallable.ObjectArrayMethod(apiClass, parameters, methodDoc);
        } else {
            throw new GeneratorException(String.format("member %s#%s(): can't deal with return type %s%s (not implemented yet)",
                                                     apiClass.getJavaName(),
                                                     methodDoc.name(),
                                                     returnType.typeName(),
                                                     returnType.dimension()));
        }
        return apiMethod;
    }

    public static CodeGenParameter[] createParameterCodeGens(ApiClass apiClass, ExecutableMemberDoc memberDoc) throws GeneratorException {
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
                                                           apiClass.getJavaName(),
                                                           memberDoc.name(),
                                                           parameter.name(),
                                                           parameterType.typeName(),
                                                           parameterType.dimension()));
            }
            codeGenParameters[i] = codeGenParameter;
        }
        return codeGenParameters;
    }

    private static void collect(ApiClass apiClass, CodeGenCallable apiMethod, HashSet<ApiClass> usedApiClasses, Map<String, List<CodeGenCallable>> sameExternalFunctionNames) throws ClassNotFoundException, NoSuchMethodException {
        apiClass.addCallable(apiMethod);
        List<CodeGenCallable> apiMethods = sameExternalFunctionNames.get(apiMethod.getFunctionBaseName());
        if (apiMethods == null) {
            apiMethods = new ArrayList<CodeGenCallable>(4);
            sameExternalFunctionNames.put(apiMethod.getFunctionBaseName(), apiMethods);
        }
        apiMethods.add(apiMethod);

        if (!apiMethod.getReturnType().isPrimitive()) {
            usedApiClasses.add(new ApiClass(apiMethod.getReturnType()));
        }
        collectUsedClassesFromParameters(apiMethod.getMemberDoc(), usedApiClasses);
    }

    private static boolean isApiElement(ProgramElementDoc programElementDoc) {
        return programElementDoc.isPublic() && programElementDoc.tags("deprecated").length == 0;
    }

    private static void collectUsedClassesFromParameters(ExecutableMemberDoc memberDoc, Set<ApiClass> usedApiClasses) throws ClassNotFoundException {
        final Parameter[] parameters = memberDoc.parameters();
        for (Parameter parameter : parameters) {
            final Type type = parameter.type();
            if (!type.isPrimitive()) {
                usedApiClasses.add(new ApiClass(type));
            }
        }
    }

}
