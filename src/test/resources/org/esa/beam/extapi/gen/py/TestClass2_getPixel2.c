PyObject* BeamPyTestClass2_getPixel2(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    
    jobject _thisJObj = NULL;
    jint p1 = (jint) 0;
    jint p2 = (jint) 0;
    jint p3 = (jint) 0;
    jfloat _result = (jfloat) 0;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Class, "org.esa.beam.extapi.gen.test.TestClass2", "getPixel", "(III)F", 0)) {
        return NULL;
    }
    _thisJObj = JObject_AsJObjectRefT(self, BPy_TestClass2_Class);
    if (_thisJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "argument 'self' must be of type 'TestClass2' (Java object reference)");
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "iii:getPixel", &p1, &p2, &p3)) {
        return NULL;
    }
    _result = (*jenv)->CallFloatMethod(jenv, _thisJObj, _method, p1, p2, p3);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2#getPixel(III)F");
    return PyFloat_FromDouble(_result);
}
