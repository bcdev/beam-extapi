
#include "${libName}.h"

#include <Python.h>
#include "structmember.h"
#include <jni.h>

#include "../beam_util.h"

static PyObject* BeamPy_Error;

int beam_initApi();

jboolean BPy_InitApi()
{
    int errCode = beam_initApi();
    if (errCode != 0) {
        char msg[64];
        sprintf(msg, "beam_initApi() failed with error code %d", errCode);
        PyErr_SetString(BeamPy_Error, msg);
        return 0;
    }
    return 1;
}

/**
 * Test Python --> Java conversion. In Python, use e.g.: beampy.to_jobject('abc')
 */
PyObject* BPy_to_jobject(PyObject* self, PyObject* args)
{
    jboolean ok = 1;
    PyObject* arg = NULL;
    PyObject* resultPyObj = NULL;
    jobject resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
    if (!PyArg_ParseTuple(args, "O:to_jobject", &arg)) {
        return NULL;
    }
    resultJObj = BPy_ToJObject(arg, &ok);
    if (!ok) {
        return NULL;
    }
    resultPyObj = BPy_FromJObject(&JObject_Type, resultJObj);
    return resultPyObj;
}

