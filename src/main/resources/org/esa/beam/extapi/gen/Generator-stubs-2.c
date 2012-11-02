jobjectArray beam_new_jstring_array(const char** array_elems, int array_length)
{
    jobjectArray array;
    int i;

    array = (*jenv)->NewObjectArray(jenv, array_length, classString, NULL);
    for (i = 0; i < array_length; i++) {
        jstring str = (*jenv)->NewStringUTF(jenv, array_elems[i]);
        (*jenv)->SetObjectArrayElement(jenv, array, i, str);
    }

    // todo: check if we must return (*jenv)->NewGlobalRef(jenv, array);
    return array;
}

jobjectArray beam_new_jobject_array(const Object* array_elems, int array_length, jclass comp_class)
{
    jobjectArray array;
    int i;

    array = (*jenv)->NewObjectArray(jenv, array_length, comp_class, NULL);
    for (i = 0; i < array_length; i++) {
        (*jenv)->SetObjectArrayElement(jenv, array, i, array_elems[i]);
    }

     // todo: check if we must return (*jenv)->NewGlobalRef(jenv, obj_array);
    return array;
}

void* beam_alloc_primitive_array(jarray array, size_t array_elem_size,  int* array_length)
{
    void* array_elems;
    void* array_addr;
    int n;

    n = (*jenv)->GetArrayLength(jenv, array);
    array_addr = (*jenv)->GetPrimitiveArrayCritical(jenv, array, NULL);
    array_elems = (boolean*) malloc(n * array_elem_size);
    memcpy(array_elems, array_addr, n * array_elem_size);
    (*jenv)->ReleasePrimitiveArrayCritical(jenv, array, array_addr, 0);
    if (array_length != NULL) {
        *array_length = n;
    }

    return array_elems;
}

boolean* beam_alloc_boolean_array(jarray array, int* array_length)
{
    return (boolean*) beam_alloc_primitive_array(array, sizeof (boolean), array_length);
}

char* beam_alloc_char_array(jarray array, int* array_length)
{
    return (char*) beam_alloc_primitive_array(array, sizeof (char), array_length);
}

byte* beam_alloc_byte_array(jarray array, int* array_length)
{
    return (byte*) beam_alloc_primitive_array(array, sizeof (byte), array_length);
}

short* beam_alloc_short_array(jarray array, int* array_length)
{
    return (short*) beam_alloc_primitive_array(array, sizeof (short), array_length);
}

int* beam_alloc_int_array(jarray array, int* array_length)
{
    return (int*) beam_alloc_primitive_array(array, sizeof (int), array_length);
}

dlong* beam_alloc_long_array(jarray array, int* array_length)
{
    return (dlong*) beam_alloc_primitive_array(array, sizeof (dlong), array_length);
}

float* beam_alloc_float_array(jarray array, int* array_length)
{
    return (float*) beam_alloc_primitive_array(array, sizeof (float), array_length);
}

double* beam_alloc_double_array(jarray array, int* array_length)
{
    return (double*) beam_alloc_primitive_array(array, sizeof (double), array_length);
}

