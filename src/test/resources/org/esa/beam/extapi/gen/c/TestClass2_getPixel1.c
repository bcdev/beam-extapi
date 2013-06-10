float TestClass2_getPixel1(TestClass2 _this, int p1, int p2)
{
    static jmethodID _method = NULL;
    float _result = (float) 0;
    if (_method == NULL) {
        if (beam_init_api() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getPixel", "(II)F");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _result = (*jenv)->CallFloatMethod(jenv, _this, _method, p1, p2);
    return _result;
}
