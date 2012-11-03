package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.util.*;

/**
 * @author Norman Fomferra
 */
class ApiInfo {

    private final Map<ApiClass, List<ApiMethod>> apiClasses;
    private final Map<ApiClass, List<ApiMethod>> allClasses;
    private final Set<ApiClass> usedNonApiClasses;

    private ApiInfo(Map<ApiClass, List<ApiMethod>> allClasses,
                    Map<ApiClass, List<ApiMethod>> apiClasses,
                    Set<ApiClass> usedNonApiClasses) {
        this.allClasses = allClasses;
        this.apiClasses = apiClasses;
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

    public List<ApiMethod> getMethodsOf(ApiClass apiClass) {
        List<ApiMethod> list = apiClasses.get(apiClass);
        return list != null ? Collections.unmodifiableList(list) : null;
    }

    public List<ApiMethod> getMethodsUsing(ApiClass apiClass) {
        List<ApiMethod> list = allClasses.get(apiClass);
        return list != null ? Collections.unmodifiableList(list) : null;
    }

    public static ApiInfo create(RootDoc rootDoc, Filter filter) {
        Map<ApiClass, List<ApiMethod>> apiClasses = parseRootDoc(rootDoc, filter);
        Map<ApiClass, List<ApiMethod>> allClasses = getAllClasses(apiClasses);
        Set<ApiClass> usedNonApiClasses = getUsedNonApiClasses(apiClasses, allClasses);
        return new ApiInfo(apiClasses, allClasses, usedNonApiClasses);
    }

    private static Map<ApiClass, List<ApiMethod>> parseRootDoc(RootDoc rootDoc, Filter filter) {

        Map<ApiClass, List<ApiMethod>> callables = new HashMap<ApiClass, List<ApiMethod>>(1000);

        for (ClassDoc classDoc : rootDoc.classes()) {

            if (filter.accept(classDoc)) {

                ApiClass apiClass = new ApiClass(classDoc);
                ArrayList<ApiMethod> apiMethods = new ArrayList<ApiMethod>();

                for (ConstructorDoc constructorDoc : classDoc.constructors()) {
                    if (filter.accept(constructorDoc)) {
                        ApiMethod apiMethod = new ApiMethod(apiClass, constructorDoc);
                        apiMethods.add(apiMethod);
                    } else {
                        System.out.printf("Filtered out: constructor %s#%s()\n", classDoc.qualifiedTypeName(), constructorDoc.name());
                    }
                }

                for (MethodDoc methodDoc : classDoc.methods()) {
                    if (filter.accept(methodDoc)) {
                        ApiMethod apiMethod = new ApiMethod(apiClass, methodDoc);
                        apiMethods.add(apiMethod);
                    } else {
                        System.out.printf("Filtered out: method %s#%s()\n", classDoc.qualifiedTypeName(), methodDoc.name());
                    }
                }

                callables.put(apiClass, apiMethods);

            } else {
                System.out.printf("Filtered out: class %s\n", classDoc.qualifiedTypeName());
            }
        }

        return callables;
    }

    private static Map<ApiClass, List<ApiMethod>> getAllClasses(Map<ApiClass, List<ApiMethod>> apiClasses) {
        Map<ApiClass, List<ApiMethod>> usedClasses = new HashMap<ApiClass, List<ApiMethod>>();
        for (Map.Entry<ApiClass, List<ApiMethod>> entry : apiClasses.entrySet()) {
            for (ApiMethod apiMethod : entry.getValue()) {
                collectAllClasses(apiMethod.getReturnType(), apiMethod, usedClasses);
                for (Parameter parameter : apiMethod.getMemberDoc().parameters()) {
                    collectAllClasses(parameter.type(), apiMethod, usedClasses);
                }
            }
        }
        return usedClasses;
    }

    private static void collectAllClasses(Type type, ApiMethod apiMethod, Map<ApiClass, List<ApiMethod>> allClasses) {
        if (!type.isPrimitive()) {
            ApiClass usedClass = new ApiClass(type);
            List<ApiMethod> referencingMethods = allClasses.get(usedClass);
            if (referencingMethods == null) {
                referencingMethods = new ArrayList<ApiMethod>();
                allClasses.put(usedClass, referencingMethods);
            }
            referencingMethods.add(apiMethod);
        }
    }

    private static Set<ApiClass> getUsedNonApiClasses(Map<ApiClass, List<ApiMethod>> apiClasses,
                                                      Map<ApiClass, List<ApiMethod>> allClasses) {
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

        public DefaultFilter(Set<String> includedClassNames) {
            this.includedClassNames = includedClassNames;
        }

        public DefaultFilter(String... includedClassNames) {
            this.includedClassNames = new HashSet<String>(Arrays.asList(includedClassNames));
        }

        @Override
        public boolean accept(ClassDoc classDoc) {
            return classDoc.isPublic() && includedClassNames.contains(classDoc.qualifiedTypeName());
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

}
