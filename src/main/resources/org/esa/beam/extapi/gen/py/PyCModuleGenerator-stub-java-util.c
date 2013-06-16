// todo - the following functions will not be used anymore once we use JObject subtypes

PyObject* BeamPyString_newString(PyObject* self, PyObject* args)
{
    const char* str;
    void* result;

    if (!PyArg_ParseTuple(args, "s:newString", &str)) {
        return NULL;
    }

    result = String_newString(str);

    if (result != NULL) {
        return Py_BuildValue("(sK)", "String", (unsigned PY_LONG_LONG) result);
    } else {
        return Py_BuildValue("");
    }
}

PyObject* BeamPyObject_delete(PyObject* self, PyObject* args)
{
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    if (!PyArg_ParseTuple(args, "(sK):Object_delete", &thisObjType, &thisObj)) {
        return NULL;
    }
    Object_delete((void *) thisObj);
    return Py_BuildValue("");
}

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



