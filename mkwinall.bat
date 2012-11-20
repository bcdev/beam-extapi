@echo off

rem set MSVC_HOME=C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC
rem set PYTHON32_HOME=C:\Python32
rem set JDK32_HOME=C:\Program Files (x86)\Java\jdk1.7.0_07

if "%JDK32_HOME%"=="" goto no_jdk
if "%PYTHON32_HOME%"=="" goto no_python

call %~dp0mkwin.bat x86 "%JDK32_HOME%" "%PYTHON32_HOME%" winx86
rem call %~dp0mkwin.bat x64 "%JDK64_HOME%" "%PYTHON64_HOME%" winx64

goto ok

:no_jdk
echo "Please specify JDK32_HOME (path to your Java 32-bit JDK)"
goto error

:no_python
echo "Please specify PYTHON32_HOME (path to your Python 32-bit SDK)"
goto error

:ok
echo "OK!"
exit /B 0

:error
exit /B 1
