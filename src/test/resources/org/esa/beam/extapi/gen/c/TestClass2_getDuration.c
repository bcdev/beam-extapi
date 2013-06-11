dlong TestClass2_getDuration(TestClass2 _this)
{
    static jmethodID _method = NULL;
    dlong _result = (dlong) 0;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getDuration", "()J");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _result = (*jenv)->CallLongMethod(jenv, _this, _method);
    return _result;
}