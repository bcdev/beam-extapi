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
void** beam_new_jobject_array_from_pyseq(PyObject* seq, int* length);