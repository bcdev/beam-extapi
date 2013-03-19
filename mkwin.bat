@echo off

if "%1" == "" goto usage
if not "%5" == "" goto usage

if /i %1 == x86       set PF=x86
if /i %1 == amd64     set PF=amd64
if /i %1 == x64       set PF=amd64
if /i %1 == arm       set PF=arm
if /i %1 == x86_arm   set PF=x86_arm
if /i %1 == x86_amd64 set PF=x86_amd64

if %PF% == "" goto usage

if not %2 == "" (set "J_HOME=%~2") else (goto usage)
if not %3 == "" (set "PY_HOME=%~3") else (goto usage)
if not %4 == "" (set "OUT=%~4") else (goto usage)

if "%MSVC_HOME%"=="" goto no_msvc

echo p1: %MSVC_HOME%
echo p2: %J_HOME%
echo p3: %PY_HOME%
echo p4: %OUT%

call "%MSVC_HOME%\vcvarsall.bat" %PF%

mkdir target\%OUT%\
del /S /Q target\%OUT%\

rem Building BEAM/C API

cl ^
src\main\c\beam_util.c ^
src\main\c\gen\beam_capi.c ^
src\main\c\gen\beam_capi.def ^
/Fotarget\%OUT%\ ^
/Fdtarget\%OUT%\ ^
/I"%J_HOME%\include\win32" ^
/I"%J_HOME%\include" ^
/Gm- /GS /Gy /Gd ^
/MDd ^
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
/OUT:"target\%OUT%\beam_capi.dll" ^
/NOLOGO ^
/LIBPATH:"%J_HOME%\lib" ^
/DLL "jvm.lib" ^
/SUBSYSTEM:WINDOWS ^
/TLBID:1 ^
/DYNAMICBASE ^
/NXCOMPAT  ^
/MACHINE:%PF%


rem Building BEAM/C API

cl ^
src\main\c\beam_capi_test.c ^
/Fotarget\%OUT%\ ^
/Fdtarget\%OUT%\ ^
/I"%J_HOME%\include\win32" ^
/I"%J_HOME%\include" ^
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
/OUT:"target\%OUT%\beam_capi_test.exe" ^
/NOLOGO ^
/LIBPATH:"%J_HOME%\lib" ^
/LIBPATH:"target\%OUT%" ^
/SUBSYSTEM:CONSOLE ^
/TLBID:1 ^
/DYNAMICBASE ^
/NXCOMPAT  ^
/MACHINE:%PF% ^
"jvm.lib" "beam_capi.lib"

rem Building BEAM/Python API

cl ^
src\main\c\beampy_carray.c ^
src\main\c\gen\beampy.c ^
src\main\c\gen\beampy.def ^
/Fotarget\%OUT%\ ^
/Fdtarget\%OUT%\ ^
/I"%J_HOME%\include\win32" ^
/I"%J_HOME%\include" ^
/I"%PY_HOME%\include" ^
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
/OUT:"target\%OUT%\_beampy.pyd" ^
/NOLOGO ^
/LIBPATH:"%J_HOME%\lib" ^
/LIBPATH:"%PY_HOME%\libs" ^
/LIBPATH:"target\%OUT%" ^
/DLL "jvm.lib" "python32.lib" "beam_capi.lib" ^
/SUBSYSTEM:WINDOWS ^
/TLBID:1 ^
/DYNAMICBASE ^
/NXCOMPAT  ^
/MACHINE:%PF%

copy "target\%OUT%\beam_capi.dll" "%PY_HOME%\DLLs\"
copy "target\%OUT%\_beampy.pyd" "%PY_HOME%\DLLs\"
copy "src\main\c\gen\beampy.py" "%PY_HOME%\Lib\"


goto ok

:no_msvc
echo "Please specify MSVC_HOME (path to your Microsoft Visual C++ SDK)"

:ok
echo "OK!"
exit /B 0

:usage
echo Error in script usage. The correct usage is:
echo     %0 ^<platform^> ^<JDK_HOME^> ^<PYTHON_HOME^> ^<OUT^>
echo where ^<platform^> is: x86 ^| amd64 ^| arm ^| x86_amd64 ^| x86_arm
echo:
echo For example:
echo     %0 x86_amd64 ...
goto :eof