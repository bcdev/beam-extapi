
boolean beampy_initJMethod(jmethodID* methodPtr, jclass cls, const char* className, const char* methodName, const char* methodSig, boolean isstatic)
{
    if (*methodPtr == NULL) {
        if (beam_initApi() == 0) {
            if (isstatic) {
                *methodPtr = (*jenv)->GetMethodID(jenv, cls, methodName, methodSig);
            } else {
                *methodPtr = (*jenv)->GetMethodID(jenv, cls, methodName, methodSig);
            }
            if (*methodPtr == NULL) {
                char msg[1024];
                sprintf(msg, "Java method not found: %s.%s%s", className, methodName, methodSig);
                PyErr_SetString(BeamPy_Error, msg);
                return 0;
            }
        } else {
            PyErr_SetString(BeamPy_Error, "beam_initApi() failed");
            return 0;
        }
    }
    return 1;
}
