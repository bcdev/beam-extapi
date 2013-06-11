PyObject* BeamPyTestClass2_getDuration(PyObject* self, PyObject* args)
{
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    dlong result;
    if (!PyArg_ParseTuple(args, "(sK):TestClass2_getDuration", &thisObjType, &thisObj)) {
        return NULL;
    }
    result = TestClass2_getDuration((TestClass2) thisObj);
    return PyLong_FromLongLong(result);
}