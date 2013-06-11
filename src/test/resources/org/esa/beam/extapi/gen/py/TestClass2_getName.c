PyObject* BeamPyTestClass2_getName(PyObject* self, PyObject* args)
{
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    char* result;
    PyObject* resultStr;
    if (!PyArg_ParseTuple(args, "(sK):TestClass2_getName", &thisObjType, &thisObj)) {
        return NULL;
    }
    result = TestClass2_getName((TestClass2) thisObj);
    if (result != NULL) {
        resultStr = PyUnicode_FromString(result);
        beam_release_string(result);
        return resultStr;
    } else {
        return Py_BuildValue("");
    }
}
