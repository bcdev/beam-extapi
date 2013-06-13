PyObject* BeamPyTestClass2_getFiles(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    const char* _thisType = NULL;
    unsigned PY_LONG_LONG _this = 0;
    jobject _thisJObj = NULL;
    const char* p1 = NULL;
    jstring p1JObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!beampy_initJMethod(&_method, classTestClass2, "org.esa.beam.extapi.gen.test.TestClass2", "getFiles", "(Ljava/lang/String;)[Ljava/io/File;", 0)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "(sK)s:BeamPyTestClass2_getFiles", &_thisType, &_this, &p1)) {
        return NULL;
    }
    _thisJObj = (jobject) _this;
    p1JObj =(*jenv)->NewStringUTF(jenv, p1);
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method, p1JObj);
    _resultPyObj = beampy_newPySeqFromJObjectArray((jarray) _resultJObj, "File");
    (*jenv)->DeleteLocalRef(jenv, p1JObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}