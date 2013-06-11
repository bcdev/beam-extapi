PyObject* BeamPyTestClass2_getPixelsForRect(PyObject* self, PyObject* args)
{
    const char* p1Type;
    unsigned PY_LONG_LONG p1;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    float* result;
    int resultLength;
    PyObject* resultObj;
    if (!PyArg_ParseTuple(args, "(sK)(sK):TestClass2_getPixelsForRect", &thisObjType, &thisObj, &p1Type, &p1)) {
        return NULL;
    }
    result = TestClass2_getPixelsForRect((TestClass2) thisObj, (Rectangle2D) p1, &resultLength);
    if (result != NULL) {
        resultObj = CArray_createFromItems("f", result, resultLength, beam_release_primitive_array);
        Py_INCREF(resultObj);
        return resultObj;
    } else {
        return Py_BuildValue("");
    }
}
