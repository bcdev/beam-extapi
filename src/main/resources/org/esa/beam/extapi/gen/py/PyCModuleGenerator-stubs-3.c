
PyObject* beam_new_pyseq_from_${typeName}_array(const ${ctype}* elems, int length)
{
    PyObject* list;
    PyObject* item;
    int i;
    list = PyList_New(length);
    if (list == NULL) {
        return NULL;
    }
    for (i = 0; i < length; i++) {
        item = ${itemFactoryCall};
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
