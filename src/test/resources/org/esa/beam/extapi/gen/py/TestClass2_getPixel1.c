PyObject* BeamPyTestClass2_getPixel1(PyObject* self, PyObject* args)
{
    int p1;
    int p2;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    float result;
    if (!PyArg_ParseTuple(args, "(sK)ii:TestClass2_getPixel1", &thisObjType, &thisObj, &p1, &p2)) {
        return NULL;
    }
    result = TestClass2_getPixel1((TestClass2) thisObj, p1, p2);
    return PyFloat_FromDouble(result);
}
