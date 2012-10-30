package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.util.*;

import static org.esa.beam.extapi.gen.Generator.isStringArray;

/**
 * @author Norman Fomferra
 */
class GeneratorInfo {

    private final List<CodeGenClass> codeGenClasses;
    private final Set<CodeGenClass> usedCodeGenClasses;
    private final Map<CodeGenCallable, String> targetFunctionNames;

    public List<CodeGenClass> getCodeGenClasses() {
        return codeGenClasses;
    }

    public String getExternalFunctionName(CodeGenCallable apiMethod) {
        return targetFunctionNames.get(apiMethod);
    }

    public Set<CodeGenClass> getUsedCodeGenClasses() {
        return usedCodeGenClasses;
    }

    private GeneratorInfo(List<CodeGenClass> codeGenClasses, Set<CodeGenClass> usedCodeGenClasses, Map<CodeGenCallable, String> targetFunctionNames) {
        this.codeGenClasses = codeGenClasses;
        this.usedCodeGenClasses = usedCodeGenClasses;
        this.targetFunctionNames = targetFunctionNames;
    }

    public static GeneratorInfo create(RootDoc rootDoc, Set<String> wrappedClassNames) throws ClassNotFoundException, NoSuchMethodException {

        ArrayList<CodeGenClass> codeGenClasses = new ArrayList<CodeGenClass>(100);
        HashSet<CodeGenClass> usedCodeGenClasses = new HashSet<CodeGenClass>();
        final ClassDoc[] classDocs = rootDoc.classes();

        Map<String, List<CodeGenCallable>> sameTargetFunctionNames = new HashMap<String, List<CodeGenCallable>>(1000);

        for (ClassDoc classDoc : classDocs) {

            if (classDoc.isPublic() && wrappedClassNames.contains(classDoc.qualifiedTypeName())) {

                CodeGenClass codeGenClass = new CodeGenClass(classDoc);
                codeGenClasses.add(codeGenClass);

                ConstructorDoc[] constructors = classDoc.constructors();
                for (ConstructorDoc constructorDoc : constructors) {
                    if (isApiElement(constructorDoc)) {
                        try {
                            CodeGenParameter[] parameters = createParameterCodeGens(codeGenClass, constructorDoc);
                            CodeGenCallable apiMethod = new CodeGenCallable.Constructor(codeGenClass, constructorDoc, parameters);
                            collect(codeGenClass, apiMethod, usedCodeGenClasses, sameTargetFunctionNames);
                        } catch (CodeGenException e) {
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
                            CodeGenParameter[] parameters = createParameterCodeGens(codeGenClass, methodDoc);
                            CodeGenCallable apiMethod = createMethodCallable(codeGenClass, methodDoc, parameters);
                            collect(codeGenClass, apiMethod, usedCodeGenClasses, sameTargetFunctionNames);
                        } catch (CodeGenException e) {
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
        Collections.sort(codeGenClasses);

        Map<CodeGenCallable, String> targetFunctionNames = new HashMap<CodeGenCallable, String>(codeGenClasses.size() * 100);
        for (CodeGenClass codeGenClass : codeGenClasses) {
            for (CodeGenCallable callable : codeGenClass.getCallableList()) {
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

        return new GeneratorInfo(codeGenClasses, usedCodeGenClasses, targetFunctionNames);
    }

    private static CodeGenCallable createMethodCallable(CodeGenClass codeGenClass, MethodDoc methodDoc, CodeGenParameter[] parameters) throws CodeGenException {
        CodeGenCallable apiMethod;
        Type returnType = methodDoc.returnType();
        if (returnType.dimension().isEmpty()) {
            if (Generator.isVoid(returnType)) {
                apiMethod = new CodeGenCallable.VoidMethod(codeGenClass, parameters, methodDoc);
            } else if (Generator.isString(returnType)) {
                apiMethod = new CodeGenCallable.StringMethod(codeGenClass, parameters, methodDoc);
            } else if (returnType.isPrimitive()) {
                apiMethod = new CodeGenCallable.PrimitiveMethod(codeGenClass, parameters, methodDoc);
            } else {
                apiMethod = new CodeGenCallable.ObjectMethod(codeGenClass, parameters, methodDoc);
            }
        } else if (isStringArray(returnType)) {
            apiMethod = new CodeGenCallable.StringArrayMethod(codeGenClass, parameters, methodDoc);
        } else {
            throw new CodeGenException(String.format("member %s#%s(): can't deal with return type %s%s (not implemented yet)",
                                                     codeGenClass.getJavaName(),
                                                     methodDoc.name(),
                                                     returnType.typeName(),
                                                     returnType.dimension()));
        }
        return apiMethod;
    }

    private static void collect(CodeGenClass codeGenClass, CodeGenCallable apiMethod, HashSet<CodeGenClass> usedCodeGenClasses, Map<String, List<CodeGenCallable>> sameExternalFunctionNames) throws ClassNotFoundException, NoSuchMethodException {
        codeGenClass.addCallable(apiMethod);
        List<CodeGenCallable> apiMethods = sameExternalFunctionNames.get(apiMethod.getFunctionBaseName());
        if (apiMethods == null) {
            apiMethods = new ArrayList<CodeGenCallable>(4);
            sameExternalFunctionNames.put(apiMethod.getFunctionBaseName(), apiMethods);
        }
        apiMethods.add(apiMethod);

        if (!apiMethod.getReturnType().isPrimitive()) {
            usedCodeGenClasses.add(new CodeGenClass(apiMethod.getReturnType()));
        }
        collectUsedClassesFromParameters(apiMethod.getMemberDoc(), usedCodeGenClasses);
    }

    private static boolean isApiElement(ProgramElementDoc programElementDoc) {
        return programElementDoc.isPublic() && programElementDoc.tags("deprecated").length == 0;
    }

    private static void collectUsedClassesFromParameters(ExecutableMemberDoc memberDoc, Set<CodeGenClass> usedCodeGenClasses) throws ClassNotFoundException {
        final Parameter[] parameters = memberDoc.parameters();
        for (Parameter parameter : parameters) {
            final Type type = parameter.type();
            if (!type.isPrimitive()) {
                usedCodeGenClasses.add(new CodeGenClass(type));
            }
        }
    }

    public static CodeGenParameter[] createParameterCodeGens(CodeGenClass codeGenClass, ExecutableMemberDoc memberDoc) throws CodeGenException {
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
            } else if (isStringArray(parameterType)) {
                codeGenParameter = new CodeGenParameter.StringArray(parameter);
            } else {
                throw new CodeGenException(String.format("member %s#%s(): can't deal with parameter %s of type %s%s (not implemented yet)",
                                                         codeGenClass.getJavaName(),
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
