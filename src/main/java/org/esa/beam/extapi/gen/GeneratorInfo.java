package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.util.*;

/**
 * @author Norman Fomferra
 */
class GeneratorInfo {
    private RootDoc rootDoc;
    private Set<String> wrappedClassNames;

    private List<ApiClass> apiClasses;
    private Set<ApiClass> usedClasses;
    private Map<ApiMethod, String> externalFunctionNames;

    GeneratorInfo(RootDoc rootDoc, Set<String> wrappedClassNames) {
        this.rootDoc = rootDoc;
        this.wrappedClassNames = wrappedClassNames;
        apiClasses = new ArrayList<ApiClass>(100);
        usedClasses = new HashSet<ApiClass>();
    }

    public List<ApiClass> getApiClasses() {
        return apiClasses;
    }

    String getExternalFunctionName(ApiMethod apiMethod) {
        return externalFunctionNames.get(apiMethod);
    }

    public Set<ApiClass> getUsedClasses() {
        return usedClasses;
    }

    public void run() throws ClassNotFoundException, NoSuchMethodException {
        final ClassDoc[] classDocs = rootDoc.classes();

        Map<String, List<ApiMethod>> sameExternalFunctionNames = new HashMap<String, List<ApiMethod>>(1000);

        for (ClassDoc classDoc : classDocs) {

            if (classDoc.isPublic() && wrappedClassNames.contains(classDoc.qualifiedTypeName())) {

                ApiClass apiClass = new ApiClass(classDoc);
                apiClasses.add(apiClass);

                ConstructorDoc[] constructors = classDoc.constructors();
                for (ConstructorDoc constructor : constructors) {
//                     constructor.
                }

                final MethodDoc[] methodDocs = classDoc.methods();
                for (MethodDoc methodDoc : methodDocs) {
                    if (methodDoc.isPublic()) {
                        if (methodDoc.tags("deprecated").length == 0) {

                            final Type retType = methodDoc.returnType();

                            ApiMethod apiMethod = new ApiMethod(apiClass, methodDoc);
                            apiClass.addApiMethod(apiMethod);

                            List<ApiMethod> apiMethods = sameExternalFunctionNames.get(Generator.getExternalBaseName(apiMethod));
                            if (apiMethods == null) {
                                apiMethods = new ArrayList<ApiMethod>(4);
                                sameExternalFunctionNames.put(Generator.getExternalBaseName(apiMethod), apiMethods);
                            }
                            apiMethods.add(apiMethod);

                            if (!retType.isPrimitive()) {
                                usedClasses.add(new ApiClass(retType));
                            }

                            final Parameter[] parameters = methodDoc.parameters();
                            for (Parameter parameter : parameters) {
                                final Type type = parameter.type();
                                if (!type.isPrimitive()) {
                                    usedClasses.add(new ApiClass(type));
                                }
                            }
                        } else {
                            System.out.printf("Ignored deprecated method: %s.%s\n", classDoc.qualifiedTypeName(), methodDoc.name());
                        }
                    }
                }

            } else {
                System.out.printf("Ignored non-API class: %s\n", classDoc.qualifiedTypeName());
            }
        }
        Collections.sort(this.apiClasses);

        externalFunctionNames = new HashMap<ApiMethod, String>(apiClasses.size() * 100);
        for (ApiClass apiClass : apiClasses) {
            for (ApiMethod apiMethod : apiClass.getApiMethods()) {
                List<ApiMethod> apiMethods = sameExternalFunctionNames.get(Generator.getExternalBaseName(apiMethod));
                if (apiMethods.size() > 1) {
                    for (int i = 0; i < apiMethods.size(); i++) {
                        externalFunctionNames.put(apiMethods.get(i), Generator.getExternalBaseName(apiMethods.get(i)) + (i + 1));
                    }
                } else {
                    externalFunctionNames.put(apiMethods.get(0), Generator.getExternalBaseName(apiMethods.get(0)));
                }
            }
        }
    }
}
