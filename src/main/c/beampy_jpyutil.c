#include "beampy_jpyutil.h"


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java Core Variables

static jboolean BPy_ModuleInit = 0;


static jclass BPy_ObjectClass = NULL;
static jclass BPy_ObjectArrayClass = NULL;
static jclass BPy_StringArrayClass = NULL;

static jclass BPy_BooleanClass = NULL;
static jmethodID BPy_BooleanConstr = NULL;

static jclass BPy_CharacterClass = NULL;
static jmethodID BPy_CharacterConstr = NULL;

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

/*
static jclass  BPy_ArrayListClass = NULL;
static jmethodID BPy_ArrayListConstr = NULL;
static jmethodID BPy_ArrayListSet = NULL;
static jmethodID BPy_ArrayListGet = NULL;
static jmethodID BPy_ArrayListSize = NULL;
*/


static jclass  BPy_RectangleClass = NULL;
static jmethodID BPy_RectangleConstr = NULL;

static jclass  BPy_FileClass = NULL;
static jmethodID BPy_FileConstr = NULL;


#define BPY_CHECK_JPYUTIL() if (!BPy_CheckJPyUtil()) { return BPy_ConvFailure(NULL, ok); } else {}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Local function declarations



typedef jarray (JNICALL *BPy_NewJArrayFn)(JNIEnv*, jsize);

jarray BPy_NewGenericJPrimitiveArrayFromBuffer(const void* buffer, jint bufferLength, size_t elemSize, BPy_NewJArrayFn NewJArray);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Python To Java Conversion Functions

jobject BPy_ConvSuccess(jobject arg, jboolean* ok)
{
    *ok = (jboolean) 1;
//printf("ok=%d\n", *ok);
    return arg;
}

jobject BPy_ConvFailure(const char* msg, jboolean* ok)
{
    if (msg != NULL) {
        PyErr_SetString(PyExc_ValueError, msg);
    }
    *ok = (jboolean) 0;
//printf("ok=%d\n", *ok);
    return NULL;
}

jarray BPy_NewGenericJPrimitiveArrayFromBuffer(const void* buffer, jint bufferLength, size_t elemSize, BPy_NewJArrayFn NewJArray)
{
    jarray arrayJObj;
    arrayJObj = NewJArray(jenv, bufferLength);
    if (arrayJObj != NULL) {
        void* addr = (*jenv)->GetPrimitiveArrayCritical(jenv, arrayJObj, NULL);
        if (addr != NULL) {
            memcpy(addr, buffer, bufferLength * elemSize);
            (*jenv)->ReleasePrimitiveArrayCritical(jenv, arrayJObj, addr, 0);
            return arrayJObj;
        } else {
            (*jenv)->DeleteLocalRef(jenv, arrayJObj);
            PyErr_SetString(PyExc_RuntimeError, "jenv->GetPrimitiveArrayCritical() failed");
            return NULL;
        }
    } else {
        PyErr_SetString(PyExc_RuntimeError, "jenv->New<T>Array() failed");
        return NULL;
    }

}

jarray BPy_NewJBooleanArrayFromBuffer(const jboolean* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jboolean), (*jenv)->NewBooleanArray);
}

jarray BPy_NewJCharArrayFromBuffer(const jchar* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jchar), (*jenv)->NewCharArray);
}

jarray BPy_NewJByteArrayFromBuffer(const jbyte* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jbyte), (*jenv)->NewByteArray);
}

jarray BPy_NewJShortArrayFromBuffer(const jshort* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jshort), (*jenv)->NewShortArray);
}

jarray BPy_NewJIntArrayFromBuffer(const jint* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jint), (*jenv)->NewIntArray);
}

jarray BPy_NewJLongArrayFromBuffer(const jlong* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jlong), (*jenv)->NewLongArray);
}

jarray BPy_NewJFloatArrayFromBuffer(const jfloat* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jfloat), (*jenv)->NewFloatArray);
}

jarray BPy_NewJDoubleArrayFromBuffer(const jdouble* buffer, jint bufferLength)
{
    return BPy_NewGenericJPrimitiveArrayFromBuffer(buffer, bufferLength, sizeof (jdouble), (*jenv)->NewDoubleArray);
}


