#include "../beam_util.h"

static JavaVM* jvm = NULL;
static JNIEnv* jenv = NULL;
static int api_init = 0;

int beam_init_api();

jobjectArray beam_new_jstring_array(const char** array_elems, int array_length);
jobjectArray beam_new_jobject_array(const jobject* obj_array_data, int obj_array_length, jclass comp_class);

void beam_copy_from_jarray(jarray array, void* elems, int array_length, int elem_size);
void beam_copy_to_jarray(jarray array, const void* elems, int array_length, int elem_size);

char* beam_alloc_string(jstring str);
char** beam_alloc_string_array(jarray array, int* array_length);
void** beam_alloc_object_array(jarray array, int* array_length);
void* beam_alloc_primitive_array(jarray array, int* array_length, int elem_size);
boolean* beam_alloc_boolean_array(jarray array, int* array_length);
char* beam_alloc_char_array(jarray array, int* array_length);
byte* beam_alloc_byte_array(jarray array, int* array_length);
short* beam_alloc_short_array(jarray array, int* array_length);
int* beam_alloc_int_array(jarray array, int* array_length);
dlong* beam_alloc_long_array(jarray array, int* array_length);
float* beam_alloc_float_array(jarray array, int* array_length);
double* beam_alloc_double_array(jarray array, int* array_length);

