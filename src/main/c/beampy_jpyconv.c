#include "beampy_jpyconv.h"


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java Core Variables

static jclass BPy_ObjectClass = NULL;
static jclass BPy_ObjectArrayClass = NULL;
static jclass BPy_StringArrayClass = NULL;

static jclass BPy_BooleanClass = NULL;
static jmethodID BPy_BooleanConstr = NULL;

static jclass BPy_CharacterClass = NULL;
static jmethodID BPy_CharacterConstr = NULL;

static jclass BPy_CharClass = NULL;
static jmethodID BPy_CharConstr = NULL;

static jclass BPy_ByteClass = NULL;
static jmethodID BPy_ByteConstr = NULL;

static jclass BPy_ShortClass = NULL;
static jmethodID BPy_ShortConstr = NULL;

static jclass BPy_IntegerClass = NULL;
static jmethodID BPy_IntegerConstr = NULL;

static jclass BPy_LongClass = NULL;
static jmethodID BPy_LongConstr = NULL;

static jclass BPy_FloatClass = NULL;
static jmethodID BPy_FloatConstr = NULL;

static jclass BPy_DoubleClass = NULL;
static jmethodID BPy_DoubleConstr = NULL;

static jclass BPy_StringClass = NULL;

static jclass  BPy_MapClass = NULL;
static jclass  BPy_ListClass = NULL;

static jclass  BPy_HashMapClass = NULL;
static jmethodID BPy_HashMapConstr = NULL;
static jmethodID BPy_HashMapPut = NULL;

static jclass  BPy_ArrayListClass = NULL;
static jmethodID BPy_ArrayListConstr = NULL;
static jmethodID BPy_ArrayListSet = NULL;
static jmethodID BPy_ArrayListGet = NULL;
static jmethodID BPy_ArrayListSize = NULL;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Local function declarations

jboolean BPy_InitConv();

jarray BPy_NewJBooleanArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJCharArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJByteArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJShortArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJIntArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJLongArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJFloatArrayFromBuffer(PyObject* arg, jboolean* ok);
jarray BPy_NewJDoubleArrayFromBuffer(PyObject* arg, jboolean* ok);

jarray BPy_NewJBooleanArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJCharArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJByteArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJShortArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJIntArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJLongArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJFloatArrayFromSeq(PyObject* arg, jboolean* ok);
jarray BPy_NewJDoubleArrayFromSeq(PyObject* arg, jboolean* ok);

jobject BPy_NewJNumberFromInt(PyObject* arg, jboolean* ok);
jobject BPy_NewJNumberFromFloat(PyObject* arg, jboolean* ok);
jstring BPy_NewJStringFromStr(PyObject* arg, jboolean* ok);
jobject BPy_NewJMapFromDict(PyObject* arg, jboolean* ok);
jobject BPy_NewJListFromSeq(PyObject* arg, jboolean* ok);
jobjectArray BPy_NewJObjectArrayFromSeq(PyObject* arg, jboolean* ok);
jobjectArray BPy_NewJStringArrayFromSeq(PyObject* arg, jboolean* ok);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Python To Java Conversion Functions

jobject BPy_ConvSuccess(jobject arg, jboolean* ok)
{
    *ok = 1;
    return arg;
}

jobject BPy_ConvFailure(const char* msg, jboolean* ok)
{
    if (msg != NULL) {
        PyErr_SetString(PyExc_ValueError, msg);
    }
    *ok = 0;
    return NULL;
}

jarray BPy_NewJBooleanArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJCharArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJByteArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJShortArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJIntArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJLongArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJFloatArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJDoubleArrayFromBuffer(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}


jarray BPy_NewJBooleanArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJCharArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJByteArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJShortArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJIntArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJLongArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJFloatArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jarray BPy_NewJDoubleArrayFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement me!");
    return NULL;
}

