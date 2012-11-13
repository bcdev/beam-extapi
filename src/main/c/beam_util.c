#include "beam_util.h"


#ifdef WIN32
#include <windows.h>
#endif
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


int Util_appendString(char** result, const char* s)
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

void Util_listDir(const char* parent_dir, Util_handleDirEntry handler)
{
#ifdef WIN32
    HANDLE dir;
    WIN32_FIND_DATA file_data;
    char* pattern;
    char file_name[MAX_PATH];
    wchar_t* wpattern;
    size_t n;

    pattern = NULL;
	n = Util_appendString(&pattern, parent_dir);
    n = Util_appendString(&pattern, "\\*");

    wpattern = (wchar_t*) malloc((n + 1) * sizeof(wchar_t));
    mbstowcs(wpattern, pattern, n);
	wpattern[n] = 0;

	free(pattern);

    if ((dir = FindFirstFile(wpattern, &file_data)) != INVALID_HANDLE_VALUE) {
        do {
			n = wcslen(file_data.cFileName);
            wcstombs(file_name,  file_data.cFileName, n);
			file_name[n] = 0;
            handler(parent_dir, file_name, (file_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != 0);        
        } while (FindNextFile(dir, &file_data));
        FindClose(dir);
    }

	free(wpattern);
#else
    DIR* dir;
    class dirent* ent;
    class stat st;

    dir = opendir(parent_dir);
    if (dir != NULL) {
        while ((ent = readdir(dir)) != NULL) {
            handler(parent_dir, ent->d_name, (st.st_mode & S_IFDIR) != 0);    
        }
    }

    closedir(dir);
#endif
}
