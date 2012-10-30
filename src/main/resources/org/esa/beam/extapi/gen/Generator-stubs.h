typedef char byte;
typedef unsigned char boolean;
typedef long long dlong;

int beam_init_vm();
char* beam_alloc_string(jstring str);
char** beam_alloc_string_array(jarray str_array, size_t* str_array_length);
void beam_free_string_array(char** str_array_data, size_t str_array_length);
jobjectArray beam_new_jstring_array(const char** str_array_data, size_t str_array_length);