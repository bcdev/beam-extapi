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

    private final Map<ApiClass, ApiMembers> apiClasses;
    private final Map<ApiClass, ApiMembers> allClasses;
    private final Set<ApiClass> usedNonApiClasses;

    private ApiInfo(Map<ApiClass, ApiMembers> apiClasses,
                    Map<ApiClass, ApiMembers> allClasses,
                    Set<ApiClass> usedNonApiClasses) {
        this.apiClasses = apiClasses;
        this.allClasses = allClasses;
        this.usedNonApiClasses = usedNonApiClasses;
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

    /*
    public ApiParameter[] getParametersOf(ApiMethod apiMethod) {
        Parameter[] parameters = apiMethod.getMemberDoc().parameters();
        ApiParameter[] apiParameters = new ApiParameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            ApiParameter.Modifier modifier = extraInfo.getParameterModifier(apiMethod, parameters[i], i);
            apiParameters[i] = new ApiParameter(parameters[i], modifier);
        }
        return apiParameters;
    }
    */

    public static ApiInfo create(RootDoc rootDoc, String ... classNames) {
        Map<ApiClass, ApiMembers> apiClasses = getApiMembers(rootDoc, new DefaultFilter(classNames));
        Map<ApiClass, ApiMembers> allClasses = getAllClasses(apiClasses);
        Set<ApiClass> usedNonApiClasses = getUsedNonApiClasses(apiClasses, allClasses);
        return new ApiInfo(apiClasses, allClasses, usedNonApiClasses);
    }

    public static ApiInfo create(RootDoc rootDoc, Filter filter) {
        Map<ApiClass, ApiMembers> apiClasses = getApiMembers(rootDoc, filter);
        Map<ApiClass, ApiMembers> allClasses = getAllClasses(apiClasses);
        Set<ApiClass> usedNonApiClasses = getUsedNonApiClasses(apiClasses, allClasses);
        return new ApiInfo(apiClasses, allClasses, usedNonApiClasses);
    }

    private static Map<ApiClass, ApiMembers> getApiMembers(RootDoc rootDoc, Filter filter) {

        Map<ApiClass, ApiMembers> apiClasses = new HashMap<ApiClass, ApiMembers>(1000);

        for (ClassDoc classDoc : rootDoc.classes()) {
            if (filter.accept(classDoc)) {
                ApiClass apiClass = new ApiClass(classDoc);
                ArrayList<ApiConstant> apiConstants = new ArrayList<ApiConstant>();
                ArrayList<ApiMethod> apiMethods = new ArrayList<ApiMethod>();

                for (ConstructorDoc constructorDoc : classDoc.constructors()) {
                    if (filter.accept(constructorDoc)) {
                        ApiMethod apiMethod = new ApiMethod(apiClass, constructorDoc);
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
                        if (filter.accept(methodDoc)) {
                            ApiMethod apiMethod = new ApiMethod(apiClass, methodDoc);
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

    public interface Filter {
        boolean accept(ClassDoc classDoc);

        boolean accept(ConstructorDoc constructorDoc);

        boolean accept(MethodDoc methodDoc);
    }

    public static class DefaultFilter implements Filter {
        final Set<String> includedClassNames;

        public DefaultFilter(String... includedClassNames) {
            this.includedClassNames = new HashSet<String>(Arrays.asList(includedClassNames));
        }

        @Override
        public boolean accept(ClassDoc classDoc) {
            return classDoc.isPublic() && (includedClassNames.isEmpty() || includedClassNames.contains(classDoc.qualifiedTypeName()));
        }

        @Override
        public boolean accept(ConstructorDoc constructorDoc) {
            return constructorDoc.isPublic() && constructorDoc.tags("deprecated").length == 0;
        }

        @Override
        public boolean accept(MethodDoc methodDoc) {
            return methodDoc.isPublic() && methodDoc.tags("deprecated").length == 0;
        }
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
