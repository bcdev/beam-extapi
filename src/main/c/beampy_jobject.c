#include "beampy_jobject.h"
#include "beampy_jpyutil.h"

PyObject* JObject_new(PyTypeObject *type, PyObject *args, PyObject *kwds);
PyObject* JObject_repr(JObject* self);
PyObject* JObject_str(JObject* self);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JObject

/**
 * Implements the BeamPy_JObjectType class singleton.
 */
PyTypeObject JObject_Type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "beampy.JObject",             /* tp_name */
    sizeof (JObject),             /* tp_basicsize */
    0,                            /* tp_itemsize */
    (destructor)JObject_dealloc,  /* tp_dealloc */
    NULL,                         /* tp_print */
    NULL,                         /* tp_getattr */
    NULL,                         /* tp_setattr */
    NULL,                         /* tp_reserved */
    (reprfunc)JObject_repr,       /* tp_repr */
    NULL,                         /* tp_as_number */
    NULL,                         /* tp_as_sequence */
    NULL,                         /* tp_as_mapping */
    NULL,                         /* tp_hash  */           // todo - nice to have: Object.hashCode()
    NULL,                         /* tp_call */
    (reprfunc)JObject_str,        /* tp_str */
    NULL,                         /* tp_getattro */
    NULL,                         /* tp_setattro */
    NULL,                         /* tp_as_buffer */
    Py_TPFLAGS_DEFAULT|Py_TPFLAGS_BASETYPE,  /* tp_flags */
    "Java Object Wrapper",        /* tp_doc */
    NULL,                         /* tp_traverse */
    NULL,                         /* tp_clear */
    NULL,                         /* tp_richcompare */     // todo - nice to have: Object.compare() / equals()
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
    (initproc)JObject_init,       /* tp_init */
    NULL,                         /* tp_alloc */
    JObject_new,                  /* tp_new */
};

PyObject* JObject_repr(JObject* self)
{
    return PyUnicode_FromFormat("%s(%p)",
                                ((PyObject*)self)->ob_type->tp_name,
                                self->jobjectRef);
}

jclass JObject_GetJObjectClass()
{
    static jclass objectClass = NULL;
    if (objectClass == NULL) {
        objectClass = (*jenv)->FindClass(jenv, "Ljava/lang/Object;");
    }
    return objectClass;
}

PyObject* JObject_str(JObject* self)
{
    static jmethodID toStringMethod = NULL;
    jstring strJObj;
    PyObject* strPyObj;
    jboolean isCopy;
    const char * utfChars;

    if (toStringMethod == NULL) {
        toStringMethod = (*jenv)->GetMethodID(jenv, JObject_GetJObjectClass(), "toString", "()Ljava/lang/String;");
    }

    strJObj = (*jenv)->CallObjectMethod(jenv, self->jobjectRef, toStringMethod);
    utfChars = (*jenv)->GetStringUTFChars(jenv, strJObj, &isCopy);
    strPyObj = PyUnicode_FromFormat("%s", utfChars);
    (*jenv)->ReleaseStringUTFChars(jenv, strJObj, utfChars);
    return strPyObj;
}


PyObject* JObject_New(jobject jobjectRef)
{
    return JObject_FromType(&JObject_Type, jobjectRef);
}

PyObject* JObject_FromType(PyTypeObject* type, jobject jobjectRef)
{
    PyObject* arg;
    PyObject* result;

    arg = PyLong_FromVoidPtr(jobjectRef);
    result = PyObject_CallFunctionObjArgs((PyObject*) type, arg, NULL);
    Py_DECREF(arg);

// printf("JObject_FromType: type=%p, jobjectRef=%p, result=%p\n",type,jobjectRef,result);

    if (result == NULL) {
        char msg[256];
        sprintf(msg, "JObject_FromType: failed to create instance of %s", type->tp_name);
        PyErr_SetString(PyExc_ValueError, msg);
    }
    return result;
}

JObject* JObject_AsJObject(PyObject* anyPyObj)
{
    if (anyPyObj == NULL || !PyObject_TypeCheck(anyPyObj, &JObject_Type)) {
        return NULL;
    }
    return (JObject*) anyPyObj;
}

jobject JObject_AsJObjectRef(PyObject* anyPyObj)
{
    JObject* jobjPyObj = JObject_AsJObject(anyPyObj);
    if (jobjPyObj == NULL) {
        return NULL;
    }
    return jobjPyObj->jobjectRef;
}

