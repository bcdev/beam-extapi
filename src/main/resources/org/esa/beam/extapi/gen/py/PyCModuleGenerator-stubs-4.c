/*
 * Creates a Python sequence (a list) from a C-array of Java objects (type void*).
 */
PyObject* beampy_newPySeqFromCObjectArray(const char* type, const void** elems, int length)
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

/*
 * Creates a C-array of Java objects (type void*) from a Python sequence.
 */
void** beampy_newCObjectArrayFromPySeq(const char* type, PyObject* seq, int* length)
{
    /*
       TODO: IMPLEMENT ME!
       Use char* PyUnicode_AsUTF8AndSize(PyObject *unicode, Py_ssize_t *size)
       PyArg_ParseTuple(item, "(sK)", &itemType, &itemObj);
     */
    return NULL;
}


/*
 * Creates a Python sequence (a list) from a C-array of C-strings (type const char*).
 */
PyObject* beampy_newPySeqFromCStringArray(const char** elems, int length)
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

/*
 * Creates a C-array of C-strings from a Python sequence.
 */
char** beampy_newCStringArrayFromPySeq(PyObject* seq, int* length)
{
    /*
       TODO: IMPLEMENT ME!
       Use char* PyUnicode_AsUTF8AndSize(PyObject *unicode, Py_ssize_t *size)
     */
    return NULL;
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
