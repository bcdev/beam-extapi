
/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 */

boolean beam_is_jvm_created();
boolean beam_create_jvm(const char* option_strings[], int option_count);
boolean beam_create_jvm_with_defaults();
boolean beam_destroy_jvm();

void beam_release_jobject(void** object);

String String_newString(const char* chars);
void beam_release_string_array(char** array_elems, int array_length);