Object* beam_alloc_object_array(jarray array, int* array_length)
{
    Object* array_elems;
    jsize n;
    jsize i;

    n = (*jenv)->GetArrayLength(jenv, array);

    array_elems = (Object*) malloc(n * sizeof (char*));
    for (i = 0; i < n; i++) {
        // todo: check if we must incremenet a global reference here!
        array_elems[i] = (*jenv)->GetObjectArrayElement(jenv, array, i);
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

void beam_free_string_array(char** array_elems, int array_length)
{
    int i;
    for (i = 0; i < array_length; i++) {
        if (array_elems[i] != NULL) {
            free(array_elems[i]);
        }
    }
    free(array_elems);
}

/* Shared library callbacks (called if this module's code is linked into a shared library and loaded by a Java VM) */

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    fprintf(stdout, "beam_capi: JNI_OnLoad() called\n");
    jvm = vm;
    return JNI_VERSION_1_4;
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

    options = (JavaVMOption*) calloc(option_count, sizeof (JavaVMOption));
    {
        int i;
        for (i = 0; i < option_count; i++) {
            options[i].optionString = (char*) option_strings[i];
        }
    }

    vm_args.version = JNI_VERSION_1_6;
    vm_args.options = options;
    vm_args.nOptions = 4;
    vm_args.ignoreUnrecognized = 0;
    res = JNI_CreateJavaVM(&jvm, (void**) &jenv, &vm_args);

    free(options);

    if (res != 0) {
        fprintf(stderr, "beam_capi error: JNI_CreateJavaVM failed with exit code %d\n", res);
        return JNI_FALSE;
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

/* 
 * The following code uses a hard-coded classpath.
 * TODO: Read environment variable 'BEAM_HOME' and generate classpath 
 *       by scanning the denoted directory.
 */

#define BEAM_HOME "C:\\Program Files\\beam-4.10.3\\" 
#define OS_PATH_SEP ";"
#define OS_FILE_SEP "\\"
#define BEAM_LJAR(F) BEAM_HOME ## "lib" ## OS_FILE_SEP ## F ## OS_PATH_SEP
#define BEAM_MJAR(F) BEAM_HOME ## "modules" ## OS_FILE_SEP ## F ## OS_PATH_SEP

static const char* beam_classpath =
    
    "-Djava.class.path="

    BEAM_LJAR("clibwrapper-jiio-1.2-20090918.jar")
    BEAM_LJAR("commons-codec-1.2.jar")
    BEAM_LJAR("commons-collections-3.1.jar")
    BEAM_LJAR("commons-httpclient-3.1.jar")
    BEAM_LJAR("commons-lang-2.1.jar")
    BEAM_LJAR("commons-logging-1.0.4.jar")
    BEAM_LJAR("commons-pool-1.5.4.jar")
    BEAM_LJAR("gt-api-2.7.4.jar")
    BEAM_LJAR("gt-coverage-2.7.4.jar")
    BEAM_LJAR("gt-cql-2.7.4.jar")
    BEAM_LJAR("gt-data-2.7.4.jar")
    BEAM_LJAR("gt-epsg-hsql-2.7.4.jar")
    BEAM_LJAR("gt-geotiff-2.7.4.jar")
    BEAM_LJAR("gt-main-2.7.4.jar")
    BEAM_LJAR("gt-metadata-2.7.4.jar")
    BEAM_LJAR("gt-opengis-2.7.4.jar")
    BEAM_LJAR("gt-referencing-2.7.4.jar")
    BEAM_LJAR("gt-render-2.7.4.jar")
    BEAM_LJAR("gt-shapefile-2.7.4.jar")
    BEAM_LJAR("gt-wms-2.7.4.jar")
    BEAM_LJAR("gt-xml-2.7.4.jar")
    BEAM_LJAR("hsqldb-1.8.0.7.jar")
    BEAM_LJAR("imageio-ext-tiff-1.0.8.jar")
    BEAM_LJAR("imageio-ext-utilities-1.0.8.jar")
    BEAM_LJAR("jai-codec-1.1.3.jar")
    BEAM_LJAR("jai-core-1.1.3.jar")
    BEAM_LJAR("jai-imageio-1.2-20090918.jar")
    BEAM_LJAR("Jama-1.0.2.jar")
    BEAM_LJAR("javahelp-2.0.02.jar")
    BEAM_LJAR("jcip-annotations-1.0.jar")
    BEAM_LJAR("jcommon-1.0.16.jar")
    BEAM_LJAR("jdom-1.0.jar")
    BEAM_LJAR("jdom-1.1.jar")
    BEAM_LJAR("jfreechart-1.0.13.jar")
    BEAM_LJAR("jide-action-3.3.7.jar")
    BEAM_LJAR("jide-common-3.3.7.jar")
    BEAM_LJAR("jide-components-3.3.7.jar")
    BEAM_LJAR("jide-dock-3.3.7.jar")
    BEAM_LJAR("jide-grids-3.3.7.jar")
    BEAM_LJAR("jnn-1.6.jar")
    BEAM_LJAR("js-1.7R1.jar")
    BEAM_LJAR("jsr-275-1.0-beta-2.jar")
    BEAM_LJAR("jtar-1.0.4.jar")
    BEAM_LJAR("jts-1.11.jar")
    BEAM_LJAR("junit-4.8.2.jar")
    BEAM_LJAR("jython-2.5.2.jar")
    BEAM_LJAR("mlibwrapper-jai-1.1.3.jar")
    BEAM_LJAR("netcdf-4.2.20.jar")
    BEAM_LJAR("nujan-1.4.1.jar")
    BEAM_LJAR("oro-2.0.8.jar")
    BEAM_LJAR( "slf4j-api-1.6.1.jar")
    BEAM_LJAR( "unidataCommon-4.2.20.jar")
    BEAM_LJAR("vecmath-1.3.2.jar")
    BEAM_LJAR("velocity-1.5.jar")
    BEAM_LJAR("xmlpull-1.1.3.1.jar")
    BEAM_LJAR("xpp3-1.1.4c.jar")
    BEAM_LJAR("xpp3_min-1.1.4c.jar")
    BEAM_LJAR("xstream-1.4.2.jar")

    BEAM_MJAR("ceres-binding-0.13.1.jar")
    BEAM_MJAR("ceres-binio-0.13.1.jar")
    BEAM_MJAR("ceres-core-0.13.1.jar")
    BEAM_MJAR("ceres-glayer-0.13.1.jar")
    BEAM_MJAR("ceres-jai-0.13.1.jar")
    BEAM_MJAR("ceres-ui-0.13.1.jar")
    BEAM_MJAR("flint-processor-1.2.jar")
    BEAM_MJAR("beam-aatsr-sst-1.5.1.jar")
    BEAM_MJAR("beam-alos-reader-1.3.1.jar")
    BEAM_MJAR("beam-atsr-reader-1.0.3.jar")
    BEAM_MJAR("beam-avhrr-reader-1.2.1.jar")
    BEAM_MJAR("beam-binning-2.1.1.jar")
    BEAM_MJAR("beam-chris-reader-1.7.1.jar")
    BEAM_MJAR("beam-cluster-analysis-1.1.2.jar")
    BEAM_MJAR("beam-collocation-1.4.1.jar")
    BEAM_MJAR("beam-core-4.10.3.jar")
    BEAM_MJAR("beam-csv-dataio-4.10.3.jar")
    BEAM_MJAR("beam-envisat-reader-1.5.1.jar")
    BEAM_MJAR("beam-flhmci-1.6.103.jar")
    BEAM_MJAR("beam-geotiff-1.2.1.jar")
    BEAM_MJAR("beam-getasse30-reader-1.0.1.jar")
    BEAM_MJAR("beam-gpf-4.10.3.jar")
    BEAM_MJAR("beam-hdf5-writer-1.0.1.jar")
    BEAM_MJAR("beam-landsat-reader-1.3.1.jar")
    BEAM_MJAR("beam-meris-brr-2.3.2.jar")
    BEAM_MJAR("beam-meris-case2-regional-1.5.5.jar")
    BEAM_MJAR("beam-meris-cloud-1.6.1.jar")
    BEAM_MJAR("beam-meris-glint-1.2.2.jar")
    BEAM_MJAR("beam-meris-l2auxdata-1.2.2.jar")
    BEAM_MJAR("beam-meris-ndvi-1.3.1.jar")
    BEAM_MJAR("beam-meris-radiometry-1.1.1.jar")
    BEAM_MJAR("beam-meris-sdr-2.3.2.jar")
    BEAM_MJAR("beam-meris-smac-1.5.204.jar")
    BEAM_MJAR("beam-merisl3-reader-1.2.1.jar")
    BEAM_MJAR("beam-modis-reader-1.3.jar")
    BEAM_MJAR("beam-mosaic-2.3.1.jar")
    BEAM_MJAR("beam-netcdf-1.1.1.jar")
    BEAM_MJAR("beam-obpg-reader-1.3.1.jar")
    BEAM_MJAR("beam-pconvert-1.4.1.jar")
    BEAM_MJAR("beam-pixel-extraction-1.2.jar")
    BEAM_MJAR("beam-processing-4.10.3.jar")
    BEAM_MJAR("beam-scripting-4.10.3.jar")
    BEAM_MJAR("beam-spot-vgt-reader-1.2.1.jar")
    BEAM_MJAR("beam-ui-4.10.3.jar")
    BEAM_MJAR("beam-unmix-1.2.1.jar")
    BEAM_MJAR("beam-visat-4.10.3.jar")
    BEAM_MJAR("beam-visat-rcp-4.10.3.jar")
    BEAM_MJAR("seadas-reader-1.3-20120621-01.jar");



jboolean beam_create_jvm_with_defaults() 
{
    const char* jvm_options[5];

    jvm_options[0] = beam_classpath;
    jvm_options[1] = "-Djava.library.path=c:\\mylibs";
    jvm_options[2] = "-Xms128M";
    jvm_options[3] = "-Xmx512M";
    jvm_options[4] = "-verbose:jni";

    return beam_create_jvm(jvm_options, 5);
}


