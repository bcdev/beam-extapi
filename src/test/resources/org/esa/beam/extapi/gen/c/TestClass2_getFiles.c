File* TestClass2_getFiles(TestClass2 _this, const char* p1, int* _resultArrayLength)
{
    static jmethodID _method = NULL;
    jstring p1String = NULL;
    File* _result = (File*) 0;
    jarray _resultArray = NULL;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    p1String = (*jenv)->NewStringUTF(jenv, p1);
    _resultArray = (*jenv)->CallObjectMethod(jenv, _this, _method, p1String);
    _result = beam_newCObjectArray(_resultArray, _resultArrayLength);
    (*jenv)->DeleteLocalRef(jenv, p1String);
    (*jenv)->DeleteLocalRef(jenv, _resultArray);
    return _result;
}
