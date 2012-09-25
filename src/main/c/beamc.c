#include "beamc.h"

static JavaVM* jvm = NULL; 
static JNIEnv* env = NULL; 

static jboolean is_jvm_created() 
{
	return jvm != NULL && env != NULL;
}

static jint create_jvm() 
{
    JavaVMInitArgs vm_args; 
    JavaVMOption options[4];
    char* beam_home;

	if (is_jvm_created()) {
		return 0;
	}

	/* use BEAM settings */
	beam_home = getenv("BEAM_HOME");
    options[0].optionString = "-Djava.compiler=NONE";           /* disable JIT */
    options[1].optionString = "-Djava.class.path=c:\\myclasses"; /* user classes */
    options[2].optionString = "-Djava.library.path=c:\\mylibs";  /* set native library path */
    options[3].optionString = "-verbose:jni";                   /* print JNI-related messages */

    vm_args.version = JNI_VERSION_1_6;
    vm_args.options = options;
    vm_args.nOptions = 4;
    vm_args.ignoreUnrecognized = 0;

	return JNI_CreateJavaVM(&jvm, (void**) &env, &vm_args);
}

static jint destroy_jvm() 
{
	jint res;

    if (!is_jvm_created()) {
		return 0;
	}
	
	res = (*jvm)->DestroyJavaVM(jvm);
	if (res == 0) {
	    jvm = NULL;
	    env = NULL;
        return 1;
	}

	return res;
}

Product beam_read_product(const char* file_path)
{
	jclass product_io_class;
	jmethodID method;
	jobject product;
	jstring file_path_s;

	if (!is_jvm_created()) {
		return NULL;
	}

    product_io_class = (*env)->FindClass(env, "org.esa.beam.framework.dataio.ProductIO");
    method = (*env)->GetStaticMethodID(env, product_io_class, "readProduct", "(Ljava/lang/String;)Lorg/esa/beam/framework/datamodel/Product");

	file_path_s = (*env)->NewStringUTF(env, file_path);
    product = (*env)->CallStaticObjectMethod(env, product_io_class, method, file_path_s);
	(*env)->ReleaseStringUTFChars(env, file_path_s, file_path);

	return (*env)->NewGlobalRef(env, product);
}

Product beam_create_product(const char* op_name, const char** parameters, Product source_product, ...)
{
	/* TODO - implement */
	return NULL;
}

void beam_release_object(jobject* object)
{
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

	if (!is_jvm_created()) {
		return NULL;
	}

    product_class = (*env)->FindClass(env, "org.esa.beam.framework.datamodel.Product");
    method = (*env)->GetMethodID(env, product_class, "getName", "(V)Ljava/lang/String");
    str = (*env)->CallObjectMethod(env, product, method);

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

	if (!is_jvm_created()) {
		return -1;
	}

    product_class = (*env)->FindClass(env, "org.esa.beam.framework.datamodel.Product");
    method = (*env)->GetMethodID(env, product_class, "getNumBands", "(V)I");
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

	if (!is_jvm_created()) {
		return NULL;
	}

    product_class = (*env)->FindClass(env, "org.esa.beam.framework.datamodel.Product");
    method = (*env)->GetMethodID(env, product_class, "getBandNames", "(V)[Ljava/lang/String");
    str_array = (*env)->CallObjectMethod(env, product, method);

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