jobject JObject_AsJObjectRefT(PyObject* anyPyObj, jclass requestedType)
{
    JObject* jobjPyObj;
//printf("JObject_AsJObjectRefT: M1 anyPyObj=%p\n", anyPyObj);
    jobjPyObj = JObject_AsJObject(anyPyObj);
//printf("JObject_AsJObjectRefT: M2\n");
    if (jobjPyObj == NULL) {
        return NULL;
    }
//printf("JObject_AsJObjectRefT: M3 jobjPyObj->jobjectRef=%p, requestedType=%p\n", jobjPyObj->jobjectRef, requestedType);
    if (!(*jenv)->IsInstanceOf(jenv, jobjPyObj->jobjectRef, requestedType)) {
        return NULL;
    }
//printf("JObject_AsJObjectRefT: M4\n");
    return jobjPyObj->jobjectRef;
}

jobjectArray JObject_AsJObjectArrayRef(PyObject* anyPyObj)
{
    static jclass objectClass = NULL;
    if (!BPy_InitJClass(&objectClass, "Ljava/lang/Object;"))
        return NULL;
    return JObject_AsJObjectArrayRefT(anyPyObj, objectClass);
}

jclass JObject_GetComponentType(jobject arrayJObj)
{
    jclass arrayCompType;
    jobject arrayClassJObj;
    jobject arrayCompTypeJObj;

    static jclass objectClass = NULL;
    static jclass classClass = NULL;
    static jmethodID getClassMethod = NULL;
    static jmethodID getComponentTypeMethod = NULL;

    if (!BPy_InitJClass(&objectClass, "Ljava/lang/Object;"))
        return NULL;
    if (!BPy_InitJClass(&classClass, "Ljava/lang/Class;"))
        return NULL;
    if (!BPy_InitJMethod(&getClassMethod, objectClass, "java.lang.Object", "getClass", "()Ljava/lang/Class;", 0))
        return NULL;
    if (!BPy_InitJMethod(&getComponentTypeMethod, classClass, "java.lang.Class", "getComponentType", "()Ljava/lang/Class;", 0))
        return NULL;

    arrayClassJObj = (*jenv)->CallObjectMethod(jenv, arrayJObj, getClassMethod);
    arrayCompTypeJObj = (*jenv)->CallObjectMethod(jenv, arrayClassJObj, getComponentTypeMethod);
    if (arrayCompTypeJObj != NULL) {
        arrayCompType = (*jenv)->GetObjectClass(jenv, arrayCompTypeJObj);
    } else {
        arrayCompType = NULL;
    }

    return arrayCompType;
}


jobjectArray JObject_AsJObjectArrayRefT(PyObject* anyPyObj, jclass requestedCompType)
{
    jobject arrayJObj;
    jclass arrayCompType;

    arrayJObj = JObject_AsJObjectRef(anyPyObj);
    if (arrayJObj == NULL) {
        return NULL;
    }

    arrayCompType = JObject_GetComponentType(arrayJObj);
    if (arrayCompType == NULL) {
        return NULL;
    }

    if (!(*jenv)->IsAssignableFrom(jenv, arrayCompType, requestedCompType)) {
        return NULL;
    }

    return (jobjectArray) arrayJObj;
}

/**
 * Implements the __init__() method of the JObject_Type class.
 */
int JObject_init(JObject* self, PyObject* args, PyObject* kwds)
{
// printf("JObject_init: type->tp_name=%s, self->jobjectRef=%p\n", ((PyObject*) self)->ob_type->tp_name, self->jobjectRef);
    return 0;
}

/**
 * Implements the __new__() method of the JObject_Type class.
 */
PyObject* JObject_new(PyTypeObject *type, PyObject *args, PyObject *kwds)
{
    static char *kwlist[] = {"jobjectRef", NULL};
    JObject* self;
    PyObject* jobjId = NULL;
    jobject jobjectRef;

//printf("JObject_new: type->tp_name=%s\n", type->tp_name);

    self = (JObject*) type->tp_alloc(type, 0);

    if (!PyArg_ParseTupleAndKeywords(args, kwds, "O", kwlist, &jobjId)) {
        return NULL;
    }

    jobjectRef = (jobject) PyLong_AsVoidPtr(jobjId);
    if (jobjectRef == NULL) {
        PyErr_SetString(PyExc_ValueError, "JObject_new: failed to convert argument to Java object reference");
        return NULL;
    }

    jobjectRef = (*jenv)->NewGlobalRef(jenv, jobjectRef);
    if (jobjectRef == NULL) {
        PyErr_SetString(PyExc_MemoryError, "JObject_new: jenv->NewGlobalRef() failed");
        return NULL;
    }

// printf("JObject_new: jobjectRef=%p\n", jobjectRef);

    self->jobjectRef = jobjectRef;

    return (PyObject*) self;
}

