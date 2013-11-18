PyObject* BeamPyTestClass2_Geom_transform(PyObject* self, PyObject* args)
{
    static jmethodID _method = NULL;
    PyObject* p1PyObj = NULL;
    jobject p1JObj = NULL;
    PyObject* _resultPyObj = NULL;
    jobject _resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!BPy_InitJMethod(&_method, BPy_TestClass2_Geom_Class, "org.esa.beam.extapi.gen.test.TestClass2$Geom", "transform", "(Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;)Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;", 1)) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "O:transform", &p1PyObj)) {
        return NULL;
    }
    {
        jboolean ok = 1;
        p1JObj = BPy_ToJObjectT(p1PyObj, BPy_TestClass2_Geom_Class, &ok);
        if (!ok) {
            return NULL;
        }
    }
    _resultJObj = (*jenv)->CallStaticObjectMethod(jenv, BPy_TestClass2_Geom_Class, _method, p1JObj);
    CHECK_JVM_EXCEPTION("org.esa.beam.extapi.gen.test.TestClass2$Geom#transform(Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;)Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;");
    _resultPyObj = BPy_FromJObject(&TestClass2_Geom_Type, _resultJObj);
    (*jenv)->DeleteLocalRef(jenv, _resultJObj);
    return _resultPyObj;
}
