PyObject* BeamPyTestClass2_getTimestamp(PyObject* self, PyObject* args)
{
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    void* result;
    if (!PyArg_ParseTuple(args, "(sK):TestClass2_getTimestamp", &thisObjType, &thisObj)) {
        return NULL;
    }
    result = TestClass2_getTimestamp((TestClass2) thisObj);
    if (result != NULL) {
        return Py_BuildValue("(sK)", "Date", (unsigned PY_LONG_LONG) result);
    } else {
        return Py_BuildValue("");
    }
}
