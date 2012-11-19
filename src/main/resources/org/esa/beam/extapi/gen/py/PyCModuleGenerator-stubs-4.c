
PyObject* beam_new_pyseq_from_jobject_array(const char* type, const void** elems, int length)
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

void** beam_new_jobject_array_from_pyseq(const char* type, PyObject* seq, int* length)
{
    /*
       todo: implement me!
       Use char* PyUnicode_AsUTF8AndSize(PyObject *unicode, Py_ssize_t *size)
       PyArg_ParseTuple(item, "(sK)", &itemType, &itemObj);
     */
    return NULL;
}


PyObject* beam_new_pyseq_from_string_array(const char** elems, int length)
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

char** beam_new_string_array_from_pyseq(PyObject* seq, int* length)
{
    /* todo: implement me!
       Use char* PyUnicode_AsUTF8AndSize(PyObject *unicode, Py_ssize_t *size)
     */
    return NULL;
}

