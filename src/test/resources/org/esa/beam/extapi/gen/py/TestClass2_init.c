PyObject* BeamPyTestClass2_newTestClass2(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "<init>", "()V", 0)) {
        return NULL;
    }
    _resultJObj = (*jenv)->NewObject(jenv, BPy_TestClass2_Class, _method);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#<init>()V");
    _resultPyObj = BPy_FromJObject(&TestClass2_Type, _resultJObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}