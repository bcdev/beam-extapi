// todo - make use of this file, currently we generate this code from resource templates
#ifndef BEAM_JOBJECT_H
#define BEAM_JOBJECT_H

#ifdef __cplusplus
extern "C" {
#endif

#include "Python.h"
#include "jni.h"

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
 * Creates a new JObject instance from a given global reference to a Java object.
 */
PyObject* JObject_FromJObjectRef(jobject jobjectRef);
PyObject* JObject_FromJObjectRefWithType(jobject jobjectRef, PyTypeObject* type);

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
jobject JObject_GetJObjectRefWithType(PyObject* anyPyObj, PyTypeObject* type);

int JObject_init(JObject* self, PyObject* args, PyObject* kwds);
void JObject_dealloc(JObject* self);



// todo - better use functions for retrieving these pointers
//PyAPI_DATA(JavaVM*) jvm;
extern JavaVM* jvm;
//PyAPI_DATA(JNIEnv*) jenv;
extern JNIEnv* jenv;

#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAMPY_JOBJECT_H */