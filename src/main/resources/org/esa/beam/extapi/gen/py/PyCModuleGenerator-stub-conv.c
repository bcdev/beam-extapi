///////////////////////////////////////////////
// Java strings and strings arrays
///////////////////////////////////////////////

PyObject* beampy_newPyStringFromJString(jstring strJObj)
{
    const char* utf8Chars;
    jsize n;
    PyObject* strPyObj;

    n = (*jenv)->GetStringUTFLength(jenv, strJObj);
    utf8Chars = (*jenv)->GetStringUTFChars(jenv, strJObj, 0);
    strPyObj = PyUnicode_DecodeUTF8(utf8Chars, n, NULL);
    (*jenv)->ReleaseStringUTFChars(jenv, strJObj, utf8Chars);

    return strPyObj;
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

jarray beampy_newJStringArrayFromPySeq(PyObject* seqPyObj)
{
    // todo - implement me!
    PyErr_SetString(PyExc_NotImplementedError, "not implemented: beampy_newJStringArrayFromPySeq()");
    return NULL;
}



///////////////////////////////////////////////
// Java objects and object arrays
///////////////////////////////////////////////

PyObject* beampy_newPyObjectFromJObject(jobject obj, const char* typeName)
{
    if (obj != NULL) {
        jobject ref = (*jenv)->NewGlobalRef(jenv, obj);
        return Py_BuildValue("(sK)", typeName, (unsigned PY_LONG_LONG) ref);
    } else {
        return Py_BuildValue("");
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
    // todo - implement me!
    PyErr_SetString(PyExc_NotImplementedError, "not implemented: beampy_newPySeqFromJObjectArray()");
    return NULL;
}





/*

PyObject* beampy_newPySeqFromCObjectArray______________(const char* type, const void** elems, int length)
{
    PyObject* list;
    PyObject* item;
    int i;
    list = PyList_New(length);
    if (list == NULL) {
        return NULL;
    }
    for (i = 0; i < length; i++) {
        item = Py_BuildValue("(sK)", type, (unsigned PY_LONG_LONG) elems[i]);
        if (item == NULL) {
            Py_DECREF(list);
            return NULL;
        }
        if (PyList_SetItem(list, i, item) != 0) {
            Py_DECREF(item);
            Py_DECREF(list);
            return NULL;
        }
    }
    return list;
}

PyObject* beampy_newPySeqFromCStringArray______________(const char** elems, int length)
{
    PyObject* list;
    PyObject* item;
    int i;
    list = PyList_New(length);
    if (list == NULL) {
        return NULL;
    }
    for (i = 0; i < length; i++) {
        item = PyUnicode_FromString(elems[i]);
        if (item == NULL) {
            Py_DECREF(list);
            return NULL;
        }
        if (PyList_SetItem(list, i, item) != 0) {
            Py_DECREF(item);
            Py_DECREF(list);
            return NULL;
        }
    }
    return list;
}


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
