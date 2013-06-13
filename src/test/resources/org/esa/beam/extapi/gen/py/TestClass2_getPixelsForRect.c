PyObject* BeamPyTestClass2_getPixelsForRect(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    const char* p1Type = NULL;
    unsigned PY_LONG_LONG p1 = 0;
    jobject p1JObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getPixelsForRect", "(Ljava/awt/geom/Rectangle2D;)[F", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK)(sK):BeamPyTestClass2_getPixelsForRect", &_thisType, &_this, &p1Type, &p1)) {
        return NULL;
    }
    _thisJObj = (jobject) _this;
    p1JObj = (jobject) p1;
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method, p1JObj);
    _resultPyObj = beampy_newPyObjectFromJFloatArray((jarray) _resultJObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}