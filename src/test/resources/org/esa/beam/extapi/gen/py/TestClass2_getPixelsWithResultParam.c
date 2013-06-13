PyObject* BeamPyTestClass2_getPixelsWithResultParam(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    float*   p1Data = NULL;
    int        p1Length = 0;
    PyObject*  p1PyObj = NULL;
    Py_buffer  p1Buf;
    jarray p1JObj = NULL;
    jint p2 = (jint) 0;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getPixelsWithResultParam", "([FI)[F", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK)Oi:BeamPyTestClass2_getPixelsWithResultParam", &_thisType, &_this, &p1PyObj, &p2)) {
        return NULL;
    }
    p1PyObj = beampy_getPrimitiveArrayBufferWritable(p1PyObj, &p1Buf, "f", -1);
    if (p1PyObj == NULL) {
        return NULL;
    }
    p1Data = (float*) p1Buf.buf;
    p1Length = p1Buf.len / p1Buf.itemsize;
    p1JObj = beampy_newJFloatArrayFromBuffer(p1Data, p1Length);
    if (p1JObj == NULL) {
        return NULL;
    }
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _this, _method, p1JObj, p2);
    _resultPyObj = beampy_copyJFloatArrayToPyObject((jarray) _resultJObj, "f", p1PyObj);
    if (p1Data != NULL && (*jenv)->IsSameObject(jenv, p1JObj, _resultJObj)) {
        beam_copyFromJArray(_resultJObj, p1Data, p1Length, sizeof (float));
        _resultPyObj = p1PyObj;
    } else {
        _resultPyObj = beampy_newJFloatArray(_resultArray, ${r}ArrayLength);
    }
    PyBuffer_Release(&p1Buf);
    (*jenv)->DeleteLocalRef(jenv, p1JObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}