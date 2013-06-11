
/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 */

boolean beam_is_jvm_created();
boolean beam_create_jvm(const char* option_strings[], int option_count);
boolean beam_create_jvm_with_defaults();
boolean beam_destroy_jvm();

void beam_release_string(char* chars);
void beam_release_string_array(char** array_elems, int array_length);
void beam_release_object_array(void** array_elems, int array_length);
void beam_release_primitive_array(void* array_elems, int array_length);

String String_newString(const char* chars);
void Object_delete(Object object);

