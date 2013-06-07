JavaVM* beam_getJavaVM()
{
    return jvm;
}

JNIEnv* beam_getJNIEnv()
{
    return jenv;
}

String String_newString(const char* chars)
{
    jstring str = (*jenv)->NewStringUTF(jenv, chars);
    return (*jenv)->NewGlobalRef(jenv, str);
}

jobjectArray beam_new_jstring_array(const char** array_elems, int array_length)
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

jobjectArray beam_new_jobject_array(const jobject* array_elems, int array_length, jclass comp_class)
{
    jobjectArray array;
    int i;

    array = (*jenv)->NewObjectArray(jenv, array_length, comp_class, NULL);
    for (i = 0; i < array_length; i++) {
        (*jenv)->SetObjectArrayElement(jenv, array, i, array_elems[i]);
    }

    return (*jenv)->NewGlobalRef(jenv, array);
}

void beam_release_jobject(void* object)
{
    if (object != NULL) {
        (*jenv)->DeleteGlobalRef(jenv, object);
    }
}

void beam_copy_from_jarray(jarray array, void* elems, int array_length, int elem_size)
{
    void* addr = (*jenv)->GetPrimitiveArrayCritical(jenv, array, NULL);
    memcpy(elems, addr, elem_size * array_length);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, array, addr, 0);
}

void beam_copy_to_jarray(jarray array, const void* elems, int array_length, int elem_size)
{
    void* addr = (*jenv)->GetPrimitiveArrayCritical(jenv, array, NULL);
    memcpy(addr, elems, elem_size * array_length);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, array, addr, 0);
}

void* beam_alloc_primitive_array(jarray array, int* array_length, int elem_size)
{
    void* elems;
    int n;

    n = (*jenv)->GetArrayLength(jenv, array);
    elems = (boolean*) malloc(n * elem_size);
    beam_copy_from_jarray(array, elems, elem_size, n);
    if (array_length != NULL) {
        *array_length = n;
    }

    return elems;
}

boolean* beam_alloc_boolean_array(jarray array, int* array_length)
{
    return (boolean*) beam_alloc_primitive_array(array, array_length, sizeof (boolean));
}

char* beam_alloc_char_array(jarray array, int* array_length)
{
    return (char*) beam_alloc_primitive_array(array, array_length, sizeof (char));
}

byte* beam_alloc_byte_array(jarray array, int* array_length)
{
    return (byte*) beam_alloc_primitive_array(array, array_length, sizeof (byte));
}

short* beam_alloc_short_array(jarray array, int* array_length)
{
    return (short*) beam_alloc_primitive_array(array, array_length, sizeof (short));
}

int* beam_alloc_int_array(jarray array, int* array_length)
{
    return (int*) beam_alloc_primitive_array(array, array_length, sizeof (int));
}

dlong* beam_alloc_long_array(jarray array, int* array_length)
{
    return (dlong*) beam_alloc_primitive_array(array, array_length, sizeof (dlong));
}

float* beam_alloc_float_array(jarray array, int* array_length)
{
    return (float*) beam_alloc_primitive_array(array, array_length, sizeof (float));
}

double* beam_alloc_double_array(jarray array, int* array_length)
{
    return (double*) beam_alloc_primitive_array(array, array_length, sizeof (double));
}

Object* beam_alloc_object_array(jarray array, int* array_length)
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


char* beam_alloc_string(jstring str)
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

void beam_release_string(char* chars)
{
    if (chars != NULL) {
        free(chars);
    }
}

char** beam_alloc_string_array(jarray array, int* array_length)
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

void beam_release_string_array(char** array_elems, int array_length)
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

void beam_release_object_array(void** array_elems, int array_length)
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
void beam_release_primitive_array(void* array_elems, int array_length)
{
     if (array_elems != NULL) {
          free(array_elems);
     }
}

/* Shared library callbacks (called if this module's code is linked into a shared library and loaded by a Java VM) */

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    fprintf(stdout, "beam_capi: JNI_OnLoad() called\n");
    jvm = vm;
    (*jvm)->GetEnv(vm, &jenv, JNI_VERSION_1_6);
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
    fprintf(stdout, "beam_capi: JNI_OnUnload() called\n");
    jvm = NULL;
    jenv = NULL;
}

/* Java VM functions that must be used if this module is used in stand-alone mode (= not loaded as shared library by a Java VM) */

jboolean beam_is_jvm_created() 
{
    return jvm != NULL;
}

