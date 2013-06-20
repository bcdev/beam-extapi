#include "beampy_jobject.h"


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JObject

/**
 * Implements the BeamPy_JObjectType class singleton.
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

PyObject* JObject_FromJObjectRef(jobject jobjectRef)
{
    return JObject_FromJObjectRefWithType(jobjectRef, &JObject_Type);
}

PyObject* JObject_FromJObjectRefWithType(jobject jobjectRef, PyTypeObject* type)
{
    PyObject* longPyObj = PyLong_FromVoidPtr(jobjectRef);
    PyObject* jobjPyObj = PyObject_CallObject(type, longPyObj);
    Py_DECREF(longPyObj);
    return jobjPyObj;
}

int JObject_Check(PyObject* anyPyObj)
{
    PyTypeObject* type = anyPyObj->ob_type;
    while (type != NULL) {
        if (type == &JObject_Type) {
            return 1;
        }
        type = type->tp_base;
    }
    return 0;
}

JObject* JObject_AsJObject(PyObject* anyPyObj)
{
    if (anyPyObj == NULL || !JObject_Check(anyPyObj)) {
        return NULL;
    }
    return (JObject*) anyPyObj;
}

jobject JObject_GetJObjectRef(PyObject* anyPyObj)
{
    JObject* jobjPyObj = JObject_AsJObject(anyPyObj);
    if (jobjPyObj == NULL) {
        return NULL;
    }
    return jobjPyObj->jobjectRef;
}

jobject JObject_GetJObjectRefInstanceOf(PyObject* anyPyObj, jclass jclassRef)
{
    JObject* jobjPyObj = JObject_AsJObject(anyPyObj);
    if (jobjPyObj == NULL) {
        return NULL;
    }
    if (!(*jenv)->IsInstanceOf(jenv, jobjPyObj->jobjectRef, jclassRef)) {
        return NULL;
    }
    return jobjPyObj->jobjectRef;
}

jobjectArray JObject_GetJObjectArrayRef(PyObject* anyPyObj)
{
    static jclass classObjectArray = NULL;
    JObject* jobjPyObj = JObject_AsJObject(anyPyObj);
    if (jobjPyObj == NULL) {
        return NULL;
    }
    if (classObjectArray == NULL) {
        classObjectArray = (*jenv)->FindClass(jenv, "[Ljava/lang/Object;");
    }
    return (jobjectArray) JObject_GetJObjectRefInstanceOf(jobjPyObj, classObjectArray);
}

/**
 * Implements the __init__() method of the JObject_Type class.
 */
int JObject_init(JObject* self, PyObject* args, PyObject* kwds)
{
    PyObject* jobjId;
    jobject jobjectRef;

    if (!PyArg_ParseTupleAndKeywords(args, kwds, "O", NULL, &jobjId)) {
        return -1;
    }

    jobjectRef = (jobject) PyLong_AsVoidPtr(jobjId);
    if (jobjectRef == NULL) {
        PyErr_SetString(PyExc_ValueError, "failed to convert argument to Java object reference");
        return -2;
    }

    jobjectRef = (*jenv)->NewGlobalRef(jenv, jobjectRef);
    if (jobjectRef == NULL) {
        PyErr_SetString(PyExc_MemoryError, "failed to create global Java object reference");
        return -3;
    }

    printf("JObject_init %p\n", jobjectRef);
    self->jobjectRef = jobjectRef;
    return 0;
}

/**
 * Implements the dealloc() method of the JObject_Type class.
 */
void JObject_dealloc(JObject* self)
{
    printf("JObject_dealloc %p\n", self->jobjectRef);

    if (self->jobjectRef != NULL) {
        (*jenv)->DeleteGlobalRef(jenv, self->jobjectRef);
        self->jobjectRef = NULL;
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JObjectArray

/**
 * Implements the BeamPy_JObjectType class singleton.
 */
PyTypeObject JObjectArray_Type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "JObjectArray",               /* tp_name */
    sizeof (JObject),             /* tp_basicsize */
    0,                            /* tp_itemsize */
    (destructor)JObject_dealloc,  /* tp_dealloc */
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
    "Java Object Array Wrapper",  /* tp_doc */
    NULL,                         /* tp_traverse */
    NULL,                         /* tp_clear */
    NULL,                         /* tp_richcompare */
    0,                            /* tp_weaklistoffset */
    NULL,                         /* tp_iter */
    NULL,                         /* tp_iternext */
    NULL,                         /* tp_methods */
    NULL,                         /* tp_members */
    NULL,                         /* tp_getset */
    NULL,                         /* tp_base */  // WARNING: this must be explicitly set to &JObject_Type !!!
    NULL,                         /* tp_dict */
    NULL,                         /* tp_descr_get */
    NULL,                         /* tp_descr_set */
    0,                            /* tp_dictoffset */
    (initproc) JObject_init,      /* tp_init */
    NULL,                         /* tp_alloc */
    NULL,                         /* tp_new */
};

PyObject* JObjectArray_FromJObjectArrayRef(jobjectArray jobjectArrayRef)
{
    return JObject_FromJObjectRefWithType(jobjectArrayRef, &JObjectArray_Type);
}


