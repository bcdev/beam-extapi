///////////////////////////////////////////////
// Java String and String array
///////////////////////////////////////////////

PyObject* beampy_newPyStringFromJString(jstring strJObj)
{
    const char* utf8Chars;
    PyObject* strPyObj;

    utf8Chars = (*jenv)->GetStringUTFChars(jenv, strJObj, 0);
    strPyObj = PyUnicode_FromString(utf8Chars);
    (*jenv)->ReleaseStringUTFChars(jenv, strJObj, utf8Chars);

    return strPyObj;
}

/**
 * Returns a local reference to a String array.
 */
jstring beampy_newJStringFromPyObject(PyObject* anyPyObj)
{
    char* utf8Chars;
    PyObject* strPyObj;
    jstring strJObj;

    strPyObj = PyObject_Str(anyPyObj);
    if (strPyObj == NULL) {
        return NULL;
    }

    utf8Chars = PyUnicode_AsUTF8(strPyObj);
    strJObj = (*jenv)->NewStringUTF(jenv, utf8Chars);
    Py_DECREF(strPyObj);

    if (strJObj == NULL) {
        return NULL;
    }

    return strJObj;
}

PyObject* beampy_newPySeqFromJStringArray(jarray arrayJObj)
{
    PyObject* listPyObj;
    jsize i, n;

    n = (*jenv)->GetArrayLength(jenv, arrayJObj);
    listPyObj = PyList_New(n);
    if (listPyObj == NULL) {
        return NULL;
    }

    for (i = 0; i < n; i++) {
        jstring itemJObj = (jstring) (*jenv)->GetObjectArrayElement(jenv, arrayJObj, i);
        PyObject* itemPyObj = beampy_newPyStringFromJString(itemJObj);
        (*jenv)->DeleteLocalRef(jenv, itemJObj);
        if (itemPyObj == NULL) {
            Py_DECREF(listPyObj);
            return NULL;
        }
        if (PyList_SetItem(listPyObj, i, itemPyObj) != 0) {
            Py_DECREF(itemPyObj);
            Py_DECREF(listPyObj);
            return NULL;
        }
    }

    return listPyObj;
}

/**
 * Returns a local reference to a String array.
 */
jarray beampy_newJStringArrayFromPySeq(PyObject* seqPyObj)
{
    jarray arrayJObj;
    Py_ssize_t size;
    Py_ssize_t i;

    size = PySequence_Size(seqPyObj);
    if (size < 0 || size >= (1 << 31)) {
        char msg[256];
        sprintf(msg, "invalid sequence size: %d", size);
        PyErr_SetString(PyExc_ValueError, msg);
        return NULL;
    }

    arrayJObj = (*jenv)->NewObjectArray(jenv, (jsize) size, classString, NULL);

    for (i = 0; i < size; i++) {
        PyObject* itemPyObj = PySequence_GetItem(seqPyObj, i);
        if (itemPyObj == NULL) {
            (*jenv)->DeleteLocalRef(jenv, arrayJObj);
            return NULL;
        }
        if (itemPyObj != Py_None) {
            (*jenv)->SetObjectArrayElement(jenv, arrayJObj, (jint) i, beampy_newJStringFromPyObject(itemPyObj));
        } else {
            (*jenv)->SetObjectArrayElement(jenv, arrayJObj, (jint) i, NULL);
        }
    }

    return arrayJObj;
}



///////////////////////////////////////////////
// Java Object and Object array
///////////////////////////////////////////////

/**
 * Stores a global reference to a Java object
 */
PyObject* beampy_newPyObjectFromJObject(jobject anyJObj, const char* typeName)
{
    if (anyJObj != NULL) {
        jobject refJObj = (*jenv)->NewGlobalRef(jenv, anyJObj);
        return Py_BuildValue("(sK)", typeName, (unsigned PY_LONG_LONG) refJObj);
    } else {
        return Py_BuildValue("");
    }
}

jobject beampy_newJObjectFromPyObject(PyObject* anyPyObj, const char* typeName)
{
    if (PyTuple_Check(anyPyObj) && PyTuple_Size(anyPyObj) == 2) {
        PyObject* typePyObj = PyTuple_GetItem(anyPyObj, 0);
        PyObject* jobjPyObj = PyTuple_GetItem(anyPyObj, 1);
        const char* typeNameActual = PyUnicode_AsUTF8(typePyObj);
        // todo - using a more generic approach we would here use the Java type hierarchy to perform type-checking
        if (strcmp(typeName, "Object") == 0 || strcmp(typeName, typeNameActual) == 0) {
            return (jobject) PyLong_AsVoidPtr(jobjPyObj);
        } else {
            PyErr_SetString(PyExc_ValueError, "illegal object type");
            return NULL;
        }
    } else {
        // todo - using a more generic approach we would here try to convert the python object to a Java one
        PyErr_SetString(PyExc_ValueError, "tuple of length 2 expected");
        return NULL;
    }
}

PyObject* beampy_newPySeqFromJObjectArray(jarray arrJObj, const char* typeName)
{
    PyObject* listPyObj;
    jsize i, n;

    n = (*jenv)->GetArrayLength(jenv, arrJObj);
    listPyObj = PyList_New(n);
    if (listPyObj == NULL) {
        return NULL;
    }

    for (i = 0; i < n; i++) {
        jobject itemJObj = (*jenv)->GetObjectArrayElement(jenv, arrJObj, i);
        PyObject* itemPyObj = beampy_newPyObjectFromJObject(itemJObj, typeName);
        (*jenv)->DeleteLocalRef(jenv, itemJObj);
        if (itemPyObj == NULL) {
            Py_DECREF(listPyObj);
            return NULL;
        }
        if (PyList_SetItem(listPyObj, i, itemPyObj) != 0) {
            Py_DECREF(itemPyObj);
            Py_DECREF(listPyObj);
            return NULL;
        }
    }

    return listPyObj;
}

