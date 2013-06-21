#ifndef BEAMPY_JOBJECT_H
#define BEAMPY_JOBJECT_H

#ifdef __cplusplus
extern "C" {
#endif

#include "Python.h"
#include "jni.h"

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JObject

/**
 * Structure used to represent Java JNI objects.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
typedef struct {
    PyObject_HEAD
    /** A global reference to a Java object obtained from JNI. */
    jobject jobjectRef;
} JObject;

//PyAPI_DATA(PyTypeObject) JObject_Type;
extern PyTypeObject JObject_Type;

/**
 * Creates a new JObject instance from a given global reference to a Java object and the given type.
 * The type must be &JObject_Type or type.tp_base == &JObject_Type.
 */
PyObject* JObject_FromType(PyTypeObject* type, jobject jobjectRef);

/**
 * Creates a new JObject instance from a given global reference to a Java object.
 */
PyObject* JObject_New(jobject jobjectRef);

/**
 * Returns 1 (TRUE) if the given Python object is a JObject or a derived type. 0 (FALSE) otherwise.
 */
int JObject_Check(PyObject* anyPyObj);

/**
 * Returns a JObject pointer if the given Python object is a JObject or derived type. NULL otherwise.
 */
JObject* JObject_AsJObject(PyObject* anyPyObj);

/**
 * Returns a global reference to a Java object if the given Python object is a JObject or derived type. NULL otherwise.
 */
jobject JObject_GetJObjectRef(PyObject* anyPyObj);

/**
 * Returns a global reference to a Java object if the given Python object is a JObject or derived type and if the Java
 * object is an instance of the given Java class. NULL otherwise.
 */
jobject JObject_GetJObjectRefInstanceOf(PyObject* anyPyObj, jclass requestedType);


// todo - check if we need them here (should be local in C-file)
//        used in derived type decls, but maybe PyType_Ready does the job?
int JObject_init(JObject* self, PyObject* args, PyObject* kwds);
void JObject_dealloc(JObject* self);


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JObjectArray

typedef JObject JObjectArray;

///PyAPI_DATA(PyTypeObject) JObject_Type;
extern PyTypeObject JObjectArray_Type;

/**
 * Creates a new JObjectArray instance from a given global reference to a Java object.
 */
PyObject* JObjectArray_New(jobjectArray jobjectArrayRef);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JVM access

// todo - better use functions for retrieving these pointers
//PyAPI_DATA(JavaVM*) jvm;
extern JavaVM* jvm;
//PyAPI_DATA(JNIEnv*) jenv;
extern JNIEnv* jenv;

//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAMPY_JOBJECT_H */