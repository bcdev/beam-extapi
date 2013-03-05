typedef struct {
    PyObject_HEAD
    void* jobjectId;
} BeamPyJObject;

static int BeamPyJObject_init(BeamPyJObject* self, PyObject* args, PyObject* kwds)
{
    printf("BeamPyJObject_init\n");
    self->jobjectId = PyLong_AsVoidPtr(args);
    return self->jobjectId != NULL ? 0 : 1;
}

static void BeamPyJObject_dealloc(BeamPyJObject* self)
{
    printf("BeamPyJObject_dealloc\n");
    beam_release_jobject(&self->jobjectId);
}

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


static PyObject* BeamPy_JObjectType = (PyObject*) &BeamPy_JObjectTypeV;

/*
 * The BEAM/Python API module definition structure.
 */
static struct PyModuleDef BeamPy_Module =
{
   PyModuleDef_HEAD_INIT,
   "_${libName}",           /* Name of the Python module */
   "Native BEAM/Python API",  /* Module documentation */
   -1,                 /* Size of per-interpreter state of the module, or -1 if the module keeps state in global variables. */
   BeamPy_Methods      /* Structure containing all BEAM/Python API functions */
};

/*
 * Called by the Python interpreter once immediately after the shared lib has been loaded.
 */
PyMODINIT_FUNC PyInit__${libName}()
{
    PyObject* m;

    fprintf(stdout, "${libName}: Enter PyInit__${libName}()\n");
    m = PyModule_Create(&BeamPy_Module);
    if (m == NULL) {
        return NULL;
    }

    BeamPy_Error = PyErr_NewException("${libName}.error", NULL, NULL);
    Py_INCREF(BeamPy_Error);
    PyModule_AddObject(m, "error", BeamPy_Error);

    //Py_INCREF(BeamPy_JObjectType);
    //PyModule_AddObject(m, "JObject", BeamPy_JObjectType);

    // todo - use the new BeamPy_JObjectType object instead of the currently used (sK) tuples.
    // // JObject instances shall be created using the following pattern:
    // PyObject* arg = PyLong_FromVoidPtr(ptr); // ptr is the JNI Java object
    // PyObject* obj = PyObject_Call(BeamPy_JObjectType, arg, NULL);
    // Py_DECREF(arg);

    // todo - in  BeamPyJObject_init use:
    // self->jobject = PyLong_AsVoidPtr(args);

    if (!beam_create_jvm_with_defaults()) {
        PyErr_SetString(BeamPy_Error, "Failed to create Java VM");
        return NULL;
    }

    fprintf(stdout, "${libName}: Exit PyInit__${libName}()\n");

    return m;
}

