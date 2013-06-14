///////////////////////////////////////////////
// Java strings and strings arrays
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
 * Returns a global reference to a String array.
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

    // todo - check if we must DeleteLocalRef(strJObj)
    return (*jenv)->NewGlobalRef(jenv, strJObj);
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
 * Returns a global reference to a String array.
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

    // todo - check if we must DeleteLocalRef(arrayJObj)
    return (*jenv)->NewGlobalRef(jenv, arrayJObj);
}



///////////////////////////////////////////////
// Java objects and object arrays
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
        if (strcmp(typeNameActual, "Object") == 0 || strcmp(typeNameActual, typeName) == 0) {
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

    // todo - check if we must DeleteLocalRef(arrayJObj)
    return (*jenv)->NewGlobalRef(jenv, arrayJObj);
}





/*

// The following code is experimental and unused yet.
//
// It allows us to create a Java (Hash)Map from a Python dictionary
// so that we can pass the resulting map into GPF.create() methods.

#include <jni.h>

static jmethodID hashMapConstr = NULL;
static jmethodID hashMapPutMethod = NULL;
static jmethodID booleanConstr = NULL;
static jmethodID integerConstr = NULL;
static jmethodID doubleConstr = NULL;


jmethodID beampy_getMethodID(jclass cls, const char* name, const char* sig)
{
    JNIEnv* jenv = beam_getJNIEnv();
    jmethodID m = (*jenv)->GetMethodID(jenv, cls, name, sig);
    if (m == NULL){
        fprintf(stderr, "${libName}: Java method not found: %p: %s%s\n", cls, name, sig);
    }
    return m;
}

void beam_initJavaCoreMethods() {
    static init = 0;
    if (init == 0) {
        init = 1;
        booleanConstr = beampy_getMethodID(classBoolean, "<init>", "(Z)V");
        integerConstr = beampy_getMethodID(classInteger, "<init>", "(I)V");
        doubleConstr = beampy_getMethodID(classDouble, "<init>", "(D)V");
        hashMapConstr = beampy_getMethodID(classHashMap, "<init>", "()V");
        hashMapPutMethod = beampy_getMethodID(classHashMap, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    }
}

jobject beampy_newJObjectFromPyObject(PyObject* pyObj)
{
    JNIEnv* jenv = beam_getJNIEnv();
    static int init = 0;
    jobject result = NULL;

    beam_initJavaCoreMethods();

    if (PyBool_Check(pyObj)) {
        int v = (pyObj == Py_True);
        result = (*jenv)->NewObject(jenv, classBoolean, booleanConstr, v);
    } else if (PyLong_Check(pyObj)) {
        long v = PyLong_AsLong(pyObj);
        result = (*jenv)->NewObject(jenv, classInteger, integerConstr, v);
    } else if (PyFloat_Check(pyObj)) {
        double v = PyFloat_AsDouble(pyObj);
        result = (*jenv)->NewObject(jenv, classDouble, doubleConstr, v);
    } else if (PyUnicode_Check(pyObj)) {
        char* utf8 = PyUnicode_AsUTF8(pyObj);
        result = (*jenv)->NewStringUTF(jenv, utf8);
    } else {
        PyErr_SetString(PyExc_ValueError, "Expected a boolean, number or string");
    }

    return result != NULL ? (*jenv)->NewGlobalRef(jenv, result) : NULL;
}

void* Map_newHashMap(PyObject* dict)
{
    JNIEnv* jenv = beam_getJNIEnv();
    PyObject* dictKey;
    PyObject* dictValue;
    Py_ssize_t dictPos = 0;
    jobject map = NULL;
    int apicode = 0;

    if ((apicode = beam_initApi()) != 0) {
        PyErr_SetString(BeamPy_Error, "beam_initApi() failed");
        return NULL;
    }

    beam_initJavaCoreMethods();

    map = (*jenv)->NewObject(jenv, classHashMap, hashMapConstr);
    if (map == NULL) {
        PyErr_SetString(BeamPy_Error, "Map_newHashMap: dictionary expected");
        return NULL;
    }

    while (PyDict_Next(dict, &dictPos, &dictKey, &dictValue)) {
        jobject mapKey = beampy_newJObjectFromPyObject(dictKey);
        jobject mapValue = beampy_newJObjectFromPyObject(dictValue);
        if (mapKey != NULL && mapValue != NULL) {
            (*jenv)->CallObjectMethod(jenv, map, hashMapPutMethod, mapKey, mapValue);
        }
    }

    return map;
}


//
// Factory method for Java HashMap instances.
//
// In Python, call <code>beampy.Map_newHashMap({'a': 0.04, 'b': True, 'C': 545})</code>
// or <code>beampy.Map.newHashMap({'a': 0.04, 'b': True, 'C': 545})</code>.
//
PyObject* BeamPyMap_newHashMap(PyObject* self, PyObject* args)
{
    PyObject* dict;
    void* result;

    if (!PyArg_ParseTuple(args, "O:newHashMap", &dict)) {
        return NULL;
    }

    if (!PyDict_Check(dict)) {
        PyErr_SetString(PyExc_ValueError, "dictionary expected");
        return NULL;
    }

    result = Map_newHashMap(dict);
    if (result == NULL) {
        return NULL;
    }

    return Py_BuildValue("(sK)", "Map", (unsigned PY_LONG_LONG) result);
}


*/
