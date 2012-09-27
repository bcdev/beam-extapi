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

	RETURN_IF_NO_JVM(NULL);

    product_io_class = (*jenv)->FindClass(jenv, PRODUCT_IO_CLASS);
	if (product_io_class == NULL) {
		return NULL;
	}
    method = (*jenv)->GetStaticMethodID(jenv, product_io_class, "readProduct", "(L" STRING_CLASS ";)L" PRODUCT_CLASS ";");
	if (method == NULL) {
		return NULL;
	}

	file_path_s = (*jenv)->NewStringUTF(jenv, file_path);
    product = (*jenv)->CallStaticObjectMethod(jenv, product_io_class, method, file_path_s);

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
	printf("beam-extapi: JNI_OnLoad() called\n");
	return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
	jvm = NULL;
	jenv = NULL;
	printf("beam-extapi: JNI_OnUnload() called\n");
}

JNIEXPORT jboolean JNICALL Java_org_esa_beam_extapi_CApi_init(JNIEnv *env, jobject extApi)
{
    jenv = env;
	printf("beam-extapi: Java_org_esa_beam_extapi_CApi_init() called\n");
	return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_org_esa_beam_extapi_CApi_destroy(JNIEnv *env, jobject extApi)
{
	printf("beam-extapi: Java_org_esa_beam_extapi_CApi_destroy() called\n");
}

