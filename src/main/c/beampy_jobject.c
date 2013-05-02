#include "beampy_jobject.h"

/**
 * Implements the BeamPy_JObjectType class singleton.
 *
 * THIS TYPE IS NOT YET IN USE: we currently use
 * (<type_string>, <pointer>) tuples to represent Java JNI objects.
 */
PyTypeObject JObject_Type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "JObject",                    /* tp_name */
    sizeof (JObject),             /* tp_basicsize */
    0,                            /* tp_itemsize */
    (destructor)JObject_dealloc,  /* tp_dealloc */
    NULL,                         /* tp_print */
    NULL,                         /* tp_getattr */
    NULL,                         /* tp_setattr */
    NULL,                         /* tp_reserved */
    NULL,                         /* tp_repr */            // todo --> Object.toString()
    NULL,                         /* tp_as_number */
    NULL,                         /* tp_as_sequence */
    NULL,                         /* tp_as_mapping */
    NULL,                         /* tp_hash  */           // todo --> Object.hashCode()
    NULL,                         /* tp_call */
    NULL,                         /* tp_str */
    NULL,                         /* tp_getattro */
    NULL,                         /* tp_setattro */
    NULL,                         /* tp_as_buffer */
    Py_TPFLAGS_DEFAULT,           /* tp_flags */
    "Java Object Wrapper",        /* tp_doc */
    NULL,                         /* tp_traverse */
    NULL,                         /* tp_clear */
    NULL,                         /* tp_richcompare */     // todo --> Object.compare() / equals()
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
    (initproc) JObject_init,      /* tp_init */
    NULL,                         /* tp_alloc */
    NULL,                         /* tp_new */
};


/**
 * Implements the __init__() method of the JObject_Type class.
 */
int JObject_init(JObject* self, PyObject* args, PyObject* kwds)
{
    PyObject* jobjId;
    jobject jobj;

    printf("JObject_init\n");

    if (!PyArg_ParseTupleAndKeywords(args, kwds, "O", NULL, &jobjId)) {
        return -1;
    }

    jobj = (jobject) PyLong_AsVoidPtr(jobjId);
    if (jobj != NULL) {
        self->jobj = (*jenv)->NewGlobalRef(jenv, jobj);
        return 0;
    } else {
        self->jobj = NULL;
        return 1;
    }
}

/**
 * Implements the dealloc() method of the JObject_Type class.
 */
void JObject_dealloc(JObject* self)
{
    printf("JObject_dealloc\n");

    if (self->jobj != NULL) {
        (*jenv)->DeleteGlobalRef(jenv, self->jobj);
        self->jobj = NULL;
    }
}