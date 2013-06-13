
/*
 * Template parameters:
 *   typeUC =         ${typeUC}
 *   typeLC =         ${typeLC}
 *   itemToElemCall = ${itemToElemCall}
 */


// Used in /org/esa/beam/extapi/gen/py/PyCParameterGenerator.java, PrimitiveArray
jarray beampy_newJ${typeUC}Array(const j${typeLC}* data, jint length)
{
    jarray arrayJObj = (*jenv)->New${typeUC}Array(jenv, length);
    if (arrayJObj != NULL) {
        beam_copyToJArray(arrayJObj, data, length, sizeof (j${typeLC}));
    }
    return arrayJObj;
}

// Used in /org/esa/beam/extapi/gen/py/PyCFunctionGenerator.java, PrimitiveArrayMethod
PyObject* beampy_copyJ${typeUC}ArrayToPyObject(jarray arrayJObj, const char* carrayFormat, PyObject* resultPyObj)
{
    // todo - implement me!
    PyErr_SetString(PyExc_NotImplementedError, "not implemented: beampy_copyJ${typeUC}ArrayToPyObject()");
    return NULL;
}

// Used in /org/esa/beam/extapi/gen/py/PyCFunctionGenerator.java, PrimitiveArrayMethod
PyObject* beampy_newPyObjectFromJ${typeUC}Array(jarray arrayJObj, const char* carrayFormat)
{
    // todo - implement me!
    PyErr_SetString(PyExc_NotImplementedError, "not implemented: beampy_newPyObjectFromJ${typeUC}Array()");
    return NULL;
}

/*
 * Creates C-array of primitive type ${typeName} from a Python sequence.
 * Template parameters:
 *   typeUC =         ${typeUC}
 *   typeLC =         ${typeLC}
 *   itemToElemCall = ${itemToElemCall}
 */

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