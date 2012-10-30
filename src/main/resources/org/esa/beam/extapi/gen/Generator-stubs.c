char* beam_alloc_string(jstring str)
{
    int len = (*jenv)->GetStringUTFLength(jenv, str);
    const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);
    char* result = (char*) malloc((len + 1) * sizeof (char));
    if (result != NULL) {
        strcpy(result, chars);
    }
    (*jenv)->ReleaseStringUTFChars(jenv, str, chars);
    return result;
}

void beam_alloc_string_array(jarray str_array, char*** str_array_data, size_t* str_array_length)
    jarray str_array;
	jsize length;
	char** strings;
	jsize i;

	length = (*jenv)->GetArrayLength(jenv, str_array);

	strings = (char**) malloc(array_len * sizeof (char*));
	for (i = 0; i < length; i++) {
	    jstring str = (*jenv)->GetObjectArrayElement(jenv, str_array, i);
		jsize len = (*jenv)->GetStringUTFLength(jenv, str);
		const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);

	    char* band_name = (char*) malloc((len + 1) * sizeof (char));
	    strcpy(band_name, chars);

		(*jenv)->ReleaseStringUTFChars(jenv, str, chars);

		strings[i] = band_name;
	}

	*str_array_data = strings;
	*str_array_length = length;
}

void beam_free_string_array(char** str_array_data, size_t str_array_length)
	jsize i;
	for (i = 0; i < str_array_length; i++) {
        if (str_array_data[i] != NULL) {
		    free(str_array_data[i]);
		}
	}
	free(str_array_data);
}


jarray beam_allocate_string_array_1(char** str_array_data, size_t str_array_length)
{
    jarray str_array;
	jsize length;
	char** strings;
	jsize i;

	str_array = (*jenv)->NewObjectArray(jenv, str_array);

	length = (*jenv)->GetArrayLength(jenv, str_array);

	strings = (char**) malloc(array_len * sizeof (char*));
	for (i = 0; i < length; i++) {
	    jstring str = (*jenv)->GetObjectArrayElement(jenv, str_array, i);
		jsize len = (*jenv)->GetStringUTFLength(jenv, str);
		const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);

	    char* band_name = (char*) malloc((len + 1) * sizeof (char));
	    strcpy(band_name, chars);

		(*jenv)->ReleaseStringUTFChars(jenv, str, chars);

		strings[i] = band_name;
	}

	*strings_data = strings;
	*strings_length =  length;
}
