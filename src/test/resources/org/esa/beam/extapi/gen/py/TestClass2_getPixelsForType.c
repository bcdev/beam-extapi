PyObject* BeamPyTestClass2_getPixelsForType(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    PyObject* p1PyObj = NULL;
    jobject p1JObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getPixelsForType", "(Ljava/lang/Class;)Ljava/lang/Object;", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "O:getPixelsForType", &p1PyObj)) {
        return NULL;
    }
    {
        jboolean ok = 1;
        p1JObj = BPy_ToJObjectT(p1PyObj, BPy_Class_Class, &ok);
        if (!ok) {
            return NULL;
        }
    }
    _resultJObj = (*jenv)->CallObjectMethod(jenv, _thisJObj, _method, p1JObj);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getPixelsForType(Ljava/lang/Class;)Ljava/lang/Object;");
    _resultPyObj = BPy_FromJObject(&Object_Type, _resultJObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}
