package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Norman Fomferra
 */
public class DocMock {

    public static RootDoc createRootDoc(Class<?>... classes) {
        ClassDoc[] classDocs = new  ClassDoc[classes.length];
        for (int i = 0; i < classes.length; i++) {
            classDocs[i] = createClassDoc(classes[i]);
        }
        return createRootDoc(classDocs);
    }

    public static RootDoc createRootDoc(ClassDoc... classDocs) {
        RootDoc rootDoc = mock(RootDoc.class);
        when(rootDoc.classes()).thenReturn(classDocs);
        return rootDoc;
    }

    public static ClassDoc createClassDoc(Class<?> clazz) {
        int pos = clazz.getName().lastIndexOf('.');
        String typeName = (pos > 0 ? clazz.getName().substring(pos + 1) : clazz.getName()).replace('$', '.');

        ClassDoc classDoc = mock(ClassDoc.class);
        when(classDoc.qualifiedTypeName()).thenReturn(clazz.getName());
        when(classDoc.simpleTypeName()).thenReturn(clazz.getSimpleName());
        when(classDoc.typeName()).thenReturn(typeName);
        when(classDoc.dimension()).thenReturn("");
        when(classDoc.isPrimitive()).thenReturn(clazz.isPrimitive());
        when(classDoc.isInterface()).thenReturn(clazz.isInterface());
        when(classDoc.isPublic()).thenReturn(Modifier.isPublic(clazz.getModifiers()));
        when(classDoc.isProtected()).thenReturn(Modifier.isProtected(clazz.getModifiers()));
        when(classDoc.isPrivate()).thenReturn(Modifier.isPrivate(clazz.getModifiers()));
        when(classDoc.isStatic()).thenReturn(Modifier.isStatic(clazz.getModifiers()));

        Constructor[] constructors = clazz.getConstructors();
        ConstructorDoc[] constructorDocs = new ConstructorDoc[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            constructorDocs[i] = createConstructorDoc(classDoc, constructors[i]);
        }
        when(classDoc.constructors()).thenReturn(constructorDocs);
        when(classDoc.constructors(true)).thenReturn(constructorDocs);
        when(classDoc.constructors(false)).thenReturn(constructorDocs);

        Method[] methods = clazz.getDeclaredMethods();
        MethodDoc[] methodDocs = new MethodDoc[methods.length];
        for (int i = 0; i < methods.length; i++) {
            methodDocs[i] = createMethodDoc(classDoc, methods[i]);
        }
        when(classDoc.methods()).thenReturn(methodDocs);
        when(classDoc.methods(true)).thenReturn(methodDocs);
        when(classDoc.methods(false)).thenReturn(methodDocs);

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            ClassDoc classDoc1 = createClassDoc(superclass);
            when(classDoc.superclass()).thenReturn(classDoc1);
        } else {
            when(classDoc.superclass()).thenReturn(null);
        }

        return classDoc;
    }

    private static ConstructorDoc createConstructorDoc(ClassDoc classDoc, Constructor method) {
        ConstructorDoc constructorDoc = mock(ConstructorDoc.class);
        when(constructorDoc.containingClass()).thenReturn(classDoc);
        when(constructorDoc.name()).thenReturn(method.getName());
        when(constructorDoc.parameters()).thenReturn(getParameters(method));
        when(constructorDoc.tags()).thenReturn(new Tag[0]);
        when(constructorDoc.tags("deprecated")).thenReturn(new Tag[0]);
        when(constructorDoc.isConstructor()).thenReturn(true);
        when(constructorDoc.isPublic()).thenReturn(Modifier.isPublic(method.getModifiers()));
        when(constructorDoc.isPrivate()).thenReturn(Modifier.isPrivate(method.getModifiers()));
        when(constructorDoc.isProtected()).thenReturn(Modifier.isProtected(method.getModifiers()));
        when(constructorDoc.isNative()).thenReturn(Modifier.isNative(method.getModifiers()));
        when(constructorDoc.isFinal()).thenReturn(Modifier.isFinal(method.getModifiers()));
        when(constructorDoc.isStatic()).thenReturn(Modifier.isStatic(method.getModifiers()));
        return constructorDoc;
    }

    private static MethodDoc createMethodDoc(ClassDoc classDoc, Method method) {
        MethodDoc methodDoc = mock(MethodDoc.class);
        when(methodDoc.containingClass()).thenReturn(classDoc);
        when(methodDoc.name()).thenReturn(method.getName());
        when(methodDoc.returnType()).thenReturn(new MyType(method.getReturnType()));
        when(methodDoc.parameters()).thenReturn(getParameters(method));
        when(methodDoc.tags()).thenReturn(new Tag[0]);
        when(methodDoc.isConstructor()).thenReturn(false);
        when(methodDoc.tags("deprecated")).thenReturn(new Tag[0]);
        when(methodDoc.isPublic()).thenReturn(Modifier.isPublic(method.getModifiers()));
        when(methodDoc.isPrivate()).thenReturn(Modifier.isPrivate(method.getModifiers()));
        when(methodDoc.isProtected()).thenReturn(Modifier.isProtected(method.getModifiers()));
        when(methodDoc.isAbstract()).thenReturn(Modifier.isAbstract(method.getModifiers()));
        when(methodDoc.isNative()).thenReturn(Modifier.isNative(method.getModifiers()));
        when(methodDoc.isFinal()).thenReturn(Modifier.isFinal(method.getModifiers()));
        when(methodDoc.isStatic()).thenReturn(Modifier.isStatic(method.getModifiers()));
        return methodDoc;
    }

    private static Parameter[] getParameters(Constructor method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return getParameters(parameterTypes);
    }

    private static Parameter[] getParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return getParameters(parameterTypes);
    }

    private static Parameter[] getParameters(Class<?>[] parameterTypes) {
        Parameter[] parameters = new Parameter[parameterTypes.length];
        for (int j = 0; j < parameters.length; j++) {
            parameters[j] = new MyParameter("p" + (j + 1), parameterTypes[j]);
        }
        return parameters;
    }
}
