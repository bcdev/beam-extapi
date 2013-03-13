static PyObject* BeamPy_Error;

boolean* beam_new_boolean_array_from_pyseq(PyObject* seq, int* length);
char* beam_new_char_array_from_pyseq(PyObject* seq, int* length);
byte* beam_new_byte_array_from_pyseq(PyObject* seq, int* length);
short* beam_new_short_array_from_pyseq(PyObject* seq, int* length);
int* beam_new_int_array_from_pyseq(PyObject* seq, int* length);
dlong* beam_new_dlong_array_from_pyseq(PyObject* seq, int* length);
float* beam_new_float_array_from_pyseq(PyObject* seq, int* length);
double* beam_new_double_array_from_pyseq(PyObject* seq, int* length);
char** beam_new_string_array_from_pyseq(PyObject* seq, int* length);
void** beam_new_jobject_array_from_pyseq(const char* type, PyObject* seq, int* length);

PyObject* beam_new_pyseq_from_boolean_array(const boolean* elems, int length);
PyObject* beam_new_pyseq_from_char_array(const char* elems, int length);
PyObject* beam_new_pyseq_from_byte_array(const byte* elems, int length);
PyObject* beam_new_pyseq_from_short_array(const short* elems, int length);
PyObject* beam_new_pyseq_from_int_array(const int* elems, int length);
PyObject* beam_new_pyseq_from_dlong_array(const dlong* elems, int length);
PyObject* beam_new_pyseq_from_float_array(const float* elems, int length);
PyObject* beam_new_pyseq_from_double_array(const double* elems, int length);
PyObject* beam_new_pyseq_from_string_array(const char** elems, int length);
PyObject* beam_new_pyseq_from_jobject_array(const char* type, const void** elems, int length);

/* Extra global functions for beampy. These will also go into the module definition. */
PyObject* BeamPyString_newString(PyObject* self, PyObject* args);