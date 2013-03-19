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
        String lengthExpr = getConfig().getLengthExpr(apiMethod.getEnclosingClass().getJavaName(),
                                                      apiMethod.getJavaName(),
                                                      apiMethod.getJavaSignature());

        if (parameterModifiers != null) {
            if (parameters.length != parameterModifiers.length) {
                throw new IllegalStateException("parameters.length != parameterModifiers.length");
            }
            for (int i = 0; i < parameters.length; i++) {
                apiParameters[i] = new ApiParameter(parameters[i], parameterModifiers[i], lengthExpr);
            }
            return apiParameters;
        } else {
            for (int i = 0; i < parameters.length; i++) {
                apiParameters[i] = new ApiParameter(parameters[i], ApiParameter.Modifier.IN, lengthExpr);
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
            String classRejectReason = getRejectReason(config, classDoc);
            if (classRejectReason == null) {
                ApiClass apiClass = new ApiClass(classDoc);
                ArrayList<ApiConstant> apiConstants = new ArrayList<ApiConstant>();
                ArrayList<ApiMethod> apiMethods = new ArrayList<ApiMethod>();

                for (ConstructorDoc constructorDoc : classDoc.constructors()) {
                    ApiMethod apiMethod = new ApiMethod(apiClass, constructorDoc);
                    String rejectReason = getRejectReason(config, apiMethod);
                    if (rejectReason == null) {
                        apiMethods.add(apiMethod);
                    } else {
                        System.out.printf("Rejected: constructor %s#%s() - reason: %s\n", classDoc.qualifiedTypeName(), constructorDoc.name(), rejectReason);
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
                        String rejectReason = getRejectReason(config, apiMethod);
                        if (rejectReason == null) {
                            if (classDoc0 == classDoc || !apiMethods.contains(apiMethod)) {
                                apiMethods.add(apiMethod);
                            }
                        } else {
                            System.out.printf("Rejected: method %s#%s() - reason: %s\n", classDoc.qualifiedTypeName(), methodDoc.name(), rejectReason);
                        }
                    }
                    classDoc0 = classDoc0.superclass();
                } while (classDoc0 != null && !isObjectClass(classDoc0));

                apiClasses.put(apiClass, new ApiMembers(apiConstants, apiMethods));
            } else {
                System.out.printf("Rejected: class %s - reason: %s\n", classDoc.qualifiedTypeName(), classRejectReason);
            }
        }

        return apiClasses;
    }

    private static String getRejectReason(ApiGeneratorConfig config, ClassDoc classDoc) {
        if (!classDoc.isPublic()) {
            return "not public";
        }
        if (!config.isApiClass(classDoc.qualifiedName())) {
            return "not specified in API config";
        }
        return null;
    }

    private static String getRejectReason(ApiGeneratorConfig config, ApiMethod apiMethod) {
        if (!apiMethod.getMemberDoc().isPublic()) {
            return "not public";
        }
        if (apiMethod.getMemberDoc().tags("deprecated").length > 0) {
            return "deprecated";
        }
        if (!config.isApiMethod(apiMethod.getEnclosingClass().getJavaName(),
                                      apiMethod.getJavaName(),
                                      apiMethod.getJavaSignature())) {
            return "not specified in API config";
        }
        return null;
    }

    private static boolean isObjectClass(Type type) {
        return type.qualifiedTypeName().equalsIgnoreCase("java.lang.Object");
    }

    private static Map<ApiClass, ApiMembers> getAllClasses(Map<ApiClass, ApiMembers> apiClasses) {
        Map<ApiClass, ApiMembers> allClasses = new HashMap<ApiClass, ApiMembers>();
        for (Map.Entry<ApiClass, ApiMembers> entry : apiClasses.entrySet()) {
            allClasses.put(entry.getKey(), new ApiMembers());
        }
        for (Map.Entry<ApiClass, ApiMembers> entry : apiClasses.entrySet()) {
            ApiMembers apiMembers = entry.getValue();
            for (ApiConstant apiConstant : apiMembers.apiConstants) {
                collectAllClasses(apiConstant.getType(), apiConstant, allClasses);
            }
            for (ApiMethod apiMethod : apiMembers.apiMethods) {
                collectAllClasses(apiMethod.getReturnType(), apiMethod, allClasses);
                for (Parameter parameter : apiMethod.getMemberDoc().parameters()) {
                    collectAllClasses(parameter.type(), apiMethod, allClasses);
                }
            }
        }
        return allClasses;
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
        TreeSet<ApiClass> usedClasses = new TreeSet<ApiClass>();
        for (ApiClass apiClass : new TreeSet<ApiClass>(allClasses.keySet())) {
            if (!apiClasses.containsKey(apiClass)) {
                System.out.printf("Used non-API class: %s\n", apiClass.getType().qualifiedTypeName());
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
