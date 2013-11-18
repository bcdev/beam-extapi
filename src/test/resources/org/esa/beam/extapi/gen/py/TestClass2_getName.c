PyObject* BeamPyTestClass2_getName(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getName", "()Ljava/lang/String;", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getName()Ljava/lang/String;");
    _resultPyObj = BPy_FromJString((jstring) _resultJObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}
