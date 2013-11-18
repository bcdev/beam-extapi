PyObject* BeamPyTestClass2_getDuration(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    jlong _result = (jlong) 0;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getDuration", "()J", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    _result = (*jenv)->CallLongMethod(jenv, _thisJObj, _method);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getDuration()J");
    return PyLong_FromLongLong(_result);
}
