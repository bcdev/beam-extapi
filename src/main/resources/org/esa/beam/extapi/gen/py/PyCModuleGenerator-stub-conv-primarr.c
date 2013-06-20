
/*
 * This template generates functions for converting Java primitive array to Python buffers and back.
 * Template parameters:
 *   typeUC =         ${typeUC}
 *   typeLC =         ${typeLC}
 *   bufferFormat =   ${bufferFormat}
 *   itemToElemCall = ${itemToElemCall}
 *   elemToItemCall = ${elemToItemCall}
 */

// Used in /org/esa/beam/extapi/gen/py/PyCParameterGenerator.java, PrimitiveArray
jarray beampy_newJ${typeUC}ArrayFromBuffer(const j${typeLC}* buffer, jint bufferLength)
{
    jarray arrayJObj;

    arrayJObj = (*jenv)->New${typeUC}Array(jenv, bufferLength);
    if (arrayJObj != NULL) {
        void* addr = (*jenv)->GetPrimitiveArrayCritical(jenv, arrayJObj, NULL);
        if (addr != NULL) {
            memcpy(addr, buffer, bufferLength * sizeof (j${typeLC}));
            (*jenv)->ReleasePrimitiveArrayCritical(jenv, arrayJObj, addr, 0);
        }
    }

    return arrayJObj;
}

// Used in /org/esa/beam/extapi/gen/py/PyCParameterGenerator.java, PrimitiveArray
PyObject* beampy_copyJ${typeUC}ArrayToBuffer(jarray arrayJObj, j${typeLC}* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    void* addr;
    jsize n;

    n = (*jenv)->GetArrayLength(jenv, arrayJObj);
    if (bufferLength < n) {
         PyErr_SetString(PyExc_ValueError, "array buffer too small");
         return NULL;
    }

    addr = (*jenv)->GetPrimitiveArrayCritical(jenv, arrayJObj, NULL);
    if (addr != NULL) {
        memcpy(buffer, addr, bufferLength * sizeof (j${typeLC}));
        (*jenv)->ReleasePrimitiveArrayCritical(jenv, arrayJObj, addr, 0);
    }

    return bufferPyObj;
}

// Used in /org/esa/beam/extapi/gen/py/PyCParameterGenerator.java, PrimitiveArray
PyObject* beampy_newPyObjectFromJ${typeUC}Array(jarray arrayJObj)
{
    PyObject* bufferPyObj;
    void* addr;
    jsize arrayLength;

    arrayLength = (*jenv)->GetArrayLength(jenv, arrayJObj);

    addr = (*jenv)->GetPrimitiveArrayCritical(jenv, arrayJObj, NULL);
    if (addr != NULL) {
        bufferPyObj = CArray_FromMemory("${bufferFormat}", addr, arrayLength, CArray_FreeMemory);
        (*jenv)->ReleasePrimitiveArrayCritical(jenv, arrayJObj, addr, 0);
    } else {
        bufferPyObj = NULL;
    }

    return bufferPyObj;
}

// The following code is commented out because its uses Python lists to represent Java primitive arrays.
// We now use Python buffers to do so.
// However the code might be useful at a later stage.

 /*
jarray beampy_newJ${typeUC}ArrayFromPySeq(PyObject* seq, jint* length)
{
    jarray arrayJObj;
    Py_ssize_t size;
    j${typeLC}* data;
    Py_ssize_t i;
    PyObject* item;

    size = PySequence_Size(seq);
    if (size < 0 || size >= (1 << 31)) {
        char msg[256];
        sprintf(msg, "invalid sequence size: %d", size);
        PyErr_SetString(PyExc_ValueError, msg);
        return NULL;
    }

    arrayJObj = (*jenv)->New${typeUC}Array(jenv, length);
    data = (j${typeLC}*) malloc(size * sizeof (j${typeLC}));
    if (data == NULL) {
        char msg[256];
        sprintf(msg, "out of memory while allocating j${typeLC}[%s]", size);
        PyErr_SetString(PyExc_MemoryError, msg);
        return NULL;
    }

    for (i = 0; i < size; i++) {
        item = PySequence_GetItem(seq, i);
        if (item == NULL) {
            free(elems);
            return NULL;
        }
        data[i] = ${itemToElemCall};
    }

    *length = (jint) size;
    return elems;
}
*/


/*
 * Creates a Python sequence (a list) from a C-array of primitive type ${typeName}.
 * Template parameters:
 *   typeUC =         ${typeUC}
 *   typeLC =         ${typeLC}
 *   elemToItemCall = ${elemToItemCall}
 */
 /*
PyObject* beampy_newPyListFromJ${typeUC}Array(jarray arrayJObj)
{
    jint length;
    PyObject* list;
    PyObject* item;
    jint i;
    list = PyList_New(length);
    if (list == NULL) {
        return NULL;
    }
    for (i = 0; i < length; i++) {
        item = ${elemToItemCall};
        if (item == NULL) {
            Py_DECREF(list);
            return NULL;
        }
        if (PyList_SetItem(list, i, item) != 0) {
            Py_DECREF(item);
            Py_DECREF(list);
            return NULL;
        }
    }
      return list;
  }
  */