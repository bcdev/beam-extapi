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