jboolean beam_create_jvm(const char* option_strings[], int option_count) 
{
    JavaVMInitArgs vm_args; 
    JavaVMOption* options;
    int res;

    if (jvm != NULL) {
        return JNI_TRUE;
    }

    fprintf(stdout, "beam_capi: Creating Java VM using %d options:\n", option_count);

    options = (JavaVMOption*) calloc(option_count, sizeof (JavaVMOption));
    {
        int i;
        for (i = 0; i < option_count; i++) {
            options[i].optionString = (char*) option_strings[i];
            fprintf(stdout, "beam_capi: option_strings[%d] = \"%s\":\n", i, options[i].optionString);
        }
    }

    vm_args.version = JNI_VERSION_1_6;
    vm_args.options = options;
    vm_args.nOptions = option_count;
    vm_args.ignoreUnrecognized = 0;
    res = JNI_CreateJavaVM(&jvm, (void**) &jenv, &vm_args);

    free(options);

    if (res != 0) {
        fprintf(stderr, "beam_capi error: JNI_CreateJavaVM failed with exit code %d\n", res);
        return JNI_FALSE;
    } else {
        fprintf(stdout, "beam_capi: Java VM successfully created\n");
    }

    return JNI_TRUE;
}

jboolean beam_destroy_jvm() 
{
    jint res;

    if (jvm == NULL) {
        return JNI_TRUE;
    }
    
    res = (*jvm)->DestroyJavaVM(jvm);
    if (res != 0) {
        fprintf(stderr, "beam_capi error: DestroyJavaVM failed with exit code %d\n", res);
        return JNI_FALSE;
    }

    jvm = NULL;
    jenv = NULL;
    return JNI_TRUE;
}

#ifdef WIN32
#define OS_FILESEP "\\"
#define OS_PATHSEP ";"
#else
#define OS_FILESEP "/"
#define OS_PATHSEP ":"
#endif

void beam_create_class_path_vm_option_handler(const char* parent_dir, const char* file_name, int is_dir, void* user_data)
{
    char** class_path = (char**) user_data;

    if (strcmp(file_name, ".") == 0 || strcmp(file_name, "..") == 0) {
        return;
    }

    if (*class_path == NULL) {
        Util_appendString(class_path, "-Djava.class.path=");
    } else {
        Util_appendString(class_path, OS_PATHSEP);
    }

    Util_appendString(class_path, parent_dir);
    Util_appendString(class_path, OS_FILESEP);
    Util_appendString(class_path, file_name);
}

char* beam_create_class_path_vm_option()
{
    const char* beam_home;
    char* path;
    char* class_path;

    beam_home = getenv("BEAM_HOME");
    if (beam_home == NULL) {
        fprintf(stderr, "beam_capi: missing environment variable 'BEAM_HOME',\n");
        fprintf(stderr, "           please make sure 'BEAM_HOME' points to a valid BEAM installation directory.\n");
        return NULL;
    }

    class_path = NULL;

    path = NULL;
    Util_appendString(&path, beam_home);
    Util_appendString(&path, OS_FILESEP);
    Util_appendString(&path, "lib");
    Util_listDir(path, beam_create_class_path_vm_option_handler, &class_path);
    free(path);

    path = NULL;
    Util_appendString(&path, beam_home);
    Util_appendString(&path, OS_FILESEP);
    Util_appendString(&path, "modules");
    Util_listDir(path, beam_create_class_path_vm_option_handler, &class_path);
    free(path);

    return class_path;
}

#undef OS_FILESEP
#undef OS_PATHSEP

jboolean beam_create_jvm_with_defaults()
{
    const char* jvm_options[5];
    char* class_path_option;
    jboolean result;

    class_path_option = beam_create_class_path_vm_option();
    if (class_path_option == NULL) {
        const char* beam_home = getenv("BEAM_HOME");
        fprintf(stderr, "beam_capi: failed to construct Java classpath\n");
        if (beam_home != NULL) {
            fprintf(stderr, "           please make sure 'BEAM_HOME' points to a valid BEAM installation directory.\n");
            fprintf(stderr, "           Currently BEAM_HOME = %s\n", beam_home);
        }
        return JNI_FALSE;
    }

    fprintf(stdout, "beam_capi: %s\n", class_path_option);

    jvm_options[0] = class_path_option;
    /*jvm_options[1] = "-Djava.library.path=c:\\mylibs";*/
    jvm_options[1] = "-Djava.awt.headless=true";
    jvm_options[2] = "-Xms128M";
    jvm_options[3] = "-Xmx512M";
    jvm_options[4] = "-verbose:jni";

    result = beam_create_jvm(jvm_options, 5);

    free(class_path_option);

    return result;
}