jobject BPy_NewJBooleanFromBool(PyObject* arg, jboolean* ok)
{
    jobject argJObj = NULL;

    BPY_CHECK_JPYUTIL()

    argJObj = (*jenv)->NewObject(jenv, BPy_BooleanClass, BPy_BooleanConstr, (jboolean) PyObject_IsTrue(arg));;
    if (argJObj == NULL) {
        return BPy_ConvFailure("jenv->NewObject() failed", ok);
    }
    return BPy_ConvSuccess(argJObj, ok);
}

jobject BPy_NewJNumberFromInt(PyObject* arg, jboolean* ok)
{
    jobject argJObj = NULL;
    PY_LONG_LONG v;

    BPY_CHECK_JPYUTIL()

    v = PyLong_AsLongLong(arg);
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
        return BPy_ConvFailure("jenv->NewObject() failed", ok);
    }
    return BPy_ConvSuccess(argJObj, ok);
}

jobject BPy_NewJNumberFromFloat(PyObject* arg, jboolean* ok)
{
    jobject argJObj = NULL;
    double v;

    BPY_CHECK_JPYUTIL()

    v = PyFloat_AsDouble(arg);
    if (v == (jfloat) v) {
        argJObj = (*jenv)->NewObject(jenv, BPy_FloatClass, BPy_FloatConstr, (jfloat) v);
    } else if (v == (jdouble) v) {
        argJObj = (*jenv)->NewObject(jenv, BPy_DoubleClass, BPy_DoubleConstr, (jdouble) v);
    } else {
        return BPy_ConvFailure("missing appropriate Java representation of argument", ok);
    }
    if (argJObj == NULL) {
        return BPy_ConvFailure("jenv->NewObject() failed", ok);
    }
    return BPy_ConvSuccess(argJObj, ok);
}

jstring BPy_NewJStringFromStr(PyObject* arg, jboolean* ok)
{
    PyObject* strPyObj;
    jstring strJObj = NULL;

    BPY_CHECK_JPYUTIL()

    strPyObj = PyObject_Str(arg);
    if (strPyObj == NULL) {
        return BPy_ConvFailure("argument of type 'str' expected", ok);
    }

    // todo: from Python 3.3 on, remove the following code...
    if (sizeof (jchar) == sizeof (wchar_t)) {
        wchar_t* ucStr;
        Py_ssize_t ucLen;
        ucStr = PyUnicode_AsWideCharString(strPyObj, &ucLen);
        if (ucStr == NULL) {
            Py_DECREF(strPyObj);
            return BPy_ConvFailure(NULL, ok);
        }
        strJObj = (*jenv)->NewString(jenv, (jchar*) ucStr, (jsize) ucLen);
        PyMem_Free(ucStr);
    } else if(sizeof (jchar) == sizeof (Py_UNICODE)) {
        Py_UNICODE* ucStr;
        Py_ssize_t ucLen;
        ucStr = PyUnicode_AsUnicode(strPyObj);
        if (ucStr == NULL) {
            Py_DECREF(strPyObj);
            return BPy_ConvFailure(NULL, ok);
        }
        ucLen = PyUnicode_GetSize(strPyObj);
        strJObj = (*jenv)->NewString(jenv, (jchar*) ucStr, (jsize) ucLen);
    } else {
        PyObject* ascPyObj;
        char* ascStr;
        ascPyObj = PyUnicode_AsASCIIString(strPyObj);
        if (ascPyObj == NULL) {
            Py_DECREF(strPyObj);
            return BPy_ConvFailure(NULL, ok);
        }
        ascStr = PyBytes_AsString(ascPyObj);
        if (ascStr == NULL) {
            Py_DECREF(strPyObj);
            return BPy_ConvFailure(NULL, ok);
        }
        strJObj = (*jenv)->NewStringUTF(jenv, ascStr);
    }

    // todo: ... and use this one instead
    /* // {{{
    char* utf8Chars;
    utf8Chars = PyUnicode_AsUTF8(strPyObj);
    strJObj = (*jenv)->NewStringUTF(jenv, utf8Chars);
    // }}} */

    Py_DECREF(strPyObj);
    if (strJObj == NULL) {
        return BPy_ConvFailure("Failed to create Java string from Python string", ok);
    }

    return BPy_ConvSuccess(strJObj, ok);
}


