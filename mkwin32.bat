set JDK32_HOME=C:\Program Files (x86)\Java\jdk1.7.0

mkdir target\win32\
del /S /Q target\win32\

cl ^
src\main\c\beam_util.c ^
src\main\c\gen\beam_capi.c ^
src\main\c\gen\beam_capi.def ^
/Fetarget\win32\ ^
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

