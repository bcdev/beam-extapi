
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
