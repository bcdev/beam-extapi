
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
 * Test Python --> Java conversion
 */
PyObject* BPy_Py2J(PyObject* self, PyObject* args)
{
    jboolean ok = 1;
    PyObject* arg = NULL;
    PyObject* resultPyObj = NULL;
    jobject resultJObj = NULL;
    if (!BPy_InitApi()) {
        return NULL;
    }
printf("M1\n");
    if (!PyArg_ParseTuple(args, "O:BPy_Py2J", &arg)) {
        return NULL;
    }
printf("M2\n");
    resultJObj = BPy_ToJObject(arg, &ok);
    if (!ok) {
        return NULL;
    }
printf("M3\n");
    resultPyObj = BPy_FromJObject(&JObject_Type, resultJObj);
    return resultPyObj;
}

