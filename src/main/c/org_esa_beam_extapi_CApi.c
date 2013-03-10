#include "gen/beam_capi.h"
#include "gen/org_esa_beam_extapi_CApi.h"

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