jobject BPy_NewJMapFromDict(PyObject* arg, jboolean* ok)
{
    PyObject* dictKeyPyObj;
    PyObject* dictValuePyObj;
    Py_ssize_t dictPos = 0;
    jobject mapJObj = NULL;

    BPY_CHECK_JPYUTIL()

    mapJObj = (*jenv)->NewObject(jenv, BPy_HashMapClass, BPy_HashMapConstr);
    if (mapJObj == NULL) {
        return BPy_ConvFailure("jenv->NewObject() failed", ok);
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

#define MAX_JARRAY_SIZE  ((int)(((unsigned int)1 << 31) - 1))

jobjectArray BPy_NewJObjectArrayFromSeqT(PyObject* arg, jclass compType, jboolean* ok)
{
    jarray arrayJObj;
    Py_ssize_t size;
    Py_ssize_t i;

    BPY_CHECK_JPYUTIL()

    size = PySequence_Size(arg);
    if (size < 0 || size >= MAX_JARRAY_SIZE) {
        char msg[256];
        sprintf(msg, "invalid sequence size: %d", size);
        return BPy_ConvFailure(msg, ok);
    }

    arrayJObj = (*jenv)->NewObjectArray(jenv, (jsize) size, compType, NULL);
    if (arrayJObj == NULL) {
        return BPy_ConvFailure("jenv->NewObjectArray() failed", ok);
    }

    for (i = 0; i < size; i++) {
        PyObject* itemPyObj = PySequence_GetItem(arg, i);
        if (itemPyObj == NULL) {
            (*jenv)->DeleteLocalRef(jenv, arrayJObj);
            // msg==NULL, because error message already set by PySequence_GetItem
            return BPy_ConvFailure(NULL, ok);
        }
        (*jenv)->SetObjectArrayElement(jenv, arrayJObj, (jint) i, BPy_ToJObjectT(itemPyObj, compType, ok));
    }

    return BPy_ConvSuccess(arrayJObj, ok);
}

jobjectArray BPy_NewJStringArrayFromSeq(PyObject* arg, jboolean* ok)
{
    return BPy_NewJObjectArrayFromSeqT(arg, BPy_StringClass, ok);
}

jobjectArray BPy_NewJObjectArrayFromSeq(PyObject* arg, jboolean* ok)
{
    return BPy_NewJObjectArrayFromSeqT(arg, BPy_ObjectClass, ok);
}

/**
 * Checks for Py_None or for JObject and returns NULL or the Java reference.
 * Note, no error message will be set, caller must check ok flag and proceed on failure.
 */
jobject BPy_ToJObjectDefault(PyObject* arg, jclass type, jboolean* ok)
{
    jobject argJObj;

    if (arg == Py_None) {
//printf("BPy_ToJObjectDefault: none\n");
        return BPy_ConvSuccess(NULL, ok);
    }

//printf("BPy_ToJObjectDefault: not none\n");
    argJObj = JObject_AsJObjectRefT(arg, type);
    if (argJObj != NULL) {
//printf("BPy_ToJObjectDefault: JObject argJObj=%p\n", argJObj);
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, argJObj), ok);
    }
//printf("BPy_ToJObjectDefault: else\n");

    return BPy_ConvFailure(NULL, ok);
}

/**
 * Checks for Py_None or for JObjectArray and returns NULL or the Java Array reference.
 * Note, no error message will be set, caller must check ok flag and proceed on failure.
 */
jobjectArray BPy_ToJObjectArrayDefault(PyObject* arg, jclass compType, jboolean* ok)
{
    jobjectArray argJObj;

    if (arg == Py_None) {
        return BPy_ConvSuccess(NULL, ok);
    }

    argJObj = JObject_AsJObjectArrayRefT(arg, compType);
    if (argJObj != NULL) {
        return BPy_ConvSuccess((*jenv)->NewLocalRef(jenv, argJObj), ok);
    }

    return BPy_ConvFailure(NULL, ok);
}

