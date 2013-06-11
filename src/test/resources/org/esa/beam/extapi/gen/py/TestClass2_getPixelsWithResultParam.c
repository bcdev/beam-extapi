PyObject* BeamPyTestClass2_getPixelsWithResultParam(PyObject* self, PyObject* args)
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
    if (!PyArg_ParseTuple(args, "(sK)Oi:TestClass2_getPixelsWithResultParam", &thisObjType, &thisObj, &p1Obj, &p2)) {
        return NULL;
    }
    p1Obj = beampy_getPrimitiveArrayBufferWritable(p1Obj, &p1Buf, "f", -1);
    if (p1Obj == NULL) {
        return NULL;
    }
    p1 = (float*) p1Buf.buf;
    p1Length = p1Buf.len / p1Buf.itemsize;
    result = TestClass2_getPixelsWithResultParam((TestClass2) thisObj, p1, p1Length, p2, &resultLength);
    PyBuffer_Release(&p1Buf);
    if (result != NULL) {
        Py_INCREF(p1Obj);
        return p1Obj;
    } else {
        return Py_BuildValue("");
    }
}
