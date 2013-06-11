PyObject* BeamPyTestClass2_getFiles(PyObject* self, PyObject* args)
{
    const char* p1;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    File* result;
    int resultLength;
    PyObject* resultSeq;
    if (!PyArg_ParseTuple(args, "(sK)s:TestClass2_getFiles", &thisObjType, &thisObj, &p1)) {
        return NULL;
    }
    result = TestClass2_getFiles((TestClass2) thisObj, p1, &resultLength);
    if (result != NULL) {
        resultSeq = beam_new_pyseq_from_jobject_array("File", result, resultLength);
        beam_release_object_array(result, resultLength);
        return resultSeq;
    } else {
        return Py_BuildValue("");
    }
}
