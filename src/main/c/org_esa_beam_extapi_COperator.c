#include "gen/beam_capi.h"
#include "gen/org_esa_beam_extapi_COperator.h"

/*
 * Class:     org_esa_beam_extapi_COperator
 * Method:    computeTileNative
 * Signature: (IIIIIILjava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_org_esa_beam_extapi_COperator_computeTileNative
  (JNIEnv *env, jobject op, jint opId, jint band_index, jint x, jint y, jint w, jint h, jobject tile_data)
{
}

/*
 * Class:     org_esa_beam_extapi_COperator
 * Method:    computeTileStackNative
 * Signature: (I[IIIII[Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_org_esa_beam_extapi_COperator_computeTileStackNative
  (JNIEnv *env, jobject op, jint opId, jintArray band_indexes, jint x, jint y, jint w, jint h, jobjectArray tile_data)
{
}

/*
 * Class:     org_esa_beam_extapi_COperator
 * Method:    initializeNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_esa_beam_extapi_COperator_initializeNative
  (JNIEnv *env, jobject op, jint opId)
{
}

/*
 * Class:     org_esa_beam_extapi_COperator
 * Method:    disposeNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_esa_beam_extapi_COperator_disposeNative
  (JNIEnv *env, jobject op, jint opId)
{
}

