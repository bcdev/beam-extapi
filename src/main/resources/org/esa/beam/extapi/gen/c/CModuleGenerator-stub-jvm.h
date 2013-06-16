/*
 * Java VM functions that must be used if this module is used in stand-alone
 * mode (= not loaded as shared library by a Java VM).
 */
boolean beam_isJvmCreated();
boolean beam_createJvm(const char* option_strings[], int option_count);
boolean beam_createJvmWithDefaults();
boolean beam_destroyJvm();

// todo - the following functions actually belong in another module because they expect String and Object typedefs to be present

String String_newString(const char* chars);
void Object_delete(Object object);
