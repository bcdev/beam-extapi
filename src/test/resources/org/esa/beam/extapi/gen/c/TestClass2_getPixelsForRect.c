float* TestClass2_getPixelsForRect(TestClass2 _this, Rectangle2D p1, int* _resultArrayLength)
{
    static jmethodID _method = NULL;
    float* _result = (float*) 0;
    jarray _resultArray = NULL;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "getPixelsForRect", "(Ljava/awt/geom/Rectangle2D;)[F");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _resultArray = (*jenv)->CallObjectMethod(jenv, _this, _method, p1);
    _result = beam_newCFloatArray(_resultArray, _resultArrayLength);
    (*jenv)->DeleteLocalRef(jenv, _resultArray);
    return _result;
}
