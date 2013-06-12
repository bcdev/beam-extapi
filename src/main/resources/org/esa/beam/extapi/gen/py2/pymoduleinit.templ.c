// Note: this is unused, experimental code

static PyMethodDef BeamPy_Methods[] = {
    {"newString", BeamPy_newString, METH_VARARGS, "Converts a Python unicode string into a Java java.lang.String object"},
    {"newMap", BeamPy_newMap, METH_VARARGS, "Converts a Python dictionary into a Java java.utils.Map object"},
    {NULL, NULL, 0, NULL}  /* Sentinel */
};

/**
 * The BEAM/Python API module definition structure.
 */
static struct PyModuleDef BeamPy_Module =
{
   PyModuleDef_HEAD_INIT,
   "_beampy",           /* Name of the Python module */
   "Native BEAM/Python API",  /* Module documentation */
   -1,                 /* Size of per-interpreter state of the module, or -1 if the module keeps state in global variables. */
   BeamPy_Methods      /* Structure containing all BEAM/Python API functions */
};

/**
 * Called by the Python interpreter once immediately after the shared lib beampy (.pyk on Windows, .so Unix) has been loaded.
 */
PyMODINIT_FUNC PyInit_beampy()
{
    PyObject* module;

    fprintf(stdout, "beampy: Enter PyInit_beampy()\n");

    /////////////////////////////////////////////////////////////////////////
    // Create BeamPy_Module

    module = PyModule_Create(&BeamPy_Module);
    if (module == NULL) {
        return NULL;
    }

    /////////////////////////////////////////////////////////////////////////
    // Register CArray_Type / CArray_module

    // In some forum I (nf) found one should use: CArray_type.tp_new = PyType_GenericNew;
    if (PyType_Ready(&CArray_Type) < 0) {
        return NULL;
    }
    Py_INCREF(&CArray_Type);
    PyModule_AddObject(module, "CArray", (PyObject*) &CArray_Type);
   

    /////////////////////////////////////////////////////////////////////////
    // Register JObject_Type ('JObject'), the base class for all BEAM classes
    //
    if (PyType_Ready(&JObject_Type) < 0) {
        return NULL;
    }
    Py_INCREF(&JObject_Type);
    PyModule_AddObject(module, "JObject", (PyObject*) &JObject_Type);

    /////////////////////////////////////////////////////////////////////////
    // Define exception type BeamPy_Error ('beampy.error')

    BeamPy_Error = PyErr_NewException("beampy.error", NULL, NULL);
    Py_INCREF(BeamPy_Error);
    PyModule_AddObject(module, "error", BeamPy_Error);

    /////////////////////////////////////////////////////////////////////////
    // Create JVM

    if (!beam_createJvmWithDefaults()) {
        PyErr_SetString(BeamPy_Error, "Failed to create Java VM");
        return NULL;
    }

    fprintf(stdout, "beampy: Exit PyInit__beampy()\n");

    return beampy_module;
}

/**
 * Factory method for Java string instances.
 *
 * In Python, call <code>beampy.String_newString('foobar')</code>
 * or <code>beampy.String.newString('foobar')</code>.
 */
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

static jmethodID hashMapConstr = NULL;
static jmethodID hashMapPutMethod = NULL;
static jmethodID booleanConstr = NULL;
static jmethodID integerConstr = NULL;
static jmethodID doubleConstr = NULL;


jmethodID beam_GetMethodID(jclass cls, const char* name, const char* sig)
{
    JNIEnv* jenv = beam_getJNIEnv();
    jmethodID m = (*jenv)->GetMethodID(jenv, cls, name, sig);
    if (m == NULL){
        fprintf(stderr, "error: Java method not found: %p: %s%s\n", cls, name, sig);
    }
    return m;
}


// TODO - this is experimental code, move to beam_init_api() once we know it is ok (nf, 29.04.2013)
void beam_init_java_core() {
    static init = 0;
    if (init == 0) {
        init = 1;
        booleanConstr = beam_GetMethodID(classBoolean, "<init>", "(Z)V");
        integerConstr = beam_GetMethodID(classInteger, "<init>", "(I)V");
        doubleConstr = beam_GetMethodID(classDouble, "<init>", "(D)V");
        hashMapConstr = beam_GetMethodID(classHashMap, "<init>", "()V");
        hashMapPutMethod = beam_GetMethodID(classHashMap, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    }
}

jobject py2j(PyObject* pyObj)
{
    JNIEnv* jenv = beam_getJNIEnv();
    static int init = 0;
    jobject result = NULL;

    beam_init_java_core();

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
        PyErr_SetString(BeamPy_Error, "beam_initApi failed");
        return NULL;
    }

    beam_init_java_core();

    map = (*jenv)->NewObject(jenv, classHashMap, hashMapConstr);
    if (map == NULL) {
        PyErr_SetString(BeamPy_Error, "Map_newHashMap: Dictionary expected");
        return NULL;
    }

    while (PyDict_Next(dict, &dictPos, &dictKey, &dictValue)) {
        jobject mapKey = py2j(dictKey);
        jobject mapValue = py2j(dictValue);
        if (mapKey != NULL && mapValue != NULL) {
            (*jenv)->CallObjectMethod(jenv, map, hashMapPutMethod, mapKey, mapValue);
        }
    }

    return map;
}


/**
 * Factory method for Java HashMap instances.
 *
 * In Python, call <code>beampy.Map_newHashMap({'a': 0.04, 'b': True, 'C': 545})</code>
 * or <code>beampy.Map.newHashMap({'a': 0.04, 'b': True, 'C': 545})</code>.
 */
PyObject* BeamPyMap_newHashMap(PyObject* self, PyObject* args)
{
    PyObject* dict;
    void* result;

    if (!PyArg_ParseTuple(args, "O:newHashMap", &dict)) {
        return NULL;
    }

    if (!PyDict_Check(dict)) {
        PyErr_SetString(PyExc_ValueError, "Dictionary expected");
        return NULL;
    }

    result = Map_newHashMap(dict);
    if (result == NULL) {
        return NULL;
    }

    return Py_BuildValue("(sK)", "Map", (unsigned PY_LONG_LONG) result);
}

