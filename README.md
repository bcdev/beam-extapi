beam-extapi
===========

BEAM Python API
---------------

### How to build:
* Install JDK 1.6 32-bit. Set `JDK32_HOME`.
* Install Python 3.2 32bit. Set `PYTHON32_HOME`.
* Checkout sources from GitHub (https://github.com/bcdev/beam-extapi). Checkout directory is `%BEAM_EXTAPI%` (Windows) or `$BEAM_EXTAPI` (Unix).
* On Windows, install Visual C++ 2010 Express (http://www.microsoft.com/visualstudio/deu/downloads#d-2010-express)
* On Windows, execute `%BEAM_EXTAPI%/makewin32.bat`
* On Unix, execute `$BEAM_EXTAPI/makeunix.sh`  (not available yet)

### How to install:
* Install BEAM 4.10.3. Set `BEAM_HOME`.
* On Windows, copy `beampy.pyd` and `beam_capi.dll` from `%BEAM_EXTAPI%/target/win32` to `%PYTHON32_HOME%/DLLs`
* On Unix, copy `beampy.pyd` and `beam_capi.sh` from `$BEAM_EXTAPI/target/unix32` to `$PYTHON32_HOME/DLLs` (not available yet)

### How to run:
* Start Python, then type

    >>> import beampy
    >>> dir(beampy)
    >>> help(beampy)



Developer Links
---------------
beam-extapi code repository  

* Project on Github: https://github.com/bcdev/beam-extapi
* Java Native Interface (JNI) website:  http://docs.oracle.com/javase/7/docs/technotes/guides/jni/
* JNI data types:	http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/types.html
* JNI function reference: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html
* Python/C API reference:	http://docs.python.org/py3k/c-api/index.html
* Python/C API tutorial:	http://docs.python.org/py3k/extending/index.html
* Python/C argument parsing:  http://docs.python.org/py3k/c-api/arg.html


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

* Done: Use BEAM_HOME env and auto-configure JVM parameters (use Util_listDir)
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


Frequently seen problems
========================

Windows VC++ using "Release" configuration:
Fehler	11	error LNK1104: Datei "python32_d.lib" kann nicht geöffnet werden.	C:\Users\Norman\JavaProjects\beam-extapi\msvc\beampy\LINK	beampy

--> No solution; Google does not help. Must use Release configuration

