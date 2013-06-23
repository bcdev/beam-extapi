
JavaVM* jvm = NULL;
JNIEnv* jenv = NULL;

/* Shared library callbacks (called if this module's code is linked into a shared library and loaded by a Java VM) */

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    fprintf(stdout, "${libName}: JNI_OnLoad() called\n");
    jvm = vm;
    (*jvm)->GetEnv(vm, &jenv, JNI_VERSION_1_6);
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
    fprintf(stdout, "${libName}: JNI_OnUnload() called\n");
    jvm = NULL;
    jenv = NULL;
}

/* Java VM functions that must be used if this module is used in stand-alone mode (= not loaded as shared library by a Java VM) */

boolean beam_isJvmCreated()
{
    return jvm != NULL;
}

boolean beam_createJvm(const char* option_strings[], int option_count)
{
    JavaVMInitArgs vm_args; 
    JavaVMOption* options;
    int res;

    if (jvm != NULL) {
        return JNI_TRUE;
    }

    fprintf(stdout, "${libName}: creating Java VM using %d options\n", option_count);

    options = (JavaVMOption*) calloc(option_count, sizeof (JavaVMOption));
    {
        int i;
        for (i = 0; i < option_count; i++) {
            options[i].optionString = (char*) option_strings[i];
            fprintf(stdout, "${libName}: option(%d) = \"%s\"\n", i, options[i].optionString);
        }
    }

    vm_args.version = JNI_VERSION_1_6;
    vm_args.options = options;
    vm_args.nOptions = option_count;
    vm_args.ignoreUnrecognized = 0;
    res = JNI_CreateJavaVM(&jvm, (void**) &jenv, &vm_args);

    free(options);

    if (res != 0) {
        fprintf(stderr, "${libName}: JNI_CreateJavaVM failed with exit code %d\n", res);
        return JNI_FALSE;
    } else {
        fprintf(stdout, "${libName}: Java VM successfully created\n");
    }

    return JNI_TRUE;
}

boolean beam_destroyJvm()
{
    jint res;

    if (jvm == NULL) {
        return JNI_TRUE;
    }
    
    res = (*jvm)->DestroyJavaVM(jvm);
    if (res != 0) {
        fprintf(stderr, "${libName}: DestroyJavaVM failed with exit code %d\n", res);
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

void beam_createJvmClassPathOptionHandler(const char* parent_dir, const char* file_name, int is_dir, void* user_data)
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

char* beam_createJvmClassPathOption()
{
    const char* beam_home;
    char* path;
    char* class_path;

    beam_home = getenv("BEAM_HOME");
    if (beam_home == NULL) {
        fprintf(stderr, "${libName}: missing environment variable 'BEAM_HOME'\n");
        fprintf(stderr, "${libName}: please make sure 'BEAM_HOME' points to a valid BEAM installation directory\n");
        return NULL;
    }

    class_path = NULL;

    path = NULL;
    Util_appendString(&path, beam_home);
    Util_appendString(&path, OS_FILESEP);
    Util_appendString(&path, "lib");
    Util_listDir(path, beam_createJvmClassPathOptionHandler, &class_path);
    free(path);

    path = NULL;
    Util_appendString(&path, beam_home);
    Util_appendString(&path, OS_FILESEP);
    Util_appendString(&path, "modules");
    Util_listDir(path, beam_createJvmClassPathOptionHandler, &class_path);
    free(path);

    return class_path;
}

#undef OS_FILESEP
#undef OS_PATHSEP

boolean beam_createJvmWithDefaults()
{
    const char* jvm_options[16];
    char* class_path_option;
    boolean result;

    class_path_option = beam_createJvmClassPathOption();
    if (class_path_option == NULL) {
        const char* beam_home = getenv("BEAM_HOME");
        fprintf(stderr, "${libName}: failed to construct Java classpath\n");
        if (beam_home != NULL) {
            fprintf(stderr, "${libName}: please make sure 'BEAM_HOME' points to a valid BEAM installation directory\n");
            fprintf(stderr, "${libName}: currently BEAM_HOME = %s\n", beam_home);
        }
        return JNI_FALSE;
    }

    fprintf(stdout, "${libName}: %s\n", class_path_option);


    // use "-Djava.library.path=c:\\mylibs";*/
    // use "-verbose:jni";
    jvm_options[0] = class_path_option;
    jvm_options[1] = "-Djava.awt.headless=true";
    jvm_options[2] = "-Xms128M";
    jvm_options[3] = "-Xmx640M";
    result = beam_createJvm(jvm_options, 4);

    free(class_path_option);

    return result;
}



jclass beam_findJvmClass(const char* classResourceName)
{
    jclass c = (*jenv)->FindClass(jenv, classResourceName);
    if (c == NULL) {
        fprintf(stderr, "${libName}: Java class not found: %s\n", classResourceName);
    }
    return c;
}


// todo - the following functions actually belong in another module because they expect String and Object typedefs to be present

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

