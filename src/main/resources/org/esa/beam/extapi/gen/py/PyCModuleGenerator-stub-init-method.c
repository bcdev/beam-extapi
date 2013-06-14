
boolean beampy_initApi()
{
    int errCode = beam_initApi();
    if (errCode != 0) {
        char msg[64];
        sprintf(msg, "beam_initApi() failed with error code %d", errCode);
        PyErr_SetString(BeamPy_Error, msg);
        return 0;
    }
    return 1;
}

boolean beampy_initJMethod(jmethodID* methodPtr, jclass cls, const char* className, const char* methodName, const char* methodSig, boolean isstatic)
{
    //printf("methodPtr=%p, *methodPtr=%p, cls=%p, className=%s, methodName=%s, methodSig=%s, isstatic=%d\n",
    //       methodPtr,*methodPtr, cls, className, methodName, methodSig, isstatic);

    if (*methodPtr == NULL) {
        if (isstatic) {
            *methodPtr = (*jenv)->GetStaticMethodID(jenv, cls, methodName, methodSig);
        } else {
            *methodPtr = (*jenv)->GetMethodID(jenv, cls, methodName, methodSig);
        }
        if (*methodPtr == NULL) {
            char msg[1024];
            sprintf(msg, "Java method not found: %s.%s%s", className, methodName, methodSig);
            PyErr_SetString(BeamPy_Error, msg);
            return 0;
        }
    }
    return 1;
}
