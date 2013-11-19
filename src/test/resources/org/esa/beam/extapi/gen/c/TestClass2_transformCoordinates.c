double* TestClass2_transformCoordinates(TestClass2 _this, const double* p1Elems, int p1Length, int* _resultArrayLength)
{
    static jmethodID _method = NULL;
    jarray p1Array = NULL;
    double* _result = (double*) 0;
    jarray _resultArray = NULL;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetMethodID(jenv, classTestClass2, "transformCoordinates", "([D)[D");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    p1Array = (*jenv)->NewDoubleArray(jenv, p1Length);
    beam_copyToJArray(p1Array, p1Elems, p1Length, sizeof (double));
    _resultArray = (*jenv)->CallObjectMethod(jenv, _this, _method, p1Array);
    _result = beam_newCDoubleArray(_resultArray, _resultArrayLength);
    (*jenv)->DeleteLocalRef(jenv, p1Array);
    (*jenv)->DeleteLocalRef(jenv, _resultArray);
    return _result;
}
