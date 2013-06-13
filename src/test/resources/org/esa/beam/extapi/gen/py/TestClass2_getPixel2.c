PyObject* BeamPyTestClass2_getPixel2(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    jint p1 = (jint) 0;
    jint p2 = (jint) 0;
    jint p3 = (jint) 0;
    jfloat _result = (jfloat) 0;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getPixel", "(III)F", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK)iii:BeamPyTestClass2_getPixel2", &_thisType, &_this, &p1, &p2, &p3)) {
        return NULL;
    }
    _thisJObj = (jobject) _this;
    _result = (*jenv)->CallFloatMethod(jenv, _thisJObj, _method, p1, p2, p3);
    return PyFloat_FromDouble(_result);
}