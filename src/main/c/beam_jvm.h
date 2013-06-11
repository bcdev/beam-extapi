// todo - make use of this file, currently we generate this code from resource templates
#ifndef BEAM_JVM_H
#define BEAM_JVM_H

#ifdef __cplusplus
extern "C" {
#endif

#include "jni.h"

/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 */

boolean beam_isJvmCreated();
boolean beam_createJvm(const char* option_strings[], int option_count);
boolean beam_createJvmWithDefaults();
boolean beam_destroyJvm();

void Object_delete(void* object);

String String_newString(const char* chars);
void beam_deleteCString(char* chars);
void beam_deleteCStringArray(char** array_elems, int array_length);
void beam_deleteCObjectArray(void** array_elems, int array_length);
void beam_deleteCPrimitiveArray(void* array_elems, int array_length);


#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAMPY_JVM_H */