
PyObject* beam_getPrimitiveArrayBuffer(PyObject* obj, Py_buffer* view, int flags, const char* format, int len)
{
    if (obj == NULL || obj == Py_None) {
        obj = CArray_createFromLength(format, len);
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
        	    PyErr_SetString(PyExc_TypeError, "illegal buffer configuration");
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

PyObject* beam_getPrimitiveArrayBufferReadOnly(PyObject* obj, Py_buffer* view, const char* format, int len)
{
    return beam_getPrimitiveArrayBuffer(obj, view, PyBUF_SIMPLE, format, len);
}


PyObject* beam_getPrimitiveArrayBufferWritable(PyObject* obj, Py_buffer* view, const char* format, int len)
{
    return beam_getPrimitiveArrayBuffer(obj, view, PyBUF_WRITABLE, format, len);
}
