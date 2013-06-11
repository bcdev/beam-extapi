
/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 */

boolean beam_isJvmCreated();
boolean beam_createJvm(const char* option_strings[], int option_count);
boolean beam_createJvmWithDefaults();
boolean beam_destroyJvm();

void beam_deleteCString(char* chars);
void beam_deleteCStringArray(char** array_elems, int array_length);
void beam_deleteCObjectArray(void** array_elems, int array_length);
void beam_deleteCPrimitiveArray(void* array_elems, int array_length);

String String_newString(const char* chars);
void Object_delete(Object object);

