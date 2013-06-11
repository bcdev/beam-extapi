PyObject* BeamPyTestClass2_getPixelsForType(PyObject* self, PyObject* args)
{
    const char* p1Type;
    unsigned PY_LONG_LONG p1;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    void* result;
    if (!PyArg_ParseTuple(args, "(sK)(sK):TestClass2_getPixelsForType", &thisObjType, &thisObj, &p1Type, &p1)) {
        return NULL;
    }
    result = TestClass2_getPixelsForType((TestClass2) thisObj, (Class) p1);
    if (result != NULL) {
        return Py_BuildValue("(sK)", "Object", (unsigned PY_LONG_LONG) result);
    } else {
        return Py_BuildValue("");
    }
}
