float* TestClass2_getPixelsWithResultParam(TestClass2 _this, float* p1Elems, int p1Length, int p2, int* resultArrayLength)
{
    static jmethodID _method = NULL;
    jarray p1Array = NULL;
    float* _result = (float*) 0;
    jarray _resultArray = NULL;
    if (_method == NULL) {
        if (beam_init_api() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getPixelsWithResultParam", "([FI)[F");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    p1Array = (*jenv)->NewFloatArray(jenv, p1Length);
    _resultArray = (*jenv)->CallObjectMethod(jenv, _this, _method, p1Array, p2);
    if (p1Elems != NULL && (*jenv)->IsSameObject(jenv, p1Array, _resultArray)) {
        beam_copy_from_jarray(_resultArray, p1Elems, p1Length, sizeof (float));
        _result = p1Elems;
        if (resultArrayLength != NULL) {
            *resultArrayLength = p1Length;
        }
    } else {
        _result = beam_alloc_float_array(_resultArray, resultArrayLength);
    }
    (*jenv)->DeleteLocalRef(jenv, p1Array);
    (*jenv)->DeleteLocalRef(jenv, _resultArray);
    return _result;
}
