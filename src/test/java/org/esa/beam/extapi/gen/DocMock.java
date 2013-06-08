package org.esa.beam.extapi.gen;

import com.sun.javadoc.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
        when(classDoc.qualifiedName()).thenReturn(clazz.getName());
        when(classDoc.qualifiedTypeName()).thenReturn(clazz.getName());
        when(classDoc.simpleTypeName()).thenReturn(clazz.getSimpleName());
        when(classDoc.typeName()).thenReturn(typeName);
        when(classDoc.dimension()).thenReturn("");
        when(classDoc.tags()).thenReturn(new Tag[0]);
        when(classDoc.tags("deprecated")).thenReturn(new Tag[0]);
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


        Field[] fields = clazz.getDeclaredFields();
        FieldDoc[] fieldDocs = new  FieldDoc[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldDocs[i] = createFieldDoc(classDoc, fields[i]);
        }
        when(classDoc.fields()).thenReturn(fieldDocs);
        when(classDoc.fields(true)).thenReturn(fieldDocs);
        when(classDoc.fields(false)).thenReturn(fieldDocs);

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
        when(constructorDoc.isFinal()).thenReturn(Modifier.isFinal(method.getModifiers()));
        when(constructorDoc.isStatic()).thenReturn(Modifier.isStatic(method.getModifiers()));
        when(constructorDoc.isNative()).thenReturn(Modifier.isNative(method.getModifiers()));
        return constructorDoc;
    }

    private static MethodDoc createMethodDoc(ClassDoc classDoc, Method method) {
        MethodDoc methodDoc = mock(MethodDoc.class);
        when(methodDoc.containingClass()).thenReturn(classDoc);
        when(methodDoc.name()).thenReturn(method.getName());
        when(methodDoc.returnType()).thenReturn(createType(method.getReturnType()));
        when(methodDoc.parameters()).thenReturn(getParameters(method));
        when(methodDoc.tags()).thenReturn(new Tag[0]);
        when(methodDoc.isConstructor()).thenReturn(false);
        when(methodDoc.tags("deprecated")).thenReturn(new Tag[0]);
        when(methodDoc.isPublic()).thenReturn(Modifier.isPublic(method.getModifiers()));
        when(methodDoc.isPrivate()).thenReturn(Modifier.isPrivate(method.getModifiers()));
        when(methodDoc.isProtected()).thenReturn(Modifier.isProtected(method.getModifiers()));
        when(methodDoc.isFinal()).thenReturn(Modifier.isFinal(method.getModifiers()));
        when(methodDoc.isStatic()).thenReturn(Modifier.isStatic(method.getModifiers()));
        when(methodDoc.isNative()).thenReturn(Modifier.isNative(method.getModifiers()));
        when(methodDoc.isAbstract()).thenReturn(Modifier.isAbstract(method.getModifiers()));
        return methodDoc;
    }

    public static FieldDoc createFieldDoc(ClassDoc classDoc, Field field) {
        FieldDoc fieldDoc = mock(FieldDoc.class);
        when(fieldDoc.containingClass()).thenReturn(classDoc);
        when(fieldDoc.name()).thenReturn(field.getName());
        when(fieldDoc.type()).thenReturn(createType(field.getType()));
        when(fieldDoc.tags()).thenReturn(new Tag[0]);
        when(fieldDoc.isConstructor()).thenReturn(false);
        when(fieldDoc.tags("deprecated")).thenReturn(new Tag[0]);
        when(fieldDoc.isPublic()).thenReturn(Modifier.isPublic(field.getModifiers()));
        when(fieldDoc.isPrivate()).thenReturn(Modifier.isPrivate(field.getModifiers()));
        when(fieldDoc.isProtected()).thenReturn(Modifier.isProtected(field.getModifiers()));
        when(fieldDoc.isFinal()).thenReturn(Modifier.isFinal(field.getModifiers()));
        when(fieldDoc.isStatic()).thenReturn(Modifier.isStatic(field.getModifiers()));
        return fieldDoc;
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
            parameters[j] = createParameter("p" + (j + 1), parameterTypes[j]);
        }
        return parameters;
    }

    public static Parameter createParameter(String name, Class<?> type) {
        return new MyParameter(name, type);
    }

    private static class MyParameter implements Parameter {
        String name;
        Type type;

        public MyParameter(String name, Class type) {
            this.name = name;
            this.type = createType(type);
        }

        @Override
        public Type type() {
            return type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String typeName() {
            return type.typeName();
        }

        @Override
        public AnnotationDesc[] annotations() {
            return new AnnotationDesc[0];
        }

    }

    public static Type createType(Class type) {
        return new MyType(type);
    }

    private static class MyType implements Type {

        final Class baseClass;
        final int dimCount;

        public MyType(Class c) {
            this.baseClass = getBaseClass(c);
            this.dimCount = getDimCount(c);
        }


        @Override
        public String typeName() {
            int i = baseClass.getName().lastIndexOf('.');
            return (i > 0 ? baseClass.getName().substring(i + 1) : baseClass.getName()).replace('$', '.');
        }

        @Override
        public String qualifiedTypeName() {
            return packageName() + "." + typeName();
        }

        @Override
        public String simpleTypeName() {
            return baseClass.getSimpleName();
        }

        public String packageName() {
            int i = baseClass.getName().lastIndexOf('.');
            return (i > 0) ? baseClass.getName().substring(0, i) : "";
        }

        @Override
        public String dimension() {
            String s = "";
            for (int i = 0; i < dimCount; i++) {
                s += "[]";
            }
            return s;
        }

        @Override
        public boolean isPrimitive() {
            return baseClass.isPrimitive();
        }

        @Override
        public ClassDoc asClassDoc() {
            return null;
        }

        @Override
        public ParameterizedType asParameterizedType() {
            return null;
        }

        @Override
        public TypeVariable asTypeVariable() {
            return null;
        }

        @Override
        public WildcardType asWildcardType() {
            return null;
        }

        @Override
        public AnnotationTypeDoc asAnnotationTypeDoc() {
            return null;
        }

        static Class getBaseClass(Class c) {
            Class ct = c;
            while (ct.getComponentType() != null) {
                ct = ct.getComponentType();
            }
            return ct;
        }

        static int getDimCount(Class c) {
            Class ct = c;
            int n = 0;
            while (ct.getComponentType() != null) {
                ct = ct.getComponentType();
                n++;
            }
            return n;
        }

    }
}