/**
 * Implements the dealloc() method of the JObject_Type class.
 */
void JObject_dealloc(JObject* self)
{
//printf("JObject_dealloc: self->jobjectRef=%p\n", self->jobjectRef);

    if (self->jobjectRef != NULL) {
        (*jenv)->DeleteGlobalRef(jenv, self->jobjectRef);
        self->jobjectRef = NULL;
    }

    Py_TYPE(self)->tp_free((PyObject*) self);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// JObjectArray

/*
 * Implements the length method of the <sequence> interface for CArray_Type
 */
Py_ssize_t JObjectArray_sq_length(JObjectArray* self)
{
    return (Py_ssize_t) (*jenv)->GetArrayLength(jenv, self->jobjectRef);
}

/*
 * Implements the item getter method of the <sequence> interface for CArray_Type
 */
PyObject* JObjectArray_sq_item(JObjectArray* self, Py_ssize_t index)
{
    jobject elemJObj = (*jenv)->GetObjectArrayElement(jenv, self->jobjectRef, (jsize) index);
    if ((*jenv)->ExceptionCheck(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        PyErr_SetString(PyExc_RuntimeError, "JObjectArray_sq_item: jenv->GetObjectArrayElement() failed");
        return NULL;
    }
    return JObject_New(elemJObj);
}

/*
 * Implements the item assignment method of the <sequence> interface for CArray_Type
 */
int JObjectArray_sq_ass_item(JObjectArray* self, Py_ssize_t index, PyObject* item)
{
    jobject elemJObj = NULL;
    if (item != Py_None) {
        jclass compType;
        jboolean ok = 1;

        // Note: for efficiency, we could store a constant 'jclass compType' member in JObjectArray object
        compType = JObject_GetComponentType(self->jobjectRef);
        if (compType == NULL) {
            return -1;
        }

        elemJObj = BPy_ToJObjectT(item, compType, &ok);
        if (!ok) {
            return -2;
        }
    }
    (*jenv)->SetObjectArrayElement(jenv, self->jobjectRef, (jsize) index, elemJObj);
    if ((*jenv)->ExceptionCheck(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        PyErr_SetString(PyExc_RuntimeError, "JObjectArray_sq_ass_item: jenv->SetObjectArrayElement() failed");
        return -3;
    }
    return 0;
}

/*
 * Implements the <sequence> interface for CArray_Type
 */
static PySequenceMethods JObjectArray_as_sequence = {
    (lenfunc) JObjectArray_sq_length,            /* sq_length */
    NULL,   /* sq_concat */
    NULL,   /* sq_repeat */
    (ssizeargfunc) JObjectArray_sq_item,         /* sq_item */
    NULL,   /* was_sq_slice */
    (ssizeobjargproc) JObjectArray_sq_ass_item,  /* sq_ass_item */
    NULL,   /* was_sq_ass_slice */
    NULL,   /* sq_contains */
    NULL,   /* sq_inplace_concat */
    NULL,   /* sq_inplace_repeat */
};

/**
 * Implements the BeamPy_JObjectType class singleton.
 */
PyTypeObject JObjectArray_Type = {
    PyVarObject_HEAD_INIT(NULL, 0)
    "beampy.JObjectArray",        /* tp_name */
    sizeof (JObject),             /* tp_basicsize */
    0,                            /* tp_itemsize */
    (destructor)JObject_dealloc,  /* tp_dealloc */
    NULL,                         /* tp_print */
    NULL,                         /* tp_getattr */
    NULL,                         /* tp_setattr */
    NULL,                         /* tp_reserved */
    NULL,                         /* tp_repr */
    NULL,                         /* tp_as_number */
    &JObjectArray_as_sequence,    /* tp_as_sequence */
    NULL,                         /* tp_as_mapping */
    NULL,                         /* tp_hash  */
    NULL,                         /* tp_call */
    NULL,                         /* tp_str */
    NULL,                         /* tp_getattro */
    NULL,                         /* tp_setattro */
    NULL,                         /* tp_as_buffer */
    Py_TPFLAGS_DEFAULT|Py_TPFLAGS_BASETYPE,           /* tp_flags */
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
    (initproc)JObject_init,       /* tp_init */
    NULL,                         /* tp_alloc */
    JObject_new,                  /* tp_new */
};

PyObject* JObjectArray_New(jobjectArray jobjectArrayRef)
{
    return JObject_FromType(&JObjectArray_Type, jobjectArrayRef);
}


