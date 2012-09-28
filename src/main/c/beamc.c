#include "beamc.h"

#define STRING_CLASS     "java/lang/String"
#define PRODUCT_IO_CLASS "org/esa/beam/framework/dataio/ProductIO"
#define PRODUCT_CLASS    "org/esa/beam/framework/datamodel/Product"

static JavaVM* jvm = NULL; 
static JNIEnv* jenv = NULL;

#define RETURN_IF_NO_JVM_V() if (jvm == NULL) {return;}
#define RETURN_IF_NO_JVM(A) if (jvm == NULL) {return (A);}

Product beam_read_product(const char* file_path)
{
	jclass product_io_class;
	jmethodID method;
	jobject product;
	jstring file_path_s;

	BEAM_TRACE("beam_read_product 0\n");

	RETURN_IF_NO_JVM(NULL);

	BEAM_TRACE("beam_read_product 1\n");

	product_io_class = (*jenv)->FindClass(jenv, PRODUCT_IO_CLASS);
	if (product_io_class == NULL) {
		return NULL;
	}
	BEAM_TRACE("beam_read_product 2\n");
    method = (*jenv)->GetStaticMethodID(jenv, product_io_class, "readProduct", "(L" STRING_CLASS ";)L" PRODUCT_CLASS ";");
	if (method == NULL) {
		return NULL;
	}
	BEAM_TRACE("beam_read_product 3\n");

	file_path_s = (*jenv)->NewStringUTF(jenv, file_path);
    product = (*jenv)->CallStaticObjectMethod(jenv, product_io_class, method, file_path_s);

	BEAM_TRACE("beam_read_product 4\n");
	return product != NULL ? (*jenv)->NewGlobalRef(jenv, product) : NULL;
}

Product beam_create_product(const char* op_name, const char** parameters, Product source_product, ...)
{
    RETURN_IF_NO_JVM(NULL);
	/* TODO - implement */
	return NULL;
}

void beam_release_object(jobject* object)
{
	RETURN_IF_NO_JVM_V();

	if (*object != NULL) {
		(*jenv)->DeleteGlobalRef(jenv, *object);
		*object = NULL;
	}
}

char* product_get_name(Product product)
{
	jclass product_class;
	jmethodID method;
	jobject str;
	jsize len;
	char* product_name;
	const char* chars;

	RETURN_IF_NO_JVM(NULL);

    product_class = (*jenv)->FindClass(jenv, PRODUCT_CLASS);
	if (product_class == NULL) {
		return NULL;
	}
    method = (*jenv)->GetMethodID(jenv, product_class, "getName", "()L" STRING_CLASS ";");
	if (method == NULL) {
		return NULL;
	}

	str = (*jenv)->CallObjectMethod(jenv, product, method);
	if (str == NULL) {
		return NULL;
	}

	len = (*jenv)->GetStringUTFLength(jenv, str);
	chars = (*jenv)->GetStringUTFChars(jenv, str, 0);

	product_name = (char*) malloc((len + 1) * sizeof (char));
	strcpy(product_name, chars);

	(*jenv)->ReleaseStringUTFChars(jenv, str, chars);

	return product_name;
}


int product_get_num_bands(Product product)
{
	jclass product_class;
	jmethodID method;
	jint num_bands;

	RETURN_IF_NO_JVM(-1);

    product_class = (*jenv)->FindClass(jenv, PRODUCT_CLASS);
	if (product_class == NULL) {
		return -1;
	}
    method = (*jenv)->GetMethodID(jenv, product_class, "getNumBands", "()I");
	if (method == NULL) {
		return -1;
	}
    num_bands = (*jenv)->CallIntMethod(jenv, product, method);

	return num_bands;
}

char** product_get_band_names(Product product)
{
	jclass product_class;
	jmethodID method;
	jarray str_array;
	jsize array_len;
	char** band_names;
	jsize i;

	RETURN_IF_NO_JVM(NULL);

    product_class = (*jenv)->FindClass(jenv, PRODUCT_CLASS);
	if (product_class == NULL) {
		return NULL;
	}
    method = (*jenv)->GetMethodID(jenv, product_class, "getBandNames", "()[L" STRING_CLASS ";");
	if (method == NULL) {
		return NULL;
	}
    str_array = (*jenv)->CallObjectMethod(jenv, product, method);
	if (str_array == NULL) {
		return NULL;
	}
	array_len = (*jenv)->GetArrayLength(jenv, str_array);

	band_names = (char**) malloc(array_len * sizeof (char*));
	for (i = 0; i < array_len; i++) {
	    jstring str = (*jenv)->GetObjectArrayElement(jenv, str_array, i);
		jsize len = (*jenv)->GetStringUTFLength(jenv, str);
		const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);

	    char* band_name = (char*) malloc((len + 1) * sizeof (char));
	    strcpy(band_name, chars);

		(*jenv)->ReleaseStringUTFChars(jenv, str, chars);

		band_names[i] = band_name;
	}

	return band_names;
}


/* Java VM functions */


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
		fprintf(stderr, "beam-extapi error: JNI_CreateJavaVM failed with exit code %d\n", res);
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
		fprintf(stderr, "beam-extapi error: DestroyJavaVM failed with exit code %d\n", res);
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

/*
 * The VM calls JNI_OnLoad when the native library is loaded (for example, through System.loadLibrary). 
 * JNI_OnLoad must return the JNI version needed by the native library.
 *
 * In order to use any of the new JNI functions, a native library must export a JNI_OnLoad function
 * that returns JNI_VERSION_1_2. If the native library does not export a JNI_OnLoad function, the VM 
 * assumes that the library only requires JNI version JNI_VERSION_1_1. If the VM does not recognize
 * the version number returned by JNI_OnLoad, the native library cannot be loaded.
 *
 * The VM calls JNI_OnUnload when the class loader containing the native library is garbage collected.
 * This function can be used to perform cleanup operations. Because this function is called in an 
 * unknown context (such as from a finalizer), the programmer should be conservative on using Java VM 
 * services, and refrain from arbitrary Java call-backs.
 * 
 * Note that JNI_OnLoad and JNI_OnUnload are two functions optionally supplied by JNI libraries, 
 * not exported from the VM.
 *
 * From: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/invocation.html#JNI_OnLoad 
 */


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    jvm = vm;
	BEAM_TRACE("beam-extapi: JNI_OnLoad() called\n");
	return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
	jvm = NULL;
	jenv = NULL;
	BEAM_TRACE("beam-extapi: JNI_OnUnload() called\n");
}

JNIEXPORT jboolean JNICALL Java_org_esa_beam_extapi_CApi_init(JNIEnv *env, jobject extApi)
{
    jenv = env;
	BEAM_TRACE("beam-extapi: Java_org_esa_beam_extapi_CApi_init() called\n");
	return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_org_esa_beam_extapi_CApi_destroy(JNIEnv *env, jobject extApi)
{
	BEAM_TRACE("beam-extapi: Java_org_esa_beam_extapi_CApi_destroy() called\n");
}


/*
 * Simple logging.
 */

void beam_log(int level, const char* format, ...) 
{ 
	if (beam_log_level != 0 && level <= beam_log_level) {
		va_list va;
        va_start(va, format);
		vprintf(format, va);
		va_end(va);
	}
}

