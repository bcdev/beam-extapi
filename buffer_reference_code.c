PyObject* CArray_createNewInstance(const char* type_code, int length) {
	PyTypeObject* type = &CArray_Type;
	CArrayObj* self;
	void* elems;
	int elem_size;

	elem_size = CArray_getElemSize(type_code);
	if (elem_size <= 0) {
	    return NULL;
	}

    elems = calloc(elem_size, length);
    if (elems == NULL) {
        PyErr_SetString(PyExc_MemoryError, "out of memory");
        return NULL;
    }

	if (length <= 0) {
        PyErr_SetString(PyExc_ValueError, "length must be > 0");
        return NULL;
	}

    self = (CArrayObj*) type->tp_alloc(type, 0);
    CArray_initInstance(self, type_code, elems, elem_size, length, CArray_releaseElements);
    Py_INCREF(self);
    return (PyObject*) self;
}

PyObject* BeamPyBand_readPixelsFloat(PyObject* self, PyObject* args)
{
    int x;
    int y;
    int w;
    int h;
    float* pixels = NULL;
    int pixelsLength;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    int resultLength;
    PyObject* resultBufObj;

    if (!PyArg_ParseTuple(args, "(sK)iiiiO:Band_readPixelsFloat", &thisObjType, &thisObj, &x, &y, &w, &h, &resultBufObj)) {
        return NULL;
    }

    if (resultBufObj == Py_None) {
        CArrayObj* carray;
        resultBufObj = CArray_createNewInstance("F", w * h);
        carray = (CArrayObj*) resultBufObj;
        if (carray != NULL) {
            pixels = (float*) carray->elems;
            pixelsLength = w * h;
            Band_readPixelsFloat((Band) thisObj, x, y, w, h, pixels, pixelsLength, &resultLength);
            return resultBufObj;
        } else {
            Py_DECREF(resultBufObj);
    	    PyErr_SetString(PyExc_MemoryError, "out of memory");
            return NULL;
        }
    }

    if (PyObject_CheckBuffer(resultBufObj)) {
        Py_buffer buffer;
        if (PyObject_GetBuffer(resultBufObj, &buffer, PyBUF_WRITABLE) == 0) {
            if (buffer.ndim <= 1 && buffer.len / buffer.itemsize >= w * h) {
                float* result;
                pixels = (float*) buffer.buf;
                pixelsLength = w * h;
                result = Band_readPixelsFloat((Band) thisObj, x, y, w, h, pixels, pixelsLength, &resultLength);
                PyBuffer_Release(&buffer);
                Py_INCREF(resultBufObj);
                return resultBufObj;
            } else {
                PyBuffer_Release(&buffer);
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

PyObject* BeamPyBand_writePixelsFloat(PyObject* self, PyObject* args)
{
    int x;
    int y;
    int w;
    int h;
    float* pixels;
    int pixelsLength;
    PyObject* pixelsBuf;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;

    if (!PyArg_ParseTuple(args, "(sK)iiiiO:Band_writePixelsFloat", &thisObjType, &thisObj, &x, &y, &w, &h, &pixelsBuf)) {
        return NULL;
    }

    if (PyObject_CheckBuffer(pixelsBuf)) {
        Py_buffer buffer;
        if (PyObject_GetBuffer(pixelsBuf, &buffer, PyBUF_SIMPLE) == 0) {
            if (buffer.ndim <= 1 && buffer.len / buffer.itemsize >= w * h) {
                float* result;
                pixels = (float*) buffer.buf;
                pixelsLength = w * h;
                Band_writePixelsFloat((Band) thisObj, x, y, w, h, pixels, pixelsLength);
                PyBuffer_Release(&buffer);
                return Py_BuildValue("");
            } else {
                PyBuffer_Release(&buffer);
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
