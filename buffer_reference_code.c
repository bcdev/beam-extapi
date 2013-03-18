PyObject* BeamPyBand_readPixelsFloat(PyObject* self, PyObject* args)
{
    int x;
    int y;
    int w;
    int h;
    float* pixels = NULL;
    int pixelsLength;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;
    int resultLength;
    PyObject* resultObj;
    Py_buffer resultObjBuffer;

    if (!PyArg_ParseTuple(args, "(sK)iiiiO:Band_readPixelsFloat", &thisObjType, &thisObj, &x, &y, &w, &h, &resultObj)) {
        return NULL;
    }

    pixelsLength = w * h;
    resultObj = beam_getPrimitiveArrayBufferWritable(resultObj, &resultObjBuffer, "f", pixelsLength);
    if (resultObj == NULL) {
        return NULL;
    }

    pixels = (float*) resultObjBuffer.buf;
    Band_readPixelsFloat((Band) thisObj, x, y, w, h, pixels, pixelsLength, &resultLength);
    PyBuffer_Release(&resultObjBuffer);

    return resultObj;
}

PyObject* BeamPyBand_writePixelsFloat(PyObject* self, PyObject* args)
{
    int x;
    int y;
    int w;
    int h;
    float* pixels;
    int pixelsLength;
    PyObject* pixelsObj;
    Py_buffer pixelsBuf;
    const char* thisObjType;
    unsigned PY_LONG_LONG thisObj;

    if (!PyArg_ParseTuple(args, "(sK)iiiiO:Band_writePixelsFloat", &thisObjType, &thisObj, &x, &y, &w, &h, &pixelsObj)) {
        return NULL;
    }

    pixelsLength = w * h;
    pixelsObj = beam_getPrimitiveArrayBufferReadOnly(pixelsObj, &pixelsBuf, "f", pixelsLength);
    if (pixelsObj == NULL) {
        return NULL;
    }

    pixels = (float*) pixelsBuf.buf;
    Band_writePixelsFloat((Band) thisObj, x, y, w, h, pixels, pixelsLength);
    PyBuffer_Release(&pixelsBuf);

    return pixelsObj;
}
