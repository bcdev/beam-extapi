TestClass2_Geom TestClass2_Geom_transform(TestClass2_Geom p1)
{
    static jmethodID _method = NULL;
    TestClass2_Geom _result = (TestClass2_Geom) 0;
    if (_method == NULL) {
        if (beam_initApi() == 0) {
            _method = (*jenv)->GetStaticMethodID(jenv, classTestClass2_Geom, "transform", "(Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;)Lorg/esa/beam/extapi/gen/test/TestClass2$Geom;");
            if (_method == NULL) {
                /* Set global error */
            }
        }
        if (_method == NULL) {
            return _result;
        }
    }
    _result = (*jenv)->CallStaticObjectMethod(jenv, classTestClass2_Geom, _method, p1);
    return _result != NULL ? (*jenv)->NewGlobalRef(jenv, _result) : NULL;
}
