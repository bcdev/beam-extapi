PyObject* BeamPyTestClass2_getPixelsWithResultParam(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    jfloat* p1Data = NULL;
    int p1Length = 0;
    PyObject* p1PyObj = NULL;
    Py_buffer p1Buf;
    jarray p1JObj = NULL;
    jint p2 = (jint) 0;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getPixelsWithResultParam", "([FI)[F", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "Oi:getPixelsWithResultParam", &p1PyObj, &p2)) {
        return NULL;
    }
    p1PyObj = BPy_ToPrimitiveArrayBufferWritable(p1PyObj, &p1Buf, "f", -1);
    if (p1PyObj == NULL) {
        return NULL;
    }
    p1Data = (jfloat*) p1Buf.buf;
    p1Length = p1Buf.len / p1Buf.itemsize;
    p1JObj = BPy_NewJFloatArrayFromBuffer(p1Data, p1Length);
    if (p1JObj == NULL) {
        return NULL;
    }
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method, p1JObj, p2);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getPixelsWithResultParam([FI)[F");
    if (p1Data != NULL && (*jenv)->IsSameObject(jenv, _resultJObj, p1JObj)) {
        _resultPyObj = BPy_CopyJFloatArrayToBuffer((jarray) p1JObj, p1Data, p1Length, p1PyObj);
    } else {
        _resultPyObj = BPy_FromJFloatArray((jarray) p1JObj);
    }
    PyBuffer_Release(&p1Buf);
    (*jenv)->DeleteLocalRef(jenv, p1JObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}
