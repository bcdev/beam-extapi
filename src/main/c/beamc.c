#include "beamc.h"

#define STRING_CLASS     "java/lang/String"
#define PRODUCT_IO_CLASS "org/esa/beam/framework/dataio/ProductIO"
#define PRODUCT_CLASS    "org/esa/beam/framework/datamodel/Product"

static JavaVM* jvm = NULL; 
static JNIEnv* env = NULL; 

#define RETURN_IF_NO_JVM_V() if (jvm == NULL) {return;}
#define RETURN_IF_NO_JVM(A) if (jvm == NULL) {return (A);}

Product beam_read_product(const char* file_path)
{
	jclass product_io_class;
	jmethodID method;
	jobject product;
	jstring file_path_s;

	RETURN_IF_NO_JVM(NULL);

    product_io_class = (*env)->FindClass(env, PRODUCT_IO_CLASS);
	if (product_io_class == NULL) {
		return NULL;
	}
    method = (*env)->GetStaticMethodID(env, product_io_class, "readProduct", "(L" STRING_CLASS ";)L" PRODUCT_CLASS ";");
	if (method == NULL) {
		return NULL;
	}

	file_path_s = (*env)->NewStringUTF(env, file_path);
    product = (*env)->CallStaticObjectMethod(env, product_io_class, method, file_path_s);

	return product != NULL ? (*env)->NewGlobalRef(env, product) : NULL;
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
		(*env)->DeleteGlobalRef(env, *object);
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

    product_class = (*env)->FindClass(env, PRODUCT_CLASS);
	if (product_class == NULL) {
		return NULL;
	}
    method = (*env)->GetMethodID(env, product_class, "getName", "()L" STRING_CLASS ";");
	if (method == NULL) {
		return NULL;
	}

	str = (*env)->CallObjectMethod(env, product, method);
	if (str == NULL) {
		return NULL;
	}

	len = (*env)->GetStringUTFLength(env, str);
	chars = (*env)->GetStringUTFChars(env, str, 0);

	product_name = (char*) malloc((len + 1) * sizeof (char));
	strcpy(product_name, chars);

	(*env)->ReleaseStringUTFChars(env, str, chars);

	return product_name;
}


int product_get_num_bands(Product product)
{
	jclass product_class;
	jmethodID method;
	jint num_bands;

	RETURN_IF_NO_JVM(-1);

    product_class = (*env)->FindClass(env, PRODUCT_CLASS);
	if (product_class == NULL) {
		return -1;
	}
    method = (*env)->GetMethodID(env, product_class, "getNumBands", "()I");
	if (method == NULL) {
		return -1;
	}
    num_bands = (*env)->CallIntMethod(env, product, method);

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

    product_class = (*env)->FindClass(env, PRODUCT_CLASS);
	if (product_class == NULL) {
		return NULL;
	}
    method = (*env)->GetMethodID(env, product_class, "getBandNames", "()[L" STRING_CLASS ";");
	if (method == NULL) {
		return NULL;
	}
    str_array = (*env)->CallObjectMethod(env, product, method);
	if (str_array == NULL) {
		return NULL;
	}
	array_len = (*env)->GetArrayLength(env, str_array);

	band_names = (char**) malloc(array_len * sizeof (char*));
	for (i = 0; i < array_len; i++) {
	    jstring str = (*env)->GetObjectArrayElement(env, str_array, i);
		jsize len = (*env)->GetStringUTFLength(env, str);
		const char* chars = (*env)->GetStringUTFChars(env, str, 0);

	    char* band_name = (char*) malloc((len + 1) * sizeof (char));
	    strcpy(band_name, chars);

		(*env)->ReleaseStringUTFChars(env, str, chars);

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
	res = JNI_CreateJavaVM(&jvm, (void**) &env, &vm_args);

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
	env = NULL;
    return JNI_TRUE;
}

