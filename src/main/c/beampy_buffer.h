#ifndef BEAMPY_BUFFER_H
#define BEAMPY_BUFFER_H

#ifdef __cplusplus
extern "C" {
#endif

#include "Python.h"

PyObject* BPy_ToPrimitiveArrayBufferReadOnly(PyObject* obj, Py_buffer* view, const char* format, int len);
PyObject* BPy_ToPrimitiveArrayBufferWritable(PyObject* obj, Py_buffer* view, const char* format, int len);
PyObject* BPy_ToPrimitiveArrayBuffer(PyObject* obj, Py_buffer* view, int flags, const char* format, int len);

#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAMPY_BUFFER_H */