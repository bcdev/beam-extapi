

PyObject* BeamPyMap_newHashMap(PyObject* self, PyObject* args)
{
    PyObject* anyPyObj = NULL;
    PyObject* resPyObj = NULL;
    jobject anyJObj = NULL;
    if (!beampy_initApi()) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "O:BeamPyMap_newHashMap", &anyPyObj)) {
        return NULL;
    }
    anyJObj = beampy_newJMapFromPyObject(anyPyObj);
    if (anyJObj != NULL) {
        resPyObj = beampy_newPyObjectFromJObject(anyJObj, "Map");
        (*jenv)->DeleteLocalRef(jenv, anyJObj);
    }
    return resPyObj;
}
