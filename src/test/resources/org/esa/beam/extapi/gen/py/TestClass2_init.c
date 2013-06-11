PyObject* BeamPyTestClass2_newTestClass2(PyObject* self, PyObject* args)
{
    void* result;
    result = TestClass2_newTestClass2();
    if (result != NULL) {
        return Py_BuildValue("(sK)", "TestClass2", (unsigned PY_LONG_LONG) result);
    } else {
        return Py_BuildValue("");
    }
}
