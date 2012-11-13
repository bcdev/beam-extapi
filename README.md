beam-extapi
===========

Links
-----

* http://docs.oracle.com/javase/7/docs/technotes/guides/jni/
* http://docs.oracle.com/javase/1.4.2/docs/guide/jni/spec/functions.html


C-Code Generation Considerations
--------------------------------

* Take care of String if used as parameter (--> const char*) or return value (by reference: char*, int max_size). (done first option)
* Don't forget to increase global reference count when returning objects from Java VM.  (done)
* If we start from a limited number of API classes, we need to remove methods that have arguments for which no factory exists at all.
* Need to find out which Java SE classes are used in parameter lists, but neither cannot retrieved nor instanciated.
  --> ApiInfo already known about this (ApiInfo.getUsedNonApiClasses()) , but not handled yet
* How to treat method (array) parameters that
** are input, output or both  (see Band.read/writePixels() methods)
  --> e.g. new parameter annotations: @In, @Out, @InOut, or simply @Const or @ReadOnly
      or may need to define special translation rules for these methods that have this issue
** are used as method return values (see Band.readPixels() methods)
  --> e.g. new annotation @Return, or @Reuse or @ReturnedIfNotNull
* How to deal with object parameters in which a C string is passed (generator doesn't know, that a string object needs to be created)
   --> String_newString()
* How to translate constants defined in Java classes?
   --> constants are already collected (--> ApiInfo)
* How to deal with same function names originating from overloaded Java methods   (see Band.readPixels() methods)
   --> renaming / ignoring overloads looked up from configuration file
       if not in configuration file, numbering these
* How to treat String parameters?
  --> Now: String s -> const char* s
* How to treat String return values?
  --> Now: A char* is returned, clients must free it
      Alternative: return String object and offer
         int beam_strlen(String s);
         void beam_strcpy(String s, char* buf, int max_len);
* How to treat generics?
  --> Nothing done so far, maybe <T> --> <Object> and <T extends C> --> C
* How to treat (primitive, string & object) arrays parameters?
  --> IN:   float[] a --> const float* aElems, int aLength
  --> OUT:  float[] a --> float* aElems, int aLength
* How to treat (primitive, string & object) return values?
  -->  float[] m() --> float* m(int* resultArrayLength)
* How to treat object collections lists, sets, maps?
  --> Not addressed so far
* How to treat enums? (e.g. HistogramMatching)
  --> Not addressed so far
* How to treat public fields? (e.g. GeoPos)
  --> Not addressed so far
* How to treat Java SE classes: File, Date, Point, etc?
  --> Not addressed so far
* How to make sure that JNI global refs are decreased/freed?
  --> Not addressed so far
* Must treat thrown exceptions!
  --> Not addressed so far
* Must throw OutOfMemoryError if malloc fails!
  --> Not addressed so far
* How to deal with multi-threaded C/Python programs (specifically, what about 'env' pointer in JNI, it is a per-thread environment)
  --> Not addressed so far
* Care: Check ALWAYS for possible null return values (many JNI functions may return null)
  --> Not addressed so far. Must throw Java exceptions.

To-do List
----------

* Use BEAM_HOME env and auto-configure JVM parameters (use Util_listDir)
* Separate C modules: beam_capi --> beam_capi_gen (generated code) + beam_capi_util (constant code)


Design Considerations
---------------------

Use JNI types in C-interface?
    Yes, because BEAM API is Java and as such it is much less verbose and more concise to reuse JNI types, and is also less work
    No, because the C-API shall be independent of its implementation or origin

Duplicate Java API (or parts) 1:1?  (e.g. any method in Java gets its C counterpart)
    Yes, because the Java API docs can be reused for C API.
    No, because a more concise C-API can be generated. And no, because changes in the Java API need to be reflected in the C API
     which will introduce a lot of work (but maybe we can generate code).

Shall C API functions return string buffers that users have to release later?
    Yes, otherwise the signature of Java counterparts is will be different, because by-reference arguments passing is required then.
         E.g. instead of
              char* name = get_name(obj);
              ...
              free(name);
         we have
              char name[81];
              get_name(obj, name, 80);
    No, because it is obvious that strings need to be freed on the users side.

Shall the API allow for modification of single structures elements that are passed as arguments by-reference?


