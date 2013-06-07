#ifndef BEAM_UTIL_H
#define BEAM_UTIL_H

#ifdef __cplusplus
extern "C" {
#endif

size_t Util_appendString(char** result, const char* s);
typedef void (*Util_handleDirEntry)(const char* parent_dir, const char* file_name, int is_dir, void* user_data);
void Util_listDir(const char* parent_dir, Util_handleDirEntry handler, void* user_data);


//#define COLLECTIONS

#ifdef COLLECTIONS


typedef struct SValue {
    /** 'b', 'i', 'f' or 's' */
    char type;
    union {
        /** boolean */
        char b;
        /** integer (32bit min) */
        long i;
        /** float (64bit min) */
        double f; // float
        /** zero-terminated string */
        const char* s;
    } value;
} Value;

typedef struct SList {
    size_t size;
    size_t capacity;
    Value* values;
} List;

typedef struct SMap {
    size_t capacity;
    List** keys;
    List** values;
} Map;

extern Value NULL_VALUE;

Value Value_getBoolean(char val);
Value Value_getInteger(long val);
Value Value_getFloat(double val);
Value Value_getString(const char* val);
size_t Value_hashCode(Value value);
size_t Value_equals(Value value1, Value value2);
size_t Value_isNull(Value value);

List* List_newList(size_t capacity);
void List_deleteList(List* list);
Value List_get(List* list, size_t index);
size_t List_add(List* list, Value value);

Map* Map_newMap(size_t capacity);
void Map_deleteMap(Map* map);
Value Map_get(Map* map, Value key);
Value Map_put(Map* map, Value key, Value value);

#endif // COLLECTIONS



#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif /* !BEAM_UTIL_H */