set JDK32_HOME=C:\Program Files (x86)\Java\jdk1.7.0
set PYTHON32_HOME=C:\Python32

mkdir target\win32\
del /S /Q target\win32\

rem Building BEAM/C API

cl ^
src\main\c\beam_util.c ^
src\main\c\gen\beam_capi.c ^
src\main\c\gen\beam_capi.def ^
/I"%JDK32_HOME%\include\win32" ^
/I"%JDK32_HOME%\include" ^
/Gm- /GS /Gy /Gd ^
/Zi /Zc:wchar_t /Zc:forScope ^
/EHsc /fp:precise /analyze- ^
/nologo /W3 /WX- /GL ^
/D "WIN32" ^
/D "NDEBUG" ^
/D "_WINDOWS" ^
/D "_USRDLL" ^
/D "BEAM_CAPI_EXPORTS" ^
/D "_WINDLL" ^
/D "_UNICODE" ^
/D "UNICODE" ^
/D "_CRT_SECURE_NO_WARNINGS" ^
/link ^
/OUT:"target\win32\beam_capi.dll" ^
/NOLOGO ^
/LIBPATH:"%JDK32_HOME%\lib" ^
/DLL "jvm.lib" ^
/SUBSYSTEM:WINDOWS ^
/TLBID:1 ^
/DYNAMICBASE ^
/NXCOMPAT  ^
/MACHINE:X86 ^
/ERRORREPORT:QUEUE ^


rem Building BEAM/Python API

cl ^
src\main\c\gen\beampy.c ^
src\main\c\gen\beampy.def ^
/I"%JDK32_HOME%\include\win32" ^
/I"%JDK32_HOME%\include" ^
/I"%PYTHON32_HOME%\include" ^
/Gm- /GS /Gy /Gd ^
/Zi /Zc:wchar_t /Zc:forScope ^
/EHsc /fp:precise /analyze- ^
/nologo /W3 /WX- /GL ^
/D "WIN32" ^
/D "NDEBUG" ^
/D "_WINDOWS" ^
/D "_USRDLL" ^
/D "BEAM_CAPI_EXPORTS" ^
/D "_WINDLL" ^
/D "_UNICODE" ^
/D "UNICODE" ^
/D "_CRT_SECURE_NO_WARNINGS" ^
/link ^
/OUT:"target\win32\beampy.pyd" ^
/NOLOGO ^
/LIBPATH:"%JDK32_HOME%\lib" ^
/LIBPATH:"%PYTHON32_HOME%\libs" ^
/LIBPATH:"target\win32" ^
/DLL "jvm.lib" "python32.lib" "beam_capi.lib" ^
/SUBSYSTEM:WINDOWS ^
/TLBID:1 ^
/DYNAMICBASE ^
/NXCOMPAT  ^
/MACHINE:X86 ^
/ERRORREPORT:QUEUE ^
