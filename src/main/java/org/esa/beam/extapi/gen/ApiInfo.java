package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.util.*;

/**
 * Collection of API classes and their methods.
 * Immutable object.
 *
 * @author Norman Fomferra
 */
public final class ApiInfo {
    private final ApiGeneratorConfig config;
    private final Map<ApiClass, ApiMembers> apiClasses;
    private final Map<ApiClass, ApiMembers> allClasses;
    private final Set<ApiClass> usedNonApiClasses;

    private ApiInfo(ApiGeneratorConfig config,
                    Map<ApiClass, ApiMembers> apiClasses,
                    Map<ApiClass, ApiMembers> allClasses,
                    Set<ApiClass> usedNonApiClasses) {
        this.config = config;
        this.apiClasses = apiClasses;
        this.allClasses = allClasses;
        this.usedNonApiClasses = usedNonApiClasses;
    }

    public ApiGeneratorConfig getConfig() {
        return config;
    }

    public Set<ApiClass> getApiClasses() {
        return Collections.unmodifiableSet(apiClasses.keySet());
    }

    public Set<ApiClass> getAllClasses() {
        return Collections.unmodifiableSet(allClasses.keySet());
    }

    public Set<ApiClass> getUsedNonApiClasses() {
        return Collections.unmodifiableSet(usedNonApiClasses);
    }

    public List<ApiConstant> getConstantsOf(ApiClass apiClass) {
        ApiMembers members = apiClasses.get(apiClass);
        return Collections.unmodifiableList(members != null ? members.apiConstants : new ArrayList<ApiConstant>(0));
    }

    public List<ApiMethod> getMethodsOf(ApiClass apiClass) {
        ApiMembers members = apiClasses.get(apiClass);
        return Collections.unmodifiableList(members != null ? members.apiMethods : new ArrayList<ApiMethod>(0));
    }

    public List<ApiConstant> getConstantsUsing(ApiClass apiClass) {
        ApiMembers members = allClasses.get(apiClass);
        return Collections.unmodifiableList(members != null ? members.apiConstants : new ArrayList<ApiConstant>(0));
    }

    public List<ApiMethod> getMethodsUsing(ApiClass apiClass) {
        ApiMembers members = allClasses.get(apiClass);
        return Collections.unmodifiableList(members != null ? members.apiMethods : new ArrayList<ApiMethod>(0));
    }

    public ApiParameter[] getParametersFor(ApiMethod apiMethod) {
        Parameter[] parameters = apiMethod.getMemberDoc().parameters();
        ApiParameter[] apiParameters = new ApiParameter[parameters.length];
        ApiParameter.Modifier[] parameterModifiers = getConfig().getParameterModifiers(apiMethod.getEnclosingClass().getJavaName(),
                                                                                       apiMethod.getJavaName(),
                                                                                       apiMethod.getJavaSignature());

        if (parameterModifiers != null) {
            if (parameters.length != parameterModifiers.length) {
                throw new IllegalStateException("parameters.length != parameterModifiers.length");
            }
            for (int i = 0; i < parameters.length; i++) {
                apiParameters[i] = new ApiParameter(parameters[i], parameterModifiers[i]);
            }
            return apiParameters;
        } else {
            for (int i = 0; i < parameters.length; i++) {
                apiParameters[i] = new ApiParameter(parameters[i], ApiParameter.Modifier.IN);
            }
            return apiParameters;
        }
    }


    public static ApiInfo create(ApiGeneratorConfig config, RootDoc rootDoc) {
        Map<ApiClass, ApiMembers> apiClasses = getApiMembers(rootDoc, config);
        Map<ApiClass, ApiMembers> allClasses = getAllClasses(apiClasses);
        Set<ApiClass> usedNonApiClasses = getUsedNonApiClasses(apiClasses, allClasses);
        return new ApiInfo(config, apiClasses, allClasses, usedNonApiClasses);
    }

