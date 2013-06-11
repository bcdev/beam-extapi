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
    /** The pointer to the Java object obtained from JNI. */
    jobject objId;
} JObject;

int JObject_init(JObject* self, PyObject* args, PyObject* kwds);
void JObject_dealloc(JObject* self);

PyAPI_DATA(PyTypeObject) JObject_Type;


#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAMPY_JOBJECT_H */