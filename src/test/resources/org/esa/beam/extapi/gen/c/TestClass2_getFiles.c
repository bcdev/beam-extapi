File* TestClass2_getFiles(TestClass2 _this, const char* p1, int* resultArrayLength)
{
    static jmethodID _method = NULL;
    jstring p1String = NULL;
    File* _result = (File*) 0;
    jarray _resultArray = NULL;

    if (beam_init_api() != 0) return _result;

    if (_method == NULL) {
        _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getFiles", "(Ljava/lang/String;)[Ljava/io/File;");
        if (_method == NULL) return _result;
    }

    p1String = (*jenv)->NewStringUTF(jenv, p1);
    _resultArray = (*jenv)->CallObjectMethod(jenv, _this, _method, p1String);
    _result = beam_alloc_object_array(_resultArray, resultArrayLength);
    return _result;
}