jobject BPy_NewJBooleanFromBool(PyObject* arg, jboolean* ok)
{
    jobject argJObj;
    if (!BPy_InitConv()) {
        return BPy_ConvFailure(NULL, ok);
    }
    argJObj = (*jenv)->NewObject(jenv, BPy_BooleanClass, BPy_BooleanConstr, (jboolean) PyObject_IsTrue(arg));;
    if (argJObj == NULL) {
        return BPy_ConvFailure("failed to instantiate Java object", ok);
    }
    return BPy_ConvSuccess(argJObj, ok);
}

jobject BPy_NewJNumberFromInt(PyObject* arg, jboolean* ok)
{
    jobject argJObj = NULL;
    PY_LONG_LONG v = PyLong_AsLongLong(arg);
    if (!BPy_InitConv()) {
        return BPy_ConvFailure(NULL, ok);
    }
    if (v == (jbyte) v) {
        argJObj = (*jenv)->NewObject(jenv, BPy_ByteClass, BPy_ByteConstr, (jbyte) v);
    } else if (v == (jshort) v) {
        argJObj = (*jenv)->NewObject(jenv, BPy_ShortClass, BPy_ShortConstr, (jshort) v);
    } else if (v == (jint) v) {
        argJObj = (*jenv)->NewObject(jenv, BPy_IntegerClass, BPy_IntegerConstr, (jint) v);
    } else if (v == (jlong) v) {
        argJObj = (*jenv)->NewObject(jenv, BPy_LongClass, BPy_LongConstr, (jlong) v);
    } else {
        return BPy_ConvFailure("missing appropriate Java representation of argument", ok);
    }
    if (argJObj == NULL) {
        return BPy_ConvFailure("failed to instantiate Java object", ok);
    }
    return BPy_ConvSuccess(argJObj, ok);
}

jobject BPy_NewJNumberFromFloat(PyObject* arg, jboolean* ok)
{
     jobject argJObj = NULL;
     double v = PyLong_AsDouble(arg);
     if (!BPy_InitConv()) {
         return BPy_ConvFailure(NULL, ok);
     }
     if (v == (jfloat) v) {
         argJObj = (*jenv)->NewObject(jenv, BPy_FloatClass, BPy_FloatConstr, (jfloat) v);
     } else if (v == (jdouble) v) {
         argJObj = (*jenv)->NewObject(jenv, BPy_DoubleClass, BPy_DoubleConstr, (jdouble) v);
     } else {
         return BPy_ConvFailure("missing appropriate Java representation of argument", ok);
     }
     if (argJObj == NULL) {
         return BPy_ConvFailure("failed to instantiate Java object", ok);
     }
     return BPy_ConvSuccess(argJObj, ok);
}

jstring BPy_NewJStringFromStr(PyObject* arg, jboolean* ok)
{
    char* utf8Chars;
    PyObject* strPyObj;
    jstring strJObj;

    strPyObj = PyObject_Str(arg);
    if (strPyObj == NULL) {
        return BPy_ConvFailure(NULL, ok);
    }

    utf8Chars = PyUnicode_AsUTF8(strPyObj);
    strJObj = (*jenv)->NewStringUTF(jenv, utf8Chars);
    Py_DECREF(strPyObj);

    if (strJObj == NULL) {
        return BPy_ConvFailure("failed to instantiate Java String", ok);
    }

    return BPy_ConvSuccess(strJObj, ok);
}


jobject BPy_NewJMapFromDict(PyObject* arg, jboolean* ok)
{
    PyObject* dictKeyPyObj;
    PyObject* dictValuePyObj;
    Py_ssize_t dictPos = 0;
    jobject mapJObj = NULL;

    if (!BPy_InitConv()) {
        return BPy_ConvFailure(NULL, ok);
    }

    mapJObj = (*jenv)->NewObject(jenv, BPy_HashMapClass, BPy_HashMapConstr);
    if (mapJObj == NULL) {
        return BPy_ConvFailure("failed to instantiate Java HashMap", ok);
    }

    while (PyDict_Next(arg, &dictPos, &dictKeyPyObj, &dictValuePyObj)) {
        jobject mapKeyJObj   = BPy_ToJObject(dictKeyPyObj, ok);
        jobject mapValueJObj = BPy_ToJObject(dictValuePyObj, ok);
        if (mapKeyJObj != NULL && mapValueJObj != NULL) {
            (*jenv)->CallObjectMethod(jenv, mapJObj, BPy_HashMapPut, mapKeyJObj, mapValueJObj);
        }
    }

    return BPy_ConvSuccess(mapJObj, ok);
}

