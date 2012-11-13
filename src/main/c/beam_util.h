int Util_append_str(char** result, const char* s);
typedef void (*Util_handleDirEntry)(const char* parent_dir, const char* file_name, int is_dir);
void Util_listDir(const char* parent_dir, Util_handleDirEntry handler);
