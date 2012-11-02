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

char** beam_alloc_string_array(jarray str_array, size_t* str_array_length)
    char** str_array_data;
    jarray str_array;
	jsize length;
	char** strings;
	jsize i;

	length = (*jenv)->GetArrayLength(jenv, str_array);

	str_array_data = (char**) malloc(array_len * sizeof (char*));
	for (i = 0; i < length; i++) {
	    jstring str = (*jenv)->GetObjectArrayElement(jenv, str_array, i);
		jsize len = (*jenv)->GetStringUTFLength(jenv, str);
		const char* chars = (*jenv)->GetStringUTFChars(jenv, str, 0);

	    char* elems = (char*) malloc((len + 1) * sizeof (char));
	    strcpy(elems, chars);

		(*jenv)->ReleaseStringUTFChars(jenv, str, chars);

		str_array_data[i] = elems;
	}

	if (str_array_length != NULL) {
	    *str_array_length = length;
	}

	return str_array_data;
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


jobjectArray beam_new_jstring_array(const char** str_array_data, size_t str_array_length)
{
    jobjectArray str_array;
	jstring str;
	jsize i;

	str_array = (*jenv)->NewObjectArray(jenv, str_array_length, classString, NULL);
	for (i = 0; i < str_array_length; i++) {
        str = (*jenv)->NewStringUTF(jenv, str_array_data[i]);
		(*jenv)->SetObjectArrayElement(jenv, str_array, i, str);
	}

	// todo: check if we must return (*jenv)->NewGlobalRef(jenv, str_array);
	return str_array;
}

jobjectArray beam_new_jobject_array(jclass comp_class, const void** obj_array_data, size_t obj_array_length)
{
    jobjectArray obj_array;
	jsize i;

    obj_array = (*jenv)->NewObjectArray(jenv, bandsLength, comp_class, NULL);
    for (i = 0; i < bandsLength; i++) {
        (*jenv)->SetObjectArrayElement(jenv, obj_array, i, obj_array_data[i]);
    }

 	// todo: check if we must return (*jenv)->NewGlobalRef(jenv, obj_array);
    return obj_array;
}
