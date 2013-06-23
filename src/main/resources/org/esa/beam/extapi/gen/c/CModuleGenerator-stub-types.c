

String String_newString(const char* chars)
{
    jstring str = (*jenv)->NewStringUTF(jenv, chars);
    return (*jenv)->NewGlobalRef(jenv, str);
}

void Object_delete(Object object)
{
    if (object != NULL) {
        (*jenv)->DeleteGlobalRef(jenv, object);
    }
}

