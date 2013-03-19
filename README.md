beam-extapi
===========

BEAM Python API
---------------

### How to build:

* Install JDK 1.6 32-bit. Set `JDK32_HOME`.
* Checkout sources from GitHub (https://github.com/bcdev/beam-extapi). In the following, the checkout directory is named `BEAM_EXTAPI`.
* On Windows,
  1. Install Python 3 32-bit. Set `PYTHON32_HOME`.
  2. install Visual C++ 2012 Express (http://www.microsoft.com/visualstudio/eng/products/visual-studio-express-products). Set `MSVC_HOME` (must point to `VC` inside Visual Studio's installation directory).
  3. execute `%BEAM_EXTAPI%/mkwinall.bat`
* On Unix,
  1. install python development extension (sudo apt-get install python3-dev)
  2. Set 'PYTHON32_INLUDE' to location of "Python.h", installed with the python-dev
  3. execute 'cmake .' in project root directory, execute 'cmake -DCMAKE_BUILD_TYPE:STRING=Debug ." in project root directory to build a debug target
  4. execute 'make' in project root directory

### How to install:

* On Windows,
  1. Install BEAM 4.10.3 32 bit. Set `BEAM_HOME`.
  2. copy `beampy.pyd` and `beam_capi.dll` from `%BEAM_EXTAPI%/target/win32` to `%PYTHON32_HOME%/DLLs`
  3. Set `PATH=%PATH%;%BEAM_HOME%/jre/bin/client` (todo: avoid this step)
* On Unix,
  1. Install BEAM 4.10.3 32/64 bit, appropriate for your OS. Set `BEAM_HOME`.
  2. copy `beampy.pyd` and `beam_capi.sh` from `$BEAM_EXTAPI/target/unix32` to `$PYTHON32_HOME/DLLs` (not available yet)
  3. Set `PATH=$PATH%;$BEAM_HOME/jre/bin/client`  (todo: avoid this step and what about Mac OS?)

### How to run:

Start Python, then type

    >>> import _beampy
    >>> dir(_beampy)
    >>> help(_beampy)
    >>> 
    >>> from _beampy import *
    >>> p = ProductIO_readProduct('testdata/MER_RR__1PPBCM20110809_093213_000001383105_00223_49375_0022.N1')
    >>> p
    >>> Product_getBandNames(p)
    >>> b13 = Product_getBand(p, 'radiance_13')
    >>> Band_getSpectralWavelength(b13)
    >>> Product_dispose(p)
    >>> 
    >>> import beampy
    >>> p = ProductIO_readProduct('testdata/MER_RR__1PPBCM20110809_093213_000001383105_00223_49375_0022.N1')
    >>> p.getBandNames()
    >>> b13 = p.getBand('radiance_13')
    >>> b13.getSpectralWavelength()
    >>> p.dispose()



Developer Links
---------------
beam-extapi code repository  

* Project on Github: https://github.com/bcdev/beam-extapi
* Java Native Interface (JNI) website: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/
* JNI data types: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/types.html
* JNI function reference: http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html
* Python/C API reference: http://docs.python.org/py3k/c-api/index.html
* Python/C API tutorial: http://docs.python.org/py3k/extending/index.html
* Python/C argument parsing:  http://docs.python.org/py3k/c-api/arg.html
* Microsoft *cl* Compiler: http://msdn.microsoft.com/en-us/library/vstudio/9s7c9wdw.aspx


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
* How to deal with object parameters in which a C string is passed (generator doesn't know, that a string object needs to be created),
  for example Product.writeHeader(Object output)
   --> String_newString(), in Python: String.newString(s)
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
* How to treat Java SE classes: File, Date, Point, Rectangle, etc?
  --> Not addressed so far. One solution is to provide factories for these objects, e.g. in Python
  class Rectangle:
    def __init__(self, obj):
        self._obj = obj
    @staticmethod
    def create(x, y, w, h):
        Rectangle(Rectangle_create(x, y, w, h))

    Where the global Rectangle_create() function is a defined in beampy.pyd (as BeamPyRectangle_create in C) and
    delegates to the Rectangle_create() function in beam_capi.dll

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

To-do List Code Generation
--------------------------

* Python API: For C-function names used in Python types (not BEAM API fucntions), follow convention that is used in
  Python native code, see Python.h and other includes e.g. bytearrayobject.h.
* Done: Use BEAM_HOME env and auto-configure JVM parameters (use Util_listDir).
* Make sure Python classes implement correctly: __eq__, __ne__, __hash__ ...
* Generate usable documentation (Doxgen + PyDoc).
* Handle reference counting JNI / Python/C.
* Handle return parameters in Python (done in C).
* Generate error handling code. Check all JNI and all Py return values.

To-do BEAM API doc
------------------
* Review and update API doc of all core classes
* Remove all "@version $Revision$ $Date$"
* Try to remove all HTML elements
* Get rid of all this annoying console outputs when invoking BEAM API: SLF4J warnings, JAI plain Java error, GeoTools EPSG database creation

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

Windows VC++ using "Debug" configuration:
Fehler	11	error LNK1104: Datei "python32_d.lib" kann nicht geöffnet werden.	C:\Users\Norman\JavaProjects\beam-extapi\msvc\beampy\LINK	beampy

--> No solution; Google does not help. Must use Release configuration

Linux 32/64
Find a way to inject the 'libmawt.so' into the runtime path using cmake.