jobject BPy_ToJObjectGeneric(PyObject* arg, jboolean* ok)
{
    jobject argJObj = BPy_ToJObjectDefault(arg, BPy_ObjectClass, ok);
    if (*ok) {
        return argJObj;
    }

//printf("BPy_ToJObjectGeneric: no default\n");
    if (PyBool_Check(arg)) {
//printf("BPy_ToJObjectGeneric: bool\n");
        return BPy_NewJBooleanFromBool(arg, ok);
    } else if (PyLong_Check(arg)) {
//printf("BPy_ToJObjectGeneric: int\n");
        return BPy_NewJNumberFromInt(arg, ok);
    } else if (PyFloat_Check(arg)) {
//printf("BPy_ToJObjectGeneric: float\n");
        return BPy_NewJNumberFromFloat(arg, ok);
    } else if (PyUnicode_Check(arg)) {
//printf("BPy_ToJObjectGeneric: unicode\n");
        return BPy_NewJStringFromStr(arg, ok);
    } else if (PyDict_Check(arg)) {
//printf("BPy_ToJObjectGeneric: dict\n");
        return BPy_NewJMapFromDict(arg, ok);
    } else if (PySequence_Check(arg)) {
//printf("BPy_ToJObjectGeneric: seq\n");
        return BPy_NewJObjectArrayFromSeq(arg, ok);
    }
//printf("BPy_ToJObjectGeneric: else\n");

    return BPy_ConvFailure("missing appropriate Java representation of argument", ok);
}

jobject BPy_ToJObjectT(PyObject* arg, jclass type, jboolean* ok)
{
    jobject argJObj;

//printf("BPy_ToJObjectT: arg %p, %s\n", arg, arg->ob_type->tp_name);

    argJObj = BPy_ToJObjectDefault(arg, type, ok);
    if (*ok) {
//printf("BPy_ToJObjectT: default %p\n", argJObj);
        return argJObj;
    }

//printf("BPy_ToJObjectT: no default\n");
    if (type == BPy_StringClass) {
//printf("BPy_ToJObjectT: str\n");
        return BPy_ToJString(arg, ok);
    }
//printf("BPy_ToJObjectT: gen\n");

    // todo - this is only correct for type == java.lang.Object. Check type and perform appropriate conversions
    return BPy_ToJObjectGeneric(arg, ok);
}

jobject BPy_ToJObject(PyObject* arg, jboolean* ok)
{
    return BPy_ToJObjectT(arg, BPy_ObjectClass, ok);
}

jstring BPy_ToJString(PyObject* arg, jboolean* ok)
{
    jobject argJObj = BPy_ToJObjectDefault(arg, BPy_StringClass, ok);
    if (*ok) {
        return argJObj;
    }

    if (PyUnicode_Check(arg)) {
//printf("BPy_ToJString: str\n");
        return BPy_NewJStringFromStr(arg, ok);
    }
//printf("BPy_ToJString: err\n");

    return BPy_ConvFailure("argument must be a 'str'", ok);
}

jobject BPy_ToJMap(PyObject* arg, jboolean* ok)
{
    jobject argJObj = BPy_ToJObjectDefault(arg, BPy_MapClass, ok);
    if (*ok) {
        return argJObj;
    }

    if (PyDict_Check(arg)) {
        return BPy_NewJMapFromDict(arg, ok);
    }

    return BPy_ConvFailure("argument must be a 'dict'", ok);
}

jobject BPy_ToJList(PyObject* arg, jboolean* ok)
{
    jobject argJObj = BPy_ToJObjectDefault(arg, BPy_ListClass, ok);
    if (*ok) {
        return argJObj;
    }
    return BPy_ConvFailure("TODO - implement conversion from Python sequence to java.util.List", ok);
}

jobjectArray BPy_ToJObjectArray(PyObject* arg, jboolean* ok)
{
    return BPy_ToJObjectArrayT(arg, BPy_ObjectClass, ok);
}

jobjectArray BPy_ToJStringArray(PyObject* arg, jboolean* ok)
{
    return BPy_ToJObjectArrayT(arg, BPy_StringClass, ok);
}


jobjectArray BPy_ToJObjectArrayT(PyObject* arg, jclass compType, jboolean* ok)
{
    jobjectArray argJObj = BPy_ToJObjectArrayDefault(arg, compType, ok);
    if (*ok) {
        return argJObj;
    }

    if (PySequence_Check(arg)) {
        return BPy_NewJObjectArrayFromSeqT(arg, compType, ok);
    }

    return BPy_ConvFailure("argument must be a sequence", ok);
}

