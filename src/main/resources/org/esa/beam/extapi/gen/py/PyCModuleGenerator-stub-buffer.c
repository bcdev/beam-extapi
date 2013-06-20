
PyObject* beampy_getPrimitiveArrayBuffer(PyObject* obj, Py_buffer* view, int flags, const char* format, int len)
{
    if (obj == NULL || obj == Py_None) {
        if (len <= 0) {
            PyErr_SetString(PyExc_ValueError, "no buffer length specified");
            return NULL;
        }
        obj = CArray_FromLength(format, len);
        if (obj == NULL) {
            PyErr_SetString(PyExc_MemoryError, "out of memory");
            return NULL;
        }
    }

    if (PyObject_CheckBuffer(obj)) {
        if (PyObject_GetBuffer(obj, view, flags) == 0) {
            if (view->ndim <= 1 && (len < 0 || view->len / view->itemsize >= len)) {
                Py_INCREF(obj);
                return obj;
            } else {
                //printf("ndim=%d, len=%d, itemsize=%d, expected len=%d\n", view->ndim, view->len, view->itemsize, len);
                PyBuffer_Release(view);
                PyErr_SetString(PyExc_ValueError, "illegal buffer configuration");
                return NULL;
            }
        }  else {
            PyErr_SetString(PyExc_TypeError, "failed to access buffer");
            return NULL;
        }
    } else {
        PyErr_SetString(PyExc_TypeError, "buffer type expected");
        return NULL;
    }
}

PyObject* beampy_getPrimitiveArrayBufferReadOnly(PyObject* obj, Py_buffer* view, const char* format, int len)
{
    return beampy_getPrimitiveArrayBuffer(obj, view, PyBUF_SIMPLE, format, len);
}


PyObject* beampy_getPrimitiveArrayBufferWritable(PyObject* obj, Py_buffer* view, const char* format, int len)
{
    return beampy_getPrimitiveArrayBuffer(obj, view, PyBUF_WRITABLE, format, len);
}
