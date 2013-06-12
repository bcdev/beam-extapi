
String String_newString(const char* chars)
{
    jstring str = (*jenv)->NewStringUTF(jenv, chars);
    return (*jenv)->NewGlobalRef(jenv, str);
}

void Object_delete(Object object)
{
    if (object != NULL) {
        (*jenv)->DeleteGlobalRef(jenv, object);
    }
}

jobjectArray beam_newJStringArray(const char** array_elems, int array_length)
{
    jobjectArray array;
    int i;

    array = (*jenv)->NewObjectArray(jenv, array_length, classString, NULL);
    for (i = 0; i < array_length; i++) {
        jstring str = (*jenv)->NewStringUTF(jenv, array_elems[i]);
        (*jenv)->SetObjectArrayElement(jenv, array, i, str);
    }

    return (*jenv)->NewGlobalRef(jenv, array);
}

jobjectArray beam_newJObjectArray(const jobject* array_elems, int array_length, jclass comp_class)
{
    jobjectArray array;
    int i;

    array = (*jenv)->NewObjectArray(jenv, array_length, comp_class, NULL);
    for (i = 0; i < array_length; i++) {
        (*jenv)->SetObjectArrayElement(jenv, array, i, array_elems[i]);
    }

    return (*jenv)->NewGlobalRef(jenv, array);
}

void beam_copyFromJArray(jarray array, void* elems, int array_length, int elem_size)
{
    void* addr = (*jenv)->GetPrimitiveArrayCritical(jenv, array, NULL);
    memcpy(elems, addr, elem_size * array_length);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, array, addr, 0);
}

void beam_copyToJArray(jarray array, const void* elems, int array_length, int elem_size)
{
    void* addr = (*jenv)->GetPrimitiveArrayCritical(jenv, array, NULL);
    memcpy(addr, elems, elem_size * array_length);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, array, addr, 0);
}

void* beam_newCPrimitiveArray(jarray array, int* array_length, int elem_size)
{
    void* elems;
    int n;

    n = (*jenv)->GetArrayLength(jenv, array);
    elems = (boolean*) malloc(n * elem_size);
    beam_copyFromJArray(array, elems, elem_size, n);
    if (array_length != NULL) {
        *array_length = n;
    }

    return elems;
}

boolean* beam_newCBooleanArray(jarray array, int* array_length)
{
    return (boolean*) beam_newCPrimitiveArray(array, array_length, sizeof (boolean));
}

char* beam_newCCharArray(jarray array, int* array_length)
{
    return (char*) beam_newCPrimitiveArray(array, array_length, sizeof (char));
}

byte* beam_newCByteArray(jarray array, int* array_length)
{
    return (byte*) beam_newCPrimitiveArray(array, array_length, sizeof (byte));
}

short* beam_newCShortArray(jarray array, int* array_length)
{
    return (short*) beam_newCPrimitiveArray(array, array_length, sizeof (short));
}

int* beam_newCIntArray(jarray array, int* array_length)
{
    return (int*) beam_newCPrimitiveArray(array, array_length, sizeof (int));
}

dlong* beam_newCLongArray(jarray array, int* array_length)
{
    return (dlong*) beam_newCPrimitiveArray(array, array_length, sizeof (dlong));
}

float* beam_newCFloatArray(jarray array, int* array_length)
{
    return (float*) beam_newCPrimitiveArray(array, array_length, sizeof (float));
}

double* beam_newCDoubleArray(jarray array, int* array_length)
{
    return (double*) beam_newCPrimitiveArray(array, array_length, sizeof (double));
}

Object* beam_newCObjectArray(jarray array, int* array_length)
{
    Object* array_elems;
    jsize n;
    jsize i;

    n = (*jenv)->GetArrayLength(jenv, array);

    array_elems = (Object*) malloc(n * sizeof (char*));
    for (i = 0; i < n; i++) {
        jobject elem = (*jenv)->GetObjectArrayElement(jenv, array, i);
        array_elems[i] = (*jenv)->NewGlobalRef(jenv, elem);
    }

    if (array_length != NULL) {
        *array_length = n;
    }

    return array_elems;
}


char* beam_newCString(jstring str)
{
    int len = (*jenv)->GetStringUTFLength(jenv, str);
    const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);
    char* result = (char*) malloc((len + 1) * sizeof (char));
    if (result != NULL) {
        strcpy(result, chars);
    }
    (*jenv)->ReleaseStringUTFChars(jenv, str, chars);
    return result;
}

void beam_deleteCString(char* chars)
{
    if (chars != NULL) {
        free(chars);
    }
}

char** beam_newCStringArray(jarray array, int* array_length)
{
    char** array_elems;
    jsize n;
    jsize i;

    n = (*jenv)->GetArrayLength(jenv, array);

    array_elems = (char**) malloc(n * sizeof (char*));
    for (i = 0; i < n; i++) {
        jstring str = (*jenv)->GetObjectArrayElement(jenv, array, i);
        jsize len = (*jenv)->GetStringUTFLength(jenv, str);
        const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);

        char* elems = (char*) malloc((len + 1) * sizeof (char));
        strcpy(elems, chars);
        (*jenv)->ReleaseStringUTFChars(jenv, str, chars);
        array_elems[i] = elems;
    }

    if (array_length != NULL) {
        *array_length = n;
    }

    return array_elems;
}

void beam_deleteCStringArray(char** array_elems, int array_length)
{
    if (array_elems != NULL) {
        int i;
        for (i = 0; i < array_length; i++) {
            if (array_elems[i] != NULL) {
                free(array_elems[i]);
            }
        }
        free(array_elems);
    }
}

void beam_deleteCObjectArray(void** array_elems, int array_length)
{
    if (array_elems != NULL) {
        void* object;
        int i;
        for (i = 0; i < array_length; i++) {
             object = array_elems[i];
             (*jenv)->DeleteGlobalRef(jenv, object);
        }
        free(array_elems);
    }
}

// array_length currently not used, but useful for debugging
void beam_deleteCPrimitiveArray(void* array_elems, int array_length)
{
     if (array_elems != NULL) {
          free(array_elems);
     }
}
