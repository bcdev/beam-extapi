#ifndef BEAM_JVM_H
#define BEAM_JVM_H

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>

/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 * All return 1 on success, 0 on failure.
 */
/**@{*/
jboolean beam_isJvmCreated(void);
jboolean beam_createJvm(const char* option_strings[], int option_count);
jboolean beam_createJvmWithDefaults(void);
jboolean beam_destroyJvm(void);
/**@}*/

jclass beam_findJvmClass(const char* classResourceName);

extern JavaVM* jvm;
extern JNIEnv* jenv;

#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAM_JVM_H */