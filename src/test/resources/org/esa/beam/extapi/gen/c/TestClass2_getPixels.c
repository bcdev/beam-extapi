float* TestClass2_getPixels(TestClass2 _this, const float* p1Elems, int p1Length, int p2, int* resultArrayLength)
{
    static jmethodID _method = NULL;
    jarray p1Array = NULL;
    float* _result = (float*) 0;
    jarray _resultArray = NULL;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getPixels", "([FI)[F");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    p1Array = (*jenv)->NewFloatArray(jenv, p1Length);
    beam_copyToJArray(p1Array, p1Elems, p1Length, sizeof (float));
    _resultArray = (*jenv)->CallObjectMethod(jenv, _this, _method, p1Array, p2);
    _result = beam_newCFloatArray(_resultArray, resultArrayLength);
    (*jenv)->DeleteLocalRef(jenv, p1Array);
    (*jenv)->DeleteLocalRef(jenv, _resultArray);
    return _result;
}
