PyObject* BeamPyTestClass2_getDuration(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    jlong _result = (jlong) 0;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getDuration", "()J", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK):BeamPyTestClass2_getDuration", &_thisType, &_this)) {
        return NULL;
    }
    _thisJObj = (jobject) _this;
    _result = (*jenv)->CallLongMethod(jenv, _thisJObj, _method);
    return PyLong_FromLongLong(_result);
}