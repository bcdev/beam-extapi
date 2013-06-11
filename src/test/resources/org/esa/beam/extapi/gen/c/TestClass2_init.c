TestClass2 TestClass2_newTestClass2()
{
    static jmethodID _method = NULL;
    TestClass2 _result = (TestClass2) 0;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "<init>", "()V");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _result = (*jenv)->NewObject(jenv, classTestClass2, _method);
    return _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;
}
