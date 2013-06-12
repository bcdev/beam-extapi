#include <stdlib.h>
#include <string.h>

#include <jni.h>

#include "../beam_util.h"
#include "${libName}.h"

static JavaVM* jvm = NULL;
static JNIEnv* jenv = NULL;

int beam_initApi();

jobjectArray beam_newJStringArray(const char** array_elems, int array_length);
jobjectArray beam_newJObjectArray(const jobject* obj_array_data, int obj_array_length, jclass comp_class);

void beam_copyFromJArray(jarray array, void* elems, int array_length, int elem_size);
void beam_copyToJArray(jarray array, const void* elems, int array_length, int elem_size);

char* beam_newCString(jstring str);
char** beam_newCStringArray(jarray array, int* array_length);
void** beam_newCObjectArray(jarray array, int* array_length);
void* beam_newCPrimitiveArray(jarray array, int* array_length, int elem_size);
boolean* beam_newCBooleanArray(jarray array, int* array_length);
char* beam_newCCharArray(jarray array, int* array_length);
byte* beam_newCByteArray(jarray array, int* array_length);
short* beam_newCShortArray(jarray array, int* array_length);
int* beam_newCIntArray(jarray array, int* array_length);
dlong* beam_newCLongArray(jarray array, int* array_length);
float* beam_newCFloatArray(jarray array, int* array_length);
double* beam_newCDoubleArray(jarray array, int* array_length);

