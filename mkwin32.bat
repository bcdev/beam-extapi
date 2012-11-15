set JDK32_HOME=C:\Program Files (x86)\Java\jdk1.7.0

cl ^
src/main/c/beam_util.c ^
src/main/c/gen/beam_capi.c ^
/I"%JDK32_HOME%\include\win32" ^
/I"%JDK32_HOME%\include" ^
/Gm- /EHsc /GS /Gy /Gd ^
/Zi /Zc:wchar_t /Zc:forScope ^
/fp:precise /analyze- /errorReport:queue ^
/nologo /W3 /WX- /GL ^
/D "WIN32" ^
/D "NDEBUG" ^
/D "_WINDOWS" ^
/D "_USRDLL" ^
/D "BEAM_CAPI_EXPORTS" ^
/D "_WINDLL" ^
/D "_UNICODE" ^
/D "UNICODE" ^
/link ^
/DLL ^
/OUT:"beam_capi.dll" ^
/DEF:"src\main\c\gen\beam_capi.def" ^
/NOLOGO ^
/LIBPATH:"%JDK32_HOME%\lib" ^
"jvm.lib" ^
/SUBSYSTEM:WINDOWS ^
/TLBID:1 ^
/DYNAMICBASE ^
/NXCOMPAT  ^
/MACHINE:X86 ^
/ERRORREPORT:QUEUE ^

