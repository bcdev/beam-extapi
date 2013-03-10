/**
 * Represents an instance of the BeamPy_JObjectType class.
 * Used to represent Java JNI objects.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
typedef struct {
    PyObject_HEAD
    /** The pointer to the Java object obtained from JNI. */
    void* jobjectId;
} BeamPyJObject;

/**
 * Implements the __init__() method of the BeamPy_JObjectType class.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
static int BeamPyJObject_init(BeamPyJObject* self, PyObject* args, PyObject* kwds)
{
    printf("BeamPyJObject_init\n");
    self->jobjectId = PyLong_AsVoidPtr(args);
    return self->jobjectId != NULL ? 0 : 1;
}

/**
 * Implements the dealloc() method of the BeamPy_JObjectType class.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
static void BeamPyJObject_dealloc(BeamPyJObject* self)
{
    printf("BeamPyJObject_dealloc\n");
    beam_release_jobject(self->jobjectId);
    self->jobjectId = NULL;
}

/**
 * Implements the BeamPy_JObjectType class singleton.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
static PyTypeObject BeamPy_JObjectTypeV = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "${libName}.JObject",         /* tp_name */
    sizeof (BeamPyJObject),       /* tp_basicsize */
    0,                            /* tp_itemsize */
    (destructor)BeamPyJObject_dealloc, /* tp_dealloc */
    NULL,                         /* tp_print */
    NULL,                         /* tp_getattr */
    NULL,                         /* tp_setattr */
    NULL,                         /* tp_reserved */
    NULL,                         /* tp_repr */
    NULL,                         /* tp_as_number */
    NULL,                         /* tp_as_sequence */
    NULL,                         /* tp_as_mapping */
    NULL,                         /* tp_hash  */
    NULL,                         /* tp_call */
    NULL,                         /* tp_str */
    NULL,                         /* tp_getattro */
    NULL,                         /* tp_setattro */
    NULL,                         /* tp_as_buffer */
    Py_TPFLAGS_DEFAULT,           /* tp_flags */
    "BEAM Java Object",           /* tp_doc */
    NULL,                         /* tp_traverse */
    NULL,                         /* tp_clear */
    NULL,                         /* tp_richcompare */
    0,                            /* tp_weaklistoffset */
    NULL,                         /* tp_iter */
    NULL,                         /* tp_iternext */
    NULL,                         /* tp_methods */
    NULL,                         /* tp_members */
    NULL,                         /* tp_getset */
    NULL,                         /* tp_base */
    NULL,                         /* tp_dict */
    NULL,                         /* tp_descr_get */
    NULL,                         /* tp_descr_set */
    0,                            /* tp_dictoffset */
    (initproc)BeamPyJObject_init, /* tp_init */
    NULL,                         /* tp_alloc */
    NULL,                         /* tp_new */
};

/**
 * Implements the BeamPy_JObjectType class singleton as PyObject pointer.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
static PyObject* BeamPy_JObjectType = (PyObject*) &BeamPy_JObjectTypeV;

/**
 * The BEAM/Python API module definition structure.
 * The variable 'BeamPy_Methods' is defined in the generated file 'beampy_module.c'.
 */
static struct PyModuleDef BeamPy_Module =
{
   PyModuleDef_HEAD_INIT,
   "_${libName}",           /* Name of the Python module */
   "Native BEAM/Python API",  /* Module documentation */
   -1,                 /* Size of per-interpreter state of the module, or -1 if the module keeps state in global variables. */
   BeamPy_Methods      /* Structure containing all BEAM/Python API functions */
};

/**
 * Called by the Python interpreter once immediately after the shared lib _${libName}.pyk has been loaded.
 */
PyMODINIT_FUNC PyInit__${libName}()
{
    PyObject* beampy_module;

    fprintf(stdout, "${libName}: Enter PyInit__${libName}()\n");

    /////////////////////////////////////////////////////////////////////////
    // Create BeamPy_Module

    beampy_module = PyModule_Create(&BeamPy_Module);
    if (beampy_module == NULL) {
        return NULL;
    }

    /////////////////////////////////////////////////////////////////////////
    // CArray_type / CArray_module

    // In some forum I (nf) found one should use: CArray_type.tp_new = PyType_GenericNew;
    if (PyType_Ready(&CArray_Type) < 0) {
        return NULL;
    }

    Py_INCREF(&CArray_Type);
    PyModule_AddObject(beampy_module, "CArray", (PyObject*) &CArray_Type);
   

    /////////////////////////////////////////////////////////////////////////
    // Register BeamPy_JObjectType ('JObject')
    //
    //if (PyType_Ready(&BeamPy_JObjectType) < 0) {
    //    return NULL;
    //}
    //Py_INCREF(BeamPy_JObjectType);
    //PyModule_AddObject(beampy_module, "JObject", BeamPy_JObjectType);
    //
    // TODO - use the new BeamPy_JObjectType object instead of the currently used (sK) tuples.
    // // JObject instances shall be created using the following pattern:
    // PyObject* arg = PyLong_FromVoidPtr(ptr); // ptr is the JNI Java object
    // PyObject* obj = PyObject_Call(BeamPy_JObjectType, arg, NULL);
    // Py_DECREF(arg);
    //
    // TODO - in  BeamPyJObject_init use the following pattern:
    // self->jobject = PyLong_AsVoidPtr(args);

    /////////////////////////////////////////////////////////////////////////
    // Define exception type BeamPy_Error ('beampy.error')

    BeamPy_Error = PyErr_NewException("${libName}.error", NULL, NULL);
    Py_INCREF(BeamPy_Error);
    PyModule_AddObject(beampy_module, "error", BeamPy_Error);

    /////////////////////////////////////////////////////////////////////////
    // Create JVM

    if (!beam_create_jvm_with_defaults()) {
        PyErr_SetString(BeamPy_Error, "Failed to create Java VM");
        return NULL;
    }

    fprintf(stdout, "${libName}: Exit PyInit__${libName}()\n");

    return beampy_module;
}

/**
 * Factory method for Java string instances.
 *
 * In Python, call <code>beampy_module.String_newString('foobar')</code>
 * or <code>beampy_module.String.newString('foobar')</code>.
 */
PyObject* BeamPyString_newString(PyObject* self, PyObject* args)
{
    const char* chars;
    void* result;

    if (!PyArg_ParseTuple(args, "s:newString", &chars)) {
        return NULL;
    }

    result = String_newString(chars);

    if (result != NULL) {
        return Py_BuildValue("(sK)", "String", (unsigned PY_LONG_LONG) result);
    } else {
        return Py_BuildValue("");
    }
}