/*
jarray BPy_ToJBooleanArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java boolean[]", ok);
}

jarray BPy_ToJCharArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java char[]", ok);
}

jarray BPy_ToJByteArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java byte[]", ok);
}

jarray BPy_ToJShortArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java short[]", ok);
}

jarray BPy_ToJIntArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java int[]", ok);
}

jarray BPy_ToJLongArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java long[]", ok);
}

jarray BPy_ToJFloatArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java float[]", ok);
}

jarray BPy_ToJDoubleArray(PyObject* arg, jboolean* ok)
{
    return BPy_ConvFailure("TODO - implement conversion to Java double[]", ok);
}
*/

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Java To Python Conversion Functions

PyObject* BPy_CopyGenericJPrimitiveArrayToBuffer(jarray arrayJObj, void* buffer, jint bufferLength, size_t elemSize, PyObject* bufferPyObj)
{
    void* addr;
    jsize n;

    n = (*jenv)->GetArrayLength(jenv, arrayJObj);
    if (bufferLength < n) {
         PyErr_SetString(PyExc_ValueError, "array buffer too small");
         return NULL;
    }

    addr = (*jenv)->GetPrimitiveArrayCritical(jenv, arrayJObj, NULL);
    if (addr == NULL) {
        PyErr_SetString(PyExc_RuntimeError, "jenv->GetPrimitiveArrayCritical() failed");
        return NULL;
    }

    memcpy(buffer, addr, bufferLength * elemSize);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, arrayJObj, addr, 0);

    return bufferPyObj;
}

PyObject* BPy_CopyJBooleanArrayToBuffer(jarray arrayJObj, jboolean* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jboolean), bufferPyObj);
}

PyObject* BPy_CopyJCharArrayToBuffer(jarray arrayJObj, jchar* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jchar), bufferPyObj);
}

PyObject* BPy_CopyJByteArrayToBuffer(jarray arrayJObj, jbyte* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jbyte), bufferPyObj);
}

PyObject* BPy_CopyJShortArrayToBuffer(jarray arrayJObj, jshort* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jshort), bufferPyObj);
}

PyObject* BPy_CopyJIntArrayToBuffer(jarray arrayJObj, jint* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jint), bufferPyObj);
}

PyObject* BPy_CopyJLongArrayToBuffer(jarray arrayJObj, jlong* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jlong), bufferPyObj);
}

PyObject* BPy_CopyJFloatArrayToBuffer(jarray arrayJObj, jfloat* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jfloat), bufferPyObj);
}

PyObject* BPy_CopyJDoubleArrayToBuffer(jarray arrayJObj, jdouble* buffer, jint bufferLength, PyObject* bufferPyObj)
{
    return BPy_CopyGenericJPrimitiveArrayToBuffer(arrayJObj, buffer, bufferLength, sizeof (jdouble), bufferPyObj);
}

PyObject* BPy_NewBufferFromJPrimitiveArray(jarray arrayJObj, const char* format)
{
    PyObject* bufferPyObj;
    void* addr;
    jsize arrayLength;

    arrayLength = (*jenv)->GetArrayLength(jenv, arrayJObj);

    addr = (*jenv)->GetPrimitiveArrayCritical(jenv, arrayJObj, NULL);
    if (addr == NULL) {
        PyErr_SetString(PyExc_RuntimeError, "GetPrimitiveArrayCritical() failed");
        return NULL;
    }

    bufferPyObj = CArray_FromMemory(format, addr, arrayLength, CArray_FreeMemory);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, arrayJObj, addr, 0);

    return bufferPyObj;
}



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
    if (utf8Chars == NULL) {
        PyErr_SetString(PyExc_RuntimeError, "jenv->GetStringUTFChars() failed");
        return NULL;
    }
    strPyObj = PyUnicode_FromString(utf8Chars);
    (*jenv)->ReleaseStringUTFChars(jenv, arg, utf8Chars);

    return strPyObj;
}

// todo - implement the following BPy_FromJ<T>Array functions

PyObject* BPy_FromJBooleanArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "b");
}

PyObject* BPy_FromJCharArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "h");
}