jobject BPy_NewJListFromSeq(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewJListFromSeq");
    return NULL;
}

typedef (*BPy_ToJObjectConverter)(PyObject*, jboolean*);

jobjectArray BPy_NewGenericJObjectArrayFromSeq(PyObject* arg, jclass componentType, BPy_ToJObjectConverter conv, jboolean* ok)
{
    jarray arrayJObj;
    Py_ssize_t size;
    Py_ssize_t i;

    size = PySequence_Size(arg);
    if (size < 0 || size >= (1 << 31)) {
        char msg[256];
        sprintf(msg, "invalid sequence size: %d", size);
        return BPy_ConvFailure(msg, ok);
    }

    arrayJObj = (*jenv)->NewObjectArray(jenv, (jsize) size, componentType, NULL);
    if (arrayJObj == NULL) {
        return BPy_ConvFailure("failed to instantiate Java Object[]", ok);
    }

    for (i = 0; i < size; i++) {
        PyObject* itemPyObj = PySequence_GetItem(arg, i);
        if (itemPyObj == NULL) {
            (*jenv)->DeleteLocalRef(jenv, arrayJObj);
            return BPy_ConvFailure("failed to get sequence item", ok);
        }
        (*jenv)->SetObjectArrayElement(jenv, arrayJObj, (jint) i, conv(itemPyObj, ok));
    }

    return BPy_ConvSuccess(arrayJObj, ok);
}

jobjectArray BPy_NewJStringArrayFromSeq(PyObject* arg, jboolean* ok)
{
    return BPy_NewGenericJObjectArrayFromSeq(arg, BPy_ObjectClass, BPy_ToJObject, ok);
}

jobjectArray BPy_NewJObjectArrayFromSeq(PyObject* arg, jboolean* ok)
{
    return BPy_NewGenericJObjectArrayFromSeq(arg, BPy_StringClass, BPy_ToJString, ok);
}



jobject BPy_ToJObject(PyObject* arg, jboolean* ok)
{
    jobject argJObj;
    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    argJObj = JObject_GetJObjectRef(arg);
    if (argJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, argJObj), ok);
    }

    if (PyBool_Check(arg)) {
        return BPy_NewJBooleanFromBool(arg, ok);
    } else if (PyLong_Check(arg)) {
        return BPy_NewJNumberFromInt(arg, ok);
    } else if (PyFloat_Check(arg)) {
        return BPy_NewJNumberFromFloat(arg, ok);
    } else if (PyUnicode_Check(arg)) {
        return BPy_NewJStringFromStr(arg, ok);
    } else if (PyDict_Check(arg)) {
        return BPy_NewJMapFromDict(arg, ok);
    } else if (PySequence_Check(arg)) {
        return BPy_NewJObjectArrayFromSeq(arg, ok);
    }

    return BPy_ConvFailure("missing appropriate Java representation of argument", ok);
}

jobject BPy_ToJObjectT(PyObject* arg, jclass type, jboolean* ok)
{
    jobject argJObj;
    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    argJObj = JObject_GetJObjectRefInstanceOf(arg, type);
    if (argJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, argJObj), ok);
    }

    return BPy_ConvFailure("missing appropriate Java representation of argument", ok);
}

