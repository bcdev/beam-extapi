/*
 * Creates a Python sequence (a list) from a C-array of type ${typeName}.
 * Code generated from template. Parameters:
 *   typeName =       ${typeName}
 *   ctype =          ${ctype}
 *   elemToItemCall = ${elemToItemCall}
 */
PyObject* beampy_newPySeqFromC${typeName}Array(const ${ctype}* elems, int length)
{
    PyObject* list;
    PyObject* item;
    int i;
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

/*
 * Creates C-array of type ${typeName} from a Python sequence.
 * Code generated from template. Parameters:
 *   typeName =       ${typeName}
 *   ctype =          ${ctype}
 *   elemToItemCall = ${elemToItemCall}
 */
${ctype}* beampy_newC${typeName}ArrayFromPySeq(PyObject* seq, int* length)
{
    Py_ssize_t size;
    ${ctype}* elems;
    Py_ssize_t i;
    PyObject* item;

    size = PySequence_Size(seq);
    if (size < 0 || size >= (1 << 31)) {
        char msg[80];
        sprintf(msg, "invalid sequence size: %d", size);
        PyErr_SetString(PyExc_ValueError, msg);
        return NULL;
    }

    elems = (${ctype}*) malloc(size * sizeof (${ctype}));
    if (elems == NULL) {
        char msg[80];
        sprintf(msg, "out of memory while allocating ${ctype}[%s]", size);
        PyErr_SetString(PyExc_MemoryError, msg);
        return NULL;
    }

    for (i = 0; i < size; i++) {
        item = PySequence_GetItem(seq, i);
        if (item == NULL) {
            free(elems);
            return NULL;
        }
        elems[i] = ${itemToElemCall};
    }

    *length = (int) size;
    return elems;
}
