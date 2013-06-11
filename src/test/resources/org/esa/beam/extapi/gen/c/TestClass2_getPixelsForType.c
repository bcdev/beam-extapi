Object TestClass2_getPixelsForType(TestClass2 _this, Class p1)
{
    static jmethodID _method = NULL;
    Object _result = (Object) 0;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getPixelsForType", "(Ljava/lang/Class;)Ljava/lang/Object;");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _result = (*jenv)->CallObjectMethod(jenv, _this, _method, p1);
    return _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;
}
