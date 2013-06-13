PyObject* BeamPyTestClass2_getTimestamp(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getTimestamp", "()Ljava/util/Date;", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK):BeamPyTestClass2_getTimestamp", &_thisType, &_this)) {
        return NULL;
    }
    _thisJObj = (jobject) _this;
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method);
    _resultPyObj = beampy_newPyObjectFromJObject(_resultJObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}