PyObject* BeamPyTestClass2_getPixel2(PyObject* self, PyObject* args)
{
    int p1;
    int p2;
    int p3;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    float result;
    if (!PyArg_ParseTuple(args, "(sK)iii:TestClass2_getPixel2", &thisObjType, &thisObj, &p1, &p2, &p3)) {
        return NULL;
    }
    result = TestClass2_getPixel2((TestClass2) thisObj, p1, p2, p3);
    return PyFloat_FromDouble(result);
}
