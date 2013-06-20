// todo - make use of this file, currently we generate this code from resource templates
#ifndef BEAM_JPYCONV_H
#define BEAM_JPYCONV_H

#ifdef __cplusplus
extern "C" {
#endif

#include "beampy_carray.h"
#include "beampy_jobject.h"

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Python To Java Conversion Functions

jobject BPy_ToJObject(PyObject* arg, jboolean* ok);
jobject BPy_ToJObjectT(PyObject* arg, jclass type, jboolean* ok);
jobject BPy_ToJString(PyObject* arg, jboolean* ok);

jobjectArray BPy_ToJObjectArray(PyObject* arg, jboolean* ok);
jobjectArray BPy_ToJObjectArrayT(PyObject* arg, jclass type, jboolean* ok);
jobjectArray BPy_ToJStringArray(PyObject* arg, jboolean* ok);

jobject BPy_ToJMap(PyObject* arg, jboolean* ok);
jobject BPy_ToJList(PyObject* arg, jboolean* ok);

jarray BPy_ToJBooleanArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJCharArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJByteArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJShortArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJIntArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJLongArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJFloatArray(PyObject* arg, jboolean* ok);
jarray BPy_ToJDoubleArray(PyObject* arg, jboolean* ok);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java To Python Conversion Functions

PyObject* BPy_FromJObject(PyTypeObject* type, jobject arg);
PyObject* BPy_FromJString(jstring arg);
PyObject* BPy_FromJBooleanArray(jarray arg);
PyObject* BPy_FromJCharArray(jarray arg);
PyObject* BPy_FromJByteArray(jarray arg);
PyObject* BPy_FromJShortArray(jarray arg);
PyObject* BPy_FromJIntArray(jarray arg);
PyObject* BPy_FromJLongArray(jarray arg);
PyObject* BPy_FromJFloatArray(jarray arg);
PyObject* BPy_FromJDoubleArray(jarray arg);
PyObject* BPy_FromJObjectArray(jobjectArray arg);
PyObject* BPy_FromJStringArray(jobjectArray arg);

//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAM_JPYCONV_H */