/*
 * Functions that must be used after some BEAM C-API library calls in order to
 * release allocated memory.
 */
void beam_deleteCString(char* chars);
void beam_deleteCStringArray(char** array_elems, int array_length);
void beam_deleteCObjectArray(void** array_elems, int array_length);
void beam_deleteCPrimitiveArray(void* array_elems, int array_length);

