#ifdef WIN32
    #include <windows.h>
#else
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <dirent.h>
#endif
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#include "beam_util.h"


size_t Util_appendString(char** result, const char* s)
{
    size_t slen, blen;    
    char* buf;
        
    buf = *result;
    slen = strlen(s);

    if (buf == NULL) {
        buf = (char*) malloc(slen + 1);
        if (buf == NULL) {
            return -1;
        }
        strcpy(buf, s);
        *result = buf;
        return slen;
    } else {
        blen = strlen(buf);
        buf = (char*) malloc(blen + slen + 1);
        if (buf == NULL) {
            return -1;
        }
        strcpy(buf, *result);
        strcpy(buf + blen, s);
        free(*result);
        *result = buf;
        return blen + slen;
    }                
}

void Util_listDir(const char* parent_dir, Util_handleDirEntry handler, void* user_data)
{
#ifdef WIN32
    HANDLE dir;
    WIN32_FIND_DATA file_data;
    PVOID oldRedirectionValue = NULL;
    char* pattern;
    char file_name[MAX_PATH + 1];

    // Disables file system redirection for the calling thread. File system redirection is enabled by default.
    // MS says, this should be done for 32bit apps on 64bit systems.
    if (!Wow64DisableWow64FsRedirection(&oldRedirectionValue)) {
        oldRedirectionValue = NULL;
    }

    pattern = NULL;
    Util_appendString(&pattern, parent_dir);
    Util_appendString(&pattern, "\\*");

    // fprintf(stdout, "debug: collecting from %s\n", pattern);

    if ((dir = FindFirstFile(pattern, &file_data)) != INVALID_HANDLE_VALUE) {
        do {
            size_t n = strlen(file_data.cFileName);
            strncpy(file_name, file_data.cFileName, min(n, MAX_PATH));
            file_name[min(n, MAX_PATH)] = 0;
            // fprintf(stdout, "debug: filename %s\n", file_name);
            handler(parent_dir, file_name, (file_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != 0, user_data);
        } while (FindNextFile(dir, &file_data));
        FindClose(dir);
    } else {
        DWORD err_code = GetLastError();
    }

    free(pattern);

    if (oldRedirectionValue != NULL) {
        Wow64RevertWow64FsRedirection(oldRedirectionValue);
    }

#else
    DIR* dir;
    struct dirent* ent;
    struct stat st;

    dir = opendir(parent_dir);
    if (dir != NULL) {
        while ((ent = readdir(dir)) != NULL) {
            handler(parent_dir, ent->d_name, (st.st_mode & S_IFDIR) != 0, user_data);    
        }
    }

    closedir(dir);
#endif
}

// experimental code
#ifdef COLLECTIONS

Value NULL_VALUE = {0, {0}};

Value Value_getBoolean(char val)
{
    Value value;
    value.type = 'b';
    value.value.b = val;
    return value;
}

Value Value_getInteger(long val);
{
    Value value;
    value.type = 'i';
    value.value.i = val;
    return value;
}

Value Value_getFloat(double val);
{
    Value value;
    value.type = 'f';
    value.value.b = val;
    return value;
}

Value Value_getString(const char* val)
{
    Value value;
    value.type = 's';
    value.value.b = val;
    return value;
}

size_t Value_hashCodeFromBytes(const char* bytes, size_t n)
{
    size_t code = 0;
    size_t i = 0;
    for (i = 0; i < n; i++) {
       code = 31 * code + bytes[i];
    }
    return code;
}

size_t Value_hashCode(Value value)
{
    if (value.type == 'b') {
        return (size_t) value.value.b;
    } else if (value.type == 'i') {
        return (size_t) value.value.i;
    } else if (value.type == 'f') {
        char bytes[sizeof (double)];
        *((double*)bytes) = value.value.f;
        return Value_hashCodeFromBytes(bytes, sizeof (double));
    } else if (value.type == 's') {
        return Value_hashCodeFromBytes(value.value.s, strlen(value.value.s));
    }
    return 0;
}

size_t Value_equals(Value value1, Value value2)
{
    if (value1.type == 'b' && value2.type == 'b') {
        return value1.value.b == value2.value.b;
    } else if (value1.type == 'i' && value2.type == 'i') {
        return value1.value.i == value2.value.i;
    } else if (value1.type == 'f' && value2.type == 'f') {
        return value1.value.f == value2.value.f;
    } else if (value1.type == 's' && value2.type == 's') {
        return strcmp(value1.value.s, value2.value.s) == 0;
    }
    return value1.type == 0 && value2.type == 0; // = NULL_VALUE
}


List* List_newList(size_t capacity)
{
    List* list = (List*) malloc(sizeof (List));
    if (list == NULL)
        return NULL;
    list->size = 0;
    list->capacity = capacity;
    list->values = (Value*) calloc(capacity, sizeof (Value));
    if (list->values == NULL) {
        free(list);
        return NULL;
    }
    return list;
}

void List_deleteList(List* list)
{
    if (list != NULL) {
        free(list->values);
        free(list);
    }
}

Value List_get(List* list, size_t index)
{
    return list->values[index];
}

Value List_set(List* list, size_t index, Value value)
{
    Value old = list->values[index];
    list->values[index] = value;
    return old;
}

size_t List_add(List* list, Value value)
{
    if (list->capacity <= list->size + 1) {
        size_t newCapacity = 2 * list->capacity;
        Value* oldValues = list->values;
        Value* newValues = (Value*) calloc(newCapacity, sizeof (Value));
        if (newValues != NULL) {
            return -1;
        }
        memcpy(newValues, oldValues, list->size * sizeof (Value));
        list->capacity = newCapacity;
    }
    list->values[list->size] = value;
    list->size++;
    return list->size;
}


Map* Map_newMap(size_t capacity)
{
    Map* map = (Map*) calloc(1, sizeof (Map));
    if (map == NULL)
        return NULL;
    map->capacity = capacity;
    map->keys = (List**) calloc(capacity, sizeof (List*));
    if (map->keys == NULL) {
        free(map);
        return NULL;
    }
    map->values = (List**) calloc(capacity, sizeof (List*));
    if (map->values == NULL) {
        free(map->keys);
        free(map);
        return NULL;
    }
    return map;
}


void Map_deleteMap(Map* map)
{
    size_t i;
    for (i = 0; i < map->capacity; i++) {
        List* keys = map->keys[i];
        List* values = map->values[i];
        if (keys != NULL) {
            List_deleteList(keys);
        }
        if (values != NULL) {
            List_deleteList(values);
        }
    }
    free(map->keys);
    free(map->values);
    free(map);
}

Value Map_get(Map* map, Value key)
{
    size_t code = Value_hashCode(key);
    size_t index = code % map->capacity;
    List* keys = map->keys[index];
    List* values = map->values[index];
    size_t i;

    if (keys == NULL) {
        return NULL_VALUE;
    }

    for (i = 0; i < keys->size; i++) {
        Value key2 = List_get(keys, i);
        if (Value_equals(key, key2)) {
            return List_get(values, i);
        }
    }

    return NULL_VALUE;
}


Value Map_put(Map* map, Value key, Value value)
{
    size_t code = Value_hashCode(key);
    size_t index = code % map->capacity;
    List* keys = map->keys[index];
    List* values = map->values[index];
    size_t i;

    if (keys == NULL) {
        keys = List_newList(4);
        values = List_newList(4);
        map->keys[index] = keys;
        map->values[index] = values;
    }

    for (i = 0; i < keys->size; i++) {
        Value key2 = List_get(keys, i);
        if (Value_equals(key, key2)) {
            Value old = List_get(values, i);
            List_set(values, i, value);
            return old;
        }
    }

    List_add(keys, key);
    List_add(values, value);
    return NULL_VALUE;
}

#endif // COLLECTIONS
