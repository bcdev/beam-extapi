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

ObjectId beamc_read_product(const char* file_path)
{
	jclass product_io;
	jmethodID method;
	jobject product;
	jstring file_path_s;

	if (!is_jvm_created()) {
		return NULL;
	}

    product_io = (*env)->FindClass(env, "org.esa.beam.framework.dataio.ProductIO");
    method = (*env)->GetStaticMethodID(env, product_io, "readProduct", "(Ljava/lang/String;)Lorg/esa/beam/framework/datamodel/Product");

	file_path_s = (*env)->NewStringUTF(env, file_path);
    product = (*env)->CallStaticObjectMethod(env, product_io, method, file_path_s);
	(*env)->ReleaseStringUTFChars(env, file_path_s, file_path);

	product = (*env)->NewGlobalRef(env, product);

	return (ObjectId) product;
}

void beamc_release_object(ObjectId object_id)
{
	if (object_id != 0) {
		(*env)->DeleteGlobalRef(env, (jobject) object_id);
	}
}

void beamc_release_string(StringId string_id)
{
	if (string_id != NULL) {
		(*env)->ReleaseStringUTFChars(env, string_id->jstr, string_id->chars);
		free(string_id);
	}
}

const char* beamc_get_product_name(ObjectId product_id)
{
	jclass product_class;
	jmethodID method;
	jobject jproduct;
	jobject jproduct_name;
	const char* product_name;

	if (!is_jvm_created()) {
		return NULL;
	}

	jproduct = (jobject) product_id;

    product_class = (*env)->FindClass(env, "org.esa.beam.framework.datamodel.Product");
    method = (*env)->GetMethodID(env, product_class, "getName", "(V)Ljava/lang/String");
    jproduct_name = (*env)->CallObjectMethod(env, jproduct, method);

	product_name = (*env)->GetStringUTFChars(env, jproduct_name, 0);

	(*env)->ReleaseStringUTFChars(env, jproduct_name, product_name);

	return product_name;
}


int beamc_get_product_num_bands(ObjectId product)
{
	jclass product_io;
	jmethodID method;
	jobject product;
	jstring file_path_s;

	if (!is_jvm_created()) {
		return NULL;
	}

    product_io = (*env)->FindClass(env, "org.esa.beam.framework.dataio.ProductIO");
    method = (*env)->GetStaticMethodID(env, product_io, "readProduct", "(Ljava/lang/String;)Lorg/esa/beam/framework/datamodel/Product");

	file_path_s = (*env)->NewStringUTF(env, file_path);
    product = (*env)->CallStaticObjectMethod(env, product_io, method, file_path_s);
	(*env)->ReleaseStringUTFChars(env, file_path_s, file_path);

	return (JavaObject) product;
}

ObjectId beamc_create_product(const char* op_name, const char** parameters, ObjectId source_product, ...)
{
	/* TODO - implement */
	return 0;
}

