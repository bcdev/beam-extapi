Date TestClass2_getTimestamp(TestClass2 _this)
{
    static jmethodID _method = NULL;
    Date _result = (Date) 0;

    if (beam_init_api() != 0) return _result;

    if (_method == NULL) {
        _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getTimestamp", "()Ljava/util/Date;");
        if (_method == NULL) return _result;
    }

    _result = (*jenv)->CallObjectMethod(jenv, _this, _method);
    return _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;
}
