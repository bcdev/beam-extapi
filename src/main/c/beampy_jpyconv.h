// todo - make use of this file, currently we generate this code from resource templates
#ifndef BEAM_JPYCONV_H
#define BEAM_JPYCONV_H

#ifdef __cplusplus
extern "C" {
#endif

#include "beampy_carray.h"
#include "beampy_jobject.h"

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
 * Java-To-Python Conversion Functions.
 * These are used for converting return values of JNI method calls to Python objects.
 */
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Primitive Array Conversion Functions

jarray BPy_NewJBooleanArrayFromBuffer(const jboolean* buffer, jint bufferLength);
jarray BPy_NewJCharArrayFromBuffer(const jchar* buffer, jint bufferLength);
jarray BPy_NewJByteArrayFromBuffer(const jbyte* buffer, jint bufferLength);
jarray BPy_NewJShortArrayFromBuffer(const jshort* buffer, jint bufferLength);
jarray BPy_NewJIntArrayFromBuffer(const jint* buffer, jint bufferLength);
jarray BPy_NewJLongArrayFromBuffer(const jlong* buffer, jint bufferLength);
jarray BPy_NewJFloatArrayFromBuffer(const jfloat* buffer, jint bufferLength);
jarray BPy_NewJDoubleArrayFromBuffer(const jdouble* buffer, jint bufferLength);
jarray BPy_NewJBooleanArrayFromBuffer(const jboolean* buffer, jint bufferLength);

PyObject* BPy_NewCArrayFromJPrimitiveArray(jarray arrayJObj, const char* format);

PyObject* BPy_CopyJBooleanArrayToBuffer(jarray arrayJObj, jboolean* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJCharArrayToBuffer(jarray arrayJObj, jchar* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJByteArrayToBuffer(jarray arrayJObj, jbyte* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJShortArrayToBuffer(jarray arrayJObj, jshort* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJIntArrayToBuffer(jarray arrayJObj, jint* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJLongArrayToBuffer(jarray arrayJObj, jlong* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJFloatArrayToBuffer(jarray arrayJObj, jfloat* buffer, jint bufferLength, PyObject* bufferPyObj);
PyObject* BPy_CopyJDoubleArrayToBuffer(jarray arrayJObj, jdouble* buffer, jint bufferLength, PyObject* bufferPyObj);


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

//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAM_JPYCONV_H */