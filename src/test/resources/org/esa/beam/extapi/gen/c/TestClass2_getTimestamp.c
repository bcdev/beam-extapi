Date TestClass2_getTimestamp(TestClass2 _this)
{
    static jmethodID _method = NULL;
    Date _result = (Date) 0;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getTimestamp", "()Ljava/util/Date;");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _result = (*jenv)->CallObjectMethod(jenv, _this, _method);
    return _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;
}
