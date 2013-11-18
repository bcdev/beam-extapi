PyObject* BeamPyTestClass2_getPixel1(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    jint p1 = (jint) 0;
    jint p2 = (jint) 0;
    jfloat _result = (jfloat) 0;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getPixel", "(II)F", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "ii:getPixel", &p1, &p2)) {
        return NULL;
    }
    _result = (*jenv)->CallFloatMethod(jenv, _thisJObj, _method, p1, p2);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getPixel(II)F");
    return PyFloat_FromDouble(_result);
}
