/*
 * Creates a Python sequence (a list) from a C-array of Java objects (type void*).
 */
PyObject* beampy_newPySeqFromCObjectArray(const char* type, const void** elems, int length)
{
    PyObject* list;
    PyObject* item;
    int i;
    list = PyList_New(length);
    if (list == NULL) {
        return NULL;
    }
    for (i = 0; i < length; i++) {
        item = Py_BuildValue("(sK)", type, (unsigned PY_LONG_LONG) elems[i]);
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

/*
 * Creates a C-array of Java objects (type void*) from a Python sequence.
 */
void** beampy_newCObjectArrayFromPySeq(const char* type, PyObject* seq, int* length)
{
    /*
       TODO: IMPLEMENT ME!
       Use char* PyUnicode_AsUTF8AndSize(PyObject *unicode, Py_ssize_t *size)
       PyArg_ParseTuple(item, "(sK)", &itemType, &itemObj);
     */
    return NULL;
}


/*
 * Creates a Python sequence (a list) from a C-array of C-strings (type const char*).
 */
PyObject* beampy_newPySeqFromCStringArray(const char** elems, int length)
{
    PyObject* list;
    PyObject* item;
    int i;
    list = PyList_New(length);
    if (list == NULL) {
        return NULL;
    }
    for (i = 0; i < length; i++) {
        item = PyUnicode_FromString(elems[i]);
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

/*
 * Creates a C-array of C-strings from a Python sequence.
 */
char** beampy_newCStringArrayFromPySeq(PyObject* seq, int* length)
{
    /*
       TODO: IMPLEMENT ME!
       Use char* PyUnicode_AsUTF8AndSize(PyObject *unicode, Py_ssize_t *size)
     */
    return NULL;
}