PyObject* BPy_FromJByteArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "b");
}

PyObject* BPy_FromJShortArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "h");
}

PyObject* BPy_FromJIntArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "i");
}

PyObject* BPy_FromJLongArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "l");
}

PyObject* BPy_FromJFloatArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "f");
}

PyObject* BPy_FromJDoubleArray(jarray arg)
{
    if (arg == NULL) {
        return Py_BuildValue("");
    }
    return BPy_NewBufferFromJPrimitiveArray(arg, "d");
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
// Factory functions for special Java objects used by the BEAM Java API, e.g. File, Rectangle

PyObject* BPy_NewRectangle(PyObject* self, PyObject* args)
{
    jint x = 0;
    jint y = 0;
    jint width = 0;
    jint height = 0;
    jobject rectangleJObj = NULL;

    if (!BPy_CheckJPyUtil()) {
        return NULL;
    }

    if (!PyArg_ParseTuple(args, "iiii:newRectangle", &x, &y, &width, &height)) {
        return NULL;
    }

    rectangleJObj = (*jenv)->NewObject(jenv, BPy_RectangleClass, BPy_RectangleConstr, x, y, width, height);
    if (rectangleJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "jenv->NewObject() failed");
        return NULL;
    }

    return BPy_FromJObject(&JObject_Type, rectangleJObj);
}

PyObject* BPy_NewFile(PyObject* self, PyObject* args)
{
    const char* path = NULL;
    jobject pathJObj = NULL;
    jobject fileJObj = NULL;

    if (!BPy_CheckJPyUtil()) {
        return NULL;
    }

    if (!PyArg_ParseTuple(args, "s:newFile", &path)) {
        return NULL;
    }

    pathJObj = (*jenv)->NewStringUTF(jenv, path);
    if (pathJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "jenv->NewStringUTF() failed");
        return NULL;
    }

    fileJObj = (*jenv)->NewObject(jenv, BPy_FileClass, BPy_FileConstr, pathJObj);
    if (fileJObj == NULL) {
        PyErr_SetString(PyExc_ValueError, "jenv->NewObject() failed");
        return NULL;
    }

    return BPy_FromJObject(&JObject_Type, fileJObj);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Initialisation


jboolean BPy_InitJClass(jclass* cls, const char* classRef)
{
    if (*cls == NULL) {
        *cls = (*jenv)->FindClass(jenv, classRef);
        if (*cls == NULL) {
            char msg[1024];
            sprintf(msg, "BPy_InitJClass: Java class not found: %s", classRef);
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
            sprintf(msg, "BPy_InitJMethod: Java method not found: %s: %s%s", className, methodName, methodSig);
            PyErr_SetString(PyExc_RuntimeError, msg);
            return 0;
        }
    }
    return 1;
}

jboolean BPy_CheckJPyUtil()
{
    if (!BPy_ModuleInit) {
        PyErr_SetString(PyExc_RuntimeError, "you must call BPy_InitJPyUtil() before you can use this function");
    }
    return BPy_ModuleInit;
}


jboolean BPy_InitJPyUtil()
{
    if (!BPy_ModuleInit) {

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
        if (!BPy_InitJClass(&BPy_FileClass, "Ljava/io/File;")) {
            return 0;
        }
        if (!BPy_InitJClass(&BPy_RectangleClass, "Ljava/awt/Rectangle;")) {
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
        if (!BPy_InitJMethod(&BPy_ShortConstr, BPy_ShortClass, "java.lang.Short", "<init>", "(S)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_IntegerConstr, BPy_IntegerClass, "java.lang.Integer", "<init>", "(I)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_LongConstr, BPy_LongClass, "java.lang.Long", "<init>", "(J)V", 0)) {
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
        if (!BPy_InitJMethod(&BPy_FileConstr, BPy_FileClass, "java.io.File", "<init>", "(Ljava/lang/String;)V", 0)) {
            return 0;
        }
        if (!BPy_InitJMethod(&BPy_RectangleConstr, BPy_RectangleClass, "java.awt.Rectangle", "<init>", "(IIII)V", 0)) {
            return 0;
        }

        BPy_ModuleInit = 1;
    }
    return BPy_ModuleInit;
}


//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

