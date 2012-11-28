char* TestClass2_getName(TestClass2 _this)
{
    static jmethodID _method = NULL;
    char* _result = (char*) 0;
    jstring _resultString = NULL;

    if (beam_init_api() != 0) return _result;

    if (_method == NULL) {
        _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getName", "()Ljava/lang/String;");
        if (_method == NULL) return _result;
    }

    _resultString = (*jenv)->CallObjectMethod(jenv, _this, _method);
    _result = beam_alloc_string(_resultString);
    return _result;
}
