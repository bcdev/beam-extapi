PyObject* BeamPyTestClass2_getPixels(PyObject* self, PyObject* args)
{
    float* p1;
    int p1Length;
    PyObject* p1Obj;
    Py_buffer p1Buf;
    int p2;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    float* result;
    int resultLength;
    PyObject* resultObj;
    if (!PyArg_ParseTuple(args, "(sK)Oi:TestClass2_getPixels", &thisObjType, &thisObj, &p1Obj, &p2)) {
        return NULL;
    }
    p1Obj = beam_getPrimitiveArrayBufferReadOnly(p1Obj, &p1Buf, "f", -1);
    if (p1Obj == NULL) {
        return NULL;
    }
    p1 = (float*) p1Buf.buf;
    p1Length = p1Buf.len / p1Buf.itemsize;
    result = TestClass2_getPixels((TestClass2) thisObj, p1, p1Length, p2, &resultLength);
    PyBuffer_Release(&p1Buf);
    if (result != NULL) {
        resultObj = CArray_createFromItems("f", result, resultLength, beam_release_primitive_array);
        Py_INCREF(resultObj);
        return resultObj;
    } else {
        return Py_BuildValue("");
    }
}