    private static Map<ApiClass, ApiMembers> getApiMembers(RootDoc rootDoc, ApiGeneratorConfig config) {

        Map<ApiClass, ApiMembers> apiClasses = new HashMap<ApiClass, ApiMembers>(1000);

        for (ClassDoc classDoc : rootDoc.classes()) {
            final boolean isPublic = classDoc.isPublic();
            final boolean isApiClass = config.isApiClass(classDoc.qualifiedName());
            if (isPublic && isApiClass) {
                ApiClass apiClass = new ApiClass(classDoc);
                ArrayList<ApiConstant> apiConstants = new ArrayList<ApiConstant>();
                ArrayList<ApiMethod> apiMethods = new ArrayList<ApiMethod>();

                for (ConstructorDoc constructorDoc : classDoc.constructors()) {
                    ApiMethod apiMethod = new ApiMethod(apiClass, constructorDoc);
                    if (isApiMethod(config, apiMethod)) {
                        apiMethods.add(apiMethod);
                    } else {
                        System.out.printf("Filtered out: constructor %s#%s()\n", classDoc.qualifiedTypeName(), constructorDoc.name());
                    }
                }

                ClassDoc classDoc0 = classDoc;
                do {
                    for (FieldDoc fieldDoc : classDoc0.fields()) {
                        if (fieldDoc.isPublic() && fieldDoc.isStatic() && fieldDoc.isFinal()) {
                            apiConstants.add(new ApiConstant(apiClass, fieldDoc));
                        }
                    }

                    for (MethodDoc methodDoc : classDoc0.methods()) {
                        ApiMethod apiMethod = new ApiMethod(apiClass, methodDoc);
                        if (isApiMethod(config, apiMethod)) {
                            if (classDoc0 == classDoc || !apiMethods.contains(apiMethod)) {
                                apiMethods.add(apiMethod);
                            }
                        } else {
                            System.out.printf("Filtered out: method %s#%s()\n", classDoc.qualifiedTypeName(), methodDoc.name());
                        }
                    }
                    classDoc0 = classDoc0.superclass();
                } while (classDoc0 != null && !isObjectClass(classDoc0));

                apiClasses.put(apiClass, new ApiMembers(apiConstants, apiMethods));
            } else {
                System.out.printf("Filtered out: class %s\n", classDoc.qualifiedTypeName());
            }
        }

        return apiClasses;
    }

    private static boolean isApiMethod(ApiGeneratorConfig config, ApiMethod apiMethod) {
        return apiMethod.getMemberDoc().isPublic()
                && apiMethod.getMemberDoc().tags("deprecated").length == 0
                && config.isApiMethod(apiMethod.getEnclosingClass().getJavaName(),
                                      apiMethod.getJavaName(),
                                      apiMethod.getJavaSignature());
    }

    private static boolean isObjectClass(Type type) {
        return type.qualifiedTypeName().equalsIgnoreCase("java.lang.Object");
    }

    private static Map<ApiClass, ApiMembers> getAllClasses(Map<ApiClass, ApiMembers> apiClasses) {
        Map<ApiClass, ApiMembers> usedClasses = new HashMap<ApiClass, ApiMembers>();
        for (Map.Entry<ApiClass, ApiMembers> entry : apiClasses.entrySet()) {
            ApiMembers apiMembers = entry.getValue();
            for (ApiConstant apiConstant : apiMembers.apiConstants) {
                collectAllClasses(apiConstant.getType(), apiConstant, usedClasses);
            }
            for (ApiMethod apiMethod : apiMembers.apiMethods) {
                collectAllClasses(apiMethod.getReturnType(), apiMethod, usedClasses);
                for (Parameter parameter : apiMethod.getMemberDoc().parameters()) {
                    collectAllClasses(parameter.type(), apiMethod, usedClasses);
                }
            }
        }
        return usedClasses;
    }

    private static void collectAllClasses(Type type, ApiConstant apiConstant, Map<ApiClass, ApiMembers> allClasses) {
        if (!type.isPrimitive()) {
            ApiMembers referencingMembers = getApiMembers(allClasses, type);
            referencingMembers.apiConstants.add(apiConstant);
        }
    }

    private static void collectAllClasses(Type type, ApiMethod apiMethod, Map<ApiClass, ApiMembers> allClasses) {
        if (!type.isPrimitive()) {
            ApiMembers referencingMembers = getApiMembers(allClasses, type);
            referencingMembers.apiMethods.add(apiMethod);
        }
    }

    private static ApiMembers getApiMembers(Map<ApiClass, ApiMembers> allClasses, Type type) {
        ApiClass usedClass = new ApiClass(type);
        ApiMembers referencingMembers = allClasses.get(usedClass);
        if (referencingMembers == null) {
            referencingMembers = new ApiMembers();
            allClasses.put(usedClass, referencingMembers);
        }
        return referencingMembers;
    }

    private static Set<ApiClass> getUsedNonApiClasses(Map<ApiClass, ApiMembers> apiClasses,
                                                      Map<ApiClass, ApiMembers> allClasses) {
        HashSet<ApiClass> usedClasses = new HashSet<ApiClass>();
        for (ApiClass apiClass : allClasses.keySet()) {
            if (!apiClasses.containsKey(apiClass)) {
                usedClasses.add(apiClass);
            }
        }
        return usedClasses;
    }

    private final static class ApiMembers {
        private final List<ApiConstant> apiConstants;
        private final List<ApiMethod> apiMethods;

        private ApiMembers() {
            this.apiConstants = new ArrayList<ApiConstant>();
            this.apiMethods = new ArrayList<ApiMethod>();
        }

        private ApiMembers(List<ApiConstant> apiConstants, List<ApiMethod> apiMethods) {
            this.apiConstants = apiConstants;
            this.apiMethods = apiMethods;
        }
    }

}
