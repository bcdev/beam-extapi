
/**
 * The BEAM/Python API module definition structure.
 * The variable 'BeamPy_Methods' is defined in the generated file 'beampy_module.c'.
 */
static struct PyModuleDef BeamPy_Module =
{
   PyModuleDef_HEAD_INIT,
   "${libName}",           /* Name of the Python module */
   "BEAM Python API",  /* Module documentation */
   -1,                 /* Size of per-interpreter state of the module, or -1 if the module keeps state in global variables. */
   BeamPy_Functions,    /* Structure containing global ${libName}-functions */
   NULL,   // m_reload
   NULL,   // m_traverse
   NULL,   // m_clear
   NULL    // m_free
};

int BPy_RegisterJObjectSubtypes(PyObject* module);


/**
 * Called by the Python interpreter once immediately after the shared lib _${libName}.pyk has been loaded.
 */
PyMODINIT_FUNC PyInit_${libName}(void)
{
    PyObject* module;
    PyObject* version;

    fprintf(stdout, "${libName}: enter PyInit_${libName}()\n");

    /////////////////////////////////////////////////////////////////////////
    // Create BeamPy_Module

    module = PyModule_Create(&BeamPy_Module);
    if (module == NULL) {
        return NULL;
    }

    /////////////////////////////////////////////////////////////////////////
    // Define exception type BeamPy_Error ('beampy.error')

    BeamPy_Error = PyErr_NewException("beampy.BeamError", NULL, NULL);
    Py_INCREF(BeamPy_Error);
    PyModule_AddObject(module, "error", BeamPy_Error);

    /////////////////////////////////////////////////////////////////////////
    // Register API version info

    version = PyUnicode_FromString("${libVersion}");
    Py_INCREF(version);
    PyModule_AddObject(module, "version", version);

    /////////////////////////////////////////////////////////////////////////
    // Register CArray_Type

    if (PyType_Ready(&CArray_Type) < 0) {
        return NULL;
    }
    Py_INCREF(&CArray_Type);
    PyModule_AddObject(module, "CArray", (PyObject*) &CArray_Type);

    /////////////////////////////////////////////////////////////////////////
    // Register JObject_Type ('JObject')
    //
    if (PyType_Ready(&JObject_Type) < 0) {
        return NULL;
    }
    Py_INCREF(&JObject_Type);
    PyModule_AddObject(module, "JObject", (PyObject*) &JObject_Type);

    /////////////////////////////////////////////////////////////////////////
    // Register JObjectArray_Type ('JObjectArray')
    //
    JObjectArray_Type.tp_base = &JObject_Type;
    if (PyType_Ready(&JObjectArray_Type) < 0) {
        return NULL;
    }
    Py_INCREF(&JObjectArray_Type);
    PyModule_AddObject(module, "JObjectArray", (PyObject*) &JObjectArray_Type);

    /////////////////////////////////////////////////////////////////////////
    // Register BEAM JObject Sub-types
    //
    if (!BPy_RegisterJObjectSubtypes(module)) {
        return NULL;
    }

    /////////////////////////////////////////////////////////////////////////
    // Create JVM

    if (!beam_createJvmWithDefaults()) {
        PyErr_SetString(BeamPy_Error, "Failed to create Java VM");
        return NULL;
    }

    /////////////////////////////////////////////////////////////////////////
    // Initialise JPyUtil

    if (!BPy_InitJPyUtil()) {
        return NULL;
    }

    fprintf(stdout, "${libName}: exit PyInit_${libName}()\n");

    return module;
}

