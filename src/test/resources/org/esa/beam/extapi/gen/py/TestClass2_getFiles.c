PyObject* BeamPyTestClass2_getFiles(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    const char* p1 = NULL;
    jstring p1JObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getFiles", "(Ljava/lang/String;)[Ljava/io/File;", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "s:getFiles", &p1)) {
        return NULL;
    }
    p1JObj =(*jenv)->NewStringUTF(jenv, p1);
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method, p1JObj);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getFiles(Ljava/lang/String;)[Ljava/io/File;");
    _resultPyObj = BPy_FromJObjectArray((jarray) _resultJObj);
    (*jenv)->DeleteLocalRef(jenv, p1JObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}
