PyObject* BeamPyTestClass2_getPixel1(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    jint p1 = (jint) 0;
    jint p2 = (jint) 0;
    jfloat _result = (jfloat) 0;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getPixel", "(II)F", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK)ii:BeamPyTestClass2_getPixel1", &_thisType, &_this, &p1, &p2)) {
        return NULL;
    }
    _thisJObj = (jobject) _this;
    _result = (*jenv)->CallFloatMethod(jenv, _thisJObj, _method, p1, p2);
    return PyFloat_FromDouble(_result);
}