jstring BPy_ToJString(PyObject* arg, jboolean* ok)
{
    jobject strJObj;
    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    strJObj = JObject_GetJObjectRefInstanceOf(arg, BPy_StringClass);
    if (strJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, strJObj), ok);
    }

    if (PyUnicode_Check(arg)) {
        return BPy_NewJStringFromStr(arg, ok);
    }

    return BPy_ConvFailure("argument must be a 'str'", ok);
}

jobject BPy_ToJMap(PyObject* arg, jboolean* ok)
{
    jobject mapJObj;
    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    mapJObj = JObject_GetJObjectRefInstanceOf(arg, BPy_MapClass);
    if (mapJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, mapJObj), ok);
    }

    if (PyDict_Check(arg)) {
        return BPy_NewJMapFromDict(arg, ok);
    }

    return BPy_ConvFailure("argument must be a 'dict'", ok);
}

jobject BPy_ToJList(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJList");
    return NULL;
}

jobjectArray BPy_ToJObjectArray(PyObject* arg, jboolean* ok)
{
    jobject arrayJObj;
    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    arrayJObj = JObject_GetJObjectRefInstanceOf(arg, BPy_ObjectArrayClass);
    if (arrayJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, arrayJObj), ok);
    }

    if (PySequence_Check(arg)) {
        return BPy_NewJObjectArrayFromSeq(arg, ok);
    }

    return BPy_ConvFailure("argument must be a sequence", ok);
}

jobjectArray BPy_ToJStringArray(PyObject* arg, jboolean* ok)
{
    jobject arrayJObj;
    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    arrayJObj = JObject_GetJObjectRefInstanceOf(arg, BPy_StringArrayClass);
    if (arrayJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, arrayJObj), ok);
    }

    if (PySequence_Check(arg)) {
        return BPy_NewJStringArrayFromSeq(arg, ok);
    }

    return BPy_ConvFailure("argument must be a sequence", ok);
}

jarray BPy_ToJBooleanArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJBooleanArray");
    return NULL;
}

jarray BPy_ToJCharArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJCharArray");
    return NULL;
}

jarray BPy_ToJByteArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJByteArray");
    return NULL;
}

jarray BPy_ToJShortArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJShortArray");
    return NULL;
}

jarray BPy_ToJIntArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJIntArray");
    return NULL;
}

jarray BPy_ToJLongArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJLongArray");
    return NULL;
}

jarray BPy_ToJFloatArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJFloatArray");
    return NULL;
}

jarray BPy_ToJDoubleArray(PyObject* arg, jboolean* ok)
{
    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_ToJDoubleArray");
    return NULL;
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java To Python Conversion Functions

PyObject* BPy_FromJObject(PyTypeObject* type, jobject arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return JObject_FromType(type, arg);
}

PyObject* BPy_FromJString(jstring arg)
{
    const char* utf8Chars;
    PyObject* strPyObj;

    if (arg == NULL) {
        return Py_BuildValue("");
    }

    utf8Chars = (*jenv)->GetStringUTFChars(jenv, arg, 0);
    strPyObj = PyUnicode_FromString(utf8Chars);
    (*jenv)->ReleaseStringUTFChars(jenv, arg, utf8Chars);

    return strPyObj;
}

PyObject* BPy_FromJBooleanArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJBooleanArray");
    return NULL;
}

PyObject* BPy_FromJCharArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJCharArray");
    return NULL;
}

PyObject* BPy_FromJByteArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJByteArray");
    return NULL;
}

PyObject* BPy_FromJShortArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJShortArray");
    return NULL;
}

PyObject* BPy_FromJIntArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJIntArray");
    return NULL;
}

PyObject* BPy_FromJLongArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJLongArray");
    return NULL;
}

PyObject* BPy_FromJFloatArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJFloatArray");
    return NULL;
}

PyObject* BPy_FromJDoubleArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }

    PyErr_SetString(PyExc_NotImplementedError, "TODO - implement BPy_NewCArrayFromJDoubleArray");
    return NULL;
}

