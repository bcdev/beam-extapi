PyObject* BeamPyTestClass2_newTestClass2(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!beampy_initApi()) {
        return NULL;
    }
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "<init>", "()V", 0)) {
        return NULL;
    }
    _resultJObj = (*jenv)->NewObject(jenv, classTestClass2, _method);
    _resultPyObj = beampy_newPyObjectFromJObject(_resultJObj, "TestClass2");
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}