PyObject* BeamPyTestClass2_transformCoordinates(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    jdouble* p1Data = NULL;
    int p1Length = 0;
    PyObject* p1PyObj = NULL;
    Py_buffer p1Buf;
    jarray p1JObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "transformCoordinates", "([D)[D", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "O:transformCoordinates", &p1PyObj)) {
        return NULL;
    }
    p1PyObj = BPy_ToPrimitiveArrayBufferReadOnly(p1PyObj, &p1Buf, "d", -1);
    if (p1PyObj == NULL) {
        return NULL;
    }
    p1Data = (jdouble*) p1Buf.buf;
    p1Length = p1Buf.len / p1Buf.itemsize;
    p1JObj = BPy_NewJDoubleArrayFromBuffer(p1Data, p1Length);
    if (p1JObj == NULL) {
        return NULL;
    }
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method, p1JObj);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#transformCoordinates([D)[D");
    _resultPyObj = BPy_FromJDoubleArray((jarray) _resultJObj);
    PyBuffer_Release(&p1Buf);
    (*jenv)->DeleteLocalRef(jenv, p1JObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}