PyObject* BPy_FromJStringArray(jobjectArray arg)
{
    PyObject* listPyObj;
    jsize i, n;

    n = (*jenv)->GetArrayLength(jenv, arg);
    listPyObj = PyList_New(n);
    if (listPyObj == NULL) {
        return NULL;
    }

    for (i = 0; i < n; i++) {
        jobject itemJObj = (*jenv)->GetObjectArrayElement(jenv, arg, i);
        PyObject* itemPyObj = BPy_FromJString(itemJObj);
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

PyObject* BPy_FromJObjectArray(jobjectArray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return JObjectArray_New(arg);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Initialisation


jboolean BPy_InitJClass(jclass* cls, const char* classRef)
{
    if (*cls == NULL) {
        *cls = (*jenv)->FindClass(jenv, classRef);
        if (*cls == NULL) {
            char msg[1024];
            sprintf(msg, "Java class not found: %s", classRef);
            PyErr_SetString(PyExc_RuntimeError, msg);
            return 0;
        }
    }
    return 1;
}

jboolean BPy_InitJMethod(jmethodID* methodPtr, jclass cls, const char* className, const char* methodName, const char* methodSig, jboolean isstatic)
{
    //printf("methodPtr=%p, *methodPtr=%p, cls=%p, className=%s, methodName=%s, methodSig=%s, isstatic=%d\n",
    //       methodPtr,*methodPtr, cls, className, methodName, methodSig, isstatic);

    if (*methodPtr == NULL) {
        if (isstatic) {
            *methodPtr = (*jenv)->GetStaticMethodID(jenv, cls, methodName, methodSig);
        } else {
            *methodPtr = (*jenv)->GetMethodID(jenv, cls, methodName, methodSig);
        }
        if (*methodPtr == NULL) {
            char msg[1024];
            sprintf(msg, "Java method not found: %s: %s%s", className, methodName, methodSig);
            PyErr_SetString(PyExc_RuntimeError, msg);
            return 0;
        }
    }
    return 1;
}

jboolean BPy_InitConv() {
    static jboolean init = 0;
    if (!init) {
        init = 1;

        if (!BPy_InitJClass(&BPy_ObjectClass, "Ljava/lang/Object;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_ObjectArrayClass, "[Ljava/lang/Object;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_StringArrayClass, "[Ljava/lang/String;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_BooleanClass, "Ljava/lang/Boolean;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_CharacterClass, "Ljava/lang/Character;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_ByteClass, "Ljava/lang/Byte;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_ShortClass, "Ljava/lang/Short;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_IntegerClass, "Ljava/lang/Integer;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_LongClass, "Ljava/lang/Long;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_FloatClass, "Ljava/lang/Float;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_DoubleClass, "Ljava/lang/Double;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_StringClass, "Ljava/lang/String;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_MapClass, "Ljava/util/Map;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_HashMapClass, "Ljava/util/HashMap;")) {
            return 0;
        }


        if (!BPy_InitJMethod(&BPy_BooleanConstr, BPy_BooleanClass, "java.lang.Boolean", "<init>", "(Z)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_CharacterConstr, BPy_CharacterClass, "java.lang.Character", "<init>", "(C)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_ByteConstr, BPy_ByteClass, "java.lang.Byte", "<init>", "(B)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_ShortConstr, BPy_BooleanClass, "java.lang.Short", "<init>", "(S)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_IntegerConstr, BPy_IntegerClass, "java.lang.Integer", "<init>", "(I)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_LongConstr, BPy_FloatClass, "java.lang.Long", "<init>", "(L)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_FloatConstr, BPy_FloatClass, "java.lang.Float", "<init>", "(F)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_DoubleConstr, BPy_DoubleClass, "java.lang.Double", "<init>", "(D)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_HashMapConstr, BPy_HashMapClass, "java.util.HashMap", "<init>", "()V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_HashMapPut, BPy_HashMapClass, "java.util.HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", 0)) {
            return 0;
        }
    }
    return 1;
}


//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

