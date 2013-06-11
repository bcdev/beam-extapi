static PyObject* BeamPy_Error;


boolean* beampy_newCBooleanArrayFromPySeq(PyObject* seq, int* length);
char*    beampy_newCCharArrayFromPySeq(PyObject* seq, int* length);
byte*    beampy_newCByteArrayFromPySeq(PyObject* seq, int* length);
short*   beampy_newCShortArrayFromPySeq(PyObject* seq, int* length);
int*     beampy_newCIntArrayFromPySeq(PyObject* seq, int* length);
dlong*   beampy_newCDlongArrayFromPySeq(PyObject* seq, int* length);
float*   beampy_newCFloatArrayFromPySeq(PyObject* seq, int* length);
double*  beampy_newCDoubleArrayFromPySeq(PyObject* seq, int* length);
char**   beampy_newCStringArrayFromPySeq(PyObject* seq, int* length);
void**   beampy_newCObjectArrayFromPySeq(const char* type, PyObject* seq, int* length);

PyObject* beampy_newPySeqFromCBooleanArray(const boolean* elems, int length);
PyObject* beampy_newPySeqFromCCharArray(const char* elems, int length);
PyObject* beampy_newPySeqFromCByteArray(const byte* elems, int length);
PyObject* beampy_newPySeqFromCShortArray(const short* elems, int length);
PyObject* beampy_newPySeqFromCIntArray(const int* elems, int length);
PyObject* beampy_newPySeqFromCDlongArray(const dlong* elems, int length);
PyObject* beampy_newPySeqFromCFloatArray(const float* elems, int length);
PyObject* beampy_newPySeqFromCDoubleArray(const double* elems, int length);
PyObject* beampy_newPySeqFromCStringArray(const char** elems, int length);
PyObject* beampy_newPySeqFromCObjectArray(const char* type, const void** elems, int length);

/* Extra global functions for beampy. These will also go into the module definition. */
PyObject* BeamPyObject_delete(PyObject* self, PyObject* args);
PyObject* BeamPyString_newString(PyObject* self, PyObject* args);
PyObject* BeamPyMap_newHashMap(PyObject* self, PyObject* args);
