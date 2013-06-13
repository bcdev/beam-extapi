// #include "beam_capi.h"
#include "${libName}.h"

#include <Python.h>
#include "structmember.h"
#include <jni.h>

#include "../beam_util.h"

static PyObject* BeamPy_Error;

/* Extra global functions for beampy. These will also go into the module definition. */
PyObject* BeamPyObject_delete(PyObject* self, PyObject* args);
PyObject* BeamPyString_newString(PyObject* self, PyObject* args);


jarray beampy_newJBooleanArray(const jboolean* data, jint length);
jarray beampy_newJByteArray(const jbyte* data, jint length);
jarray beampy_newJCharArray(const jchar* data, jint length);
jarray beampy_newJShortArray(const jshort* data, jint length);
jarray beampy_newJIntArray(const jint* data, jint length);
jarray beampy_newJLongArray(const jlong* data, jint length);
jarray beampy_newJFloatArray(const jfloat* data, jint length);
jarray beampy_newJDoubleArray(const jdouble* data, jint length);

// experimental
// PyObject* BeamPyMap_newHashMap(PyObject* self, PyObject* args);
