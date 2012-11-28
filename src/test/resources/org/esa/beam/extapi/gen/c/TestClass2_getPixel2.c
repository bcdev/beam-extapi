float TestClass2_getPixel2(TestClass2 _this, int p1, int p2, int p3)
{
    static jmethodID _method = NULL;
    float _result = (float) 0;

    if (beam_init_api() != 0) return _result;

    if (_method == NULL) {
        _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getPixel", "(III)F");
        if (_method == NULL) return _result;
    }

    _result = (*jenv)->CallFloatMethod(jenv, _this, _method, p1, p2, p3);
    return _result;
}