jarray beampy_newJObjectArrayFromPySeq(PyObject* seqPyObj, const char* typeName)
{
    jarray arrayJObj;
    Py_ssize_t size;
    Py_ssize_t i;

    size = PySequence_Size(seqPyObj);
    if (size < 0 || size >= (1 << 31)) {
        char msg[256];
        sprintf(msg, "invalid sequence size: %d", size);
        PyErr_SetString(PyExc_ValueError, msg);
        return NULL;
    }

    arrayJObj = (*jenv)->NewObjectArray(jenv, (jsize) size, classObject, NULL);

    for (i = 0; i < size; i++) {
        PyObject* itemPyObj = PySequence_GetItem(seqPyObj, i);
        if (itemPyObj == NULL) {
            (*jenv)->DeleteLocalRef(jenv, arrayJObj);
            return NULL;
        }
        if (itemPyObj != Py_None) {
            (*jenv)->SetObjectArrayElement(jenv, arrayJObj, (jint) i, beampy_newJObjectFromPyObject(itemPyObj, typeName));
        } else {
            (*jenv)->SetObjectArrayElement(jenv, arrayJObj, (jint) i, NULL);
        }
    }

    return arrayJObj;
}




///////////////////////////////////////////////
// Java Map
///////////////////////////////////////////////


static jmethodID _booleanConstr = NULL;
static jmethodID _integerConstr = NULL;
static jmethodID _doubleConstr = NULL;
static jmethodID _hashMapConstr = NULL;
static jmethodID _hashMapPutMethod = NULL;

boolean beampy_initJavaCoreVars() {
    static init = 0;
    if (init == 0) {
        init = 1;

        if (!beampy_initJMethod(&_booleanConstr, classBoolean, "java.lang.Boolean", "<init>", "(Z)V", 0)) {
            return 0;
        }
        if (!beampy_initJMethod(&_integerConstr, classInteger, "java.lang.Integer", "<init>", "(I)V", 0)) {
            return 0;
        }
        if (!beampy_initJMethod(&_doubleConstr, classDouble, "java.lang.Double", "<init>", "(D)V", 0)) {
            return 0;
        }
        if (!beampy_initJMethod(&_hashMapConstr, classHashMap, "java.util.HashMap", "<init>", "()V", 0)) {
            return 0;
        }
        if (!beampy_initJMethod(&_hashMapPutMethod, classHashMap, "java.util.HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", 0)) {
            return 0;
        }
    }
    return 1;
}

jobject beampy_newJMapFromPyObject(PyObject* anyPyObj);

jobject beampy_newGenericJObjectFromPyObject(PyObject* anyPyObj)
{
    jobject anyJObj = NULL;

    if (!beampy_initJavaCoreVars()) {
        return NULL;
    }

    if (PyBool_Check(anyPyObj)) {
        boolean v = (anyPyObj == Py_True);
        anyJObj = (*jenv)->NewObject(jenv, classBoolean, _booleanConstr, v);
    } else if (PyLong_Check(anyPyObj)) {
        long v = PyLong_AsLong(anyPyObj);
        anyJObj = (*jenv)->NewObject(jenv, classInteger, _integerConstr, v);
    } else if (PyFloat_Check(anyPyObj)) {
        double v = PyFloat_AsDouble(anyPyObj);
        anyJObj = (*jenv)->NewObject(jenv, classDouble, _doubleConstr, v);
    } else if (PyUnicode_Check(anyPyObj)) {
        char* v = PyUnicode_AsUTF8(anyPyObj);
        anyJObj = (*jenv)->NewStringUTF(jenv, v);
    } else if (PyDict_Check(anyPyObj)) {
        anyJObj = beampy_newJMapFromPyObject(anyPyObj);
    } else {
        PyErr_SetString(PyExc_ValueError, "Expected a boolean, number, string or dictionary");
    }

    return anyJObj;
}


jobject beampy_newJMapFromPyObject(PyObject* anyPyObj)
{
    PyObject* dictKeyPyObj;
    PyObject* dictValuePyObj;
    Py_ssize_t dictPos = 0;
    jobject mapJObj = NULL;

    if (!beampy_initJavaCoreVars()) {
        return NULL;
    }

    if (!(anyPyObj == Py_None || PyDict_Check(anyPyObj))) {
        PyErr_SetString(PyExc_TypeError, "dictionary or None expected");
        return NULL;
    }

    mapJObj = (*jenv)->NewObject(jenv, classHashMap, _hashMapConstr);
    if (mapJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "failed to create Java Map");
        return NULL;
    }

    if (anyPyObj != Py_None) {
        while (PyDict_Next(anyPyObj, &dictPos, &dictKeyPyObj, &dictValuePyObj)) {
            jobject mapKeyJObj   = beampy_newGenericJObjectFromPyObject(dictKeyPyObj);
            jobject mapValueJObj = beampy_newGenericJObjectFromPyObject(dictValuePyObj);
            if (mapKeyJObj != NULL && mapValueJObj != NULL) {
                (*jenv)->CallObjectMethod(jenv, mapJObj, _hashMapPutMethod, mapKeyJObj, mapValueJObj);
            }
        }
    }

    return mapJObj;
}

