
/*
 * The BEAM/Python API module definition structure.
 */
static struct PyModuleDef BeamPy_Module =
{
   PyModuleDef_HEAD_INIT,
   "${libName}",           /* Name of the Python module */
   "BEAM/Python API",  /* Module documentation */
   -1,                 /* Size of per-interpreter state of the module, or -1 if the module keeps state in global variables. */
   BeamPy_Methods      /* Structure containing all BEAM/Python API functions */
};

/*
 * Called by the Python interpreter once immediately after the shared lib has been loaded.
 */
PyMODINIT_FUNC PyInit_${libName}()
{
    PyObject* m;

    BEAM_TRACE("${libName}: PyInit_${libName}() called\n");

    m = PyModule_Create(&BeamPy_Module);
    if (m == NULL) {
        return NULL;
    }

    BeamPy_Error = PyErr_NewException("${libName}.error", NULL, NULL);
    Py_INCREF(BeamPy_Error);
    PyModule_AddObject(m, "error", BeamPy_Error);

    if (!beam_create_jvm_with_defaults()) {
        PyErr_SetString(BeamPy_Error, "Failed to create Java VM");
        return NULL;
    }

    return m;
}