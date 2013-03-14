cmake_minimum_required(VERSION 2.6)


project(beam-extapi)

##
# set platform independent environment variables and
# check for existence
#
set(JDK_HOME $ENV{JDK32_HOME})
if (NOT DEFINED JDK_HOME)
  message (FATAL_ERROR "JDK32_HOME is not defined")
endif()


##
# set operating system dependent include paths
# and environment variables and check for existence
#
# todo - check for Win64
set(JNI_PLATFORM_INLCUDE "")
if (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
  set(JNI_PLATFORM_INLCUDE "linux")

  set(PY32_INCLUDE $ENV{PYTHON32_INCLUDE})
  if (NOT DEFINED PY32_INCLUDE)
    message (FATAL_ERROR "PYTHON32_INCLUDE is not defined")
  endif()
endif()

if (${CMAKE_SYSTEM_NAME} MATCHES "Windows")
  set(JNI_PLATFORM_INLCUDE "win32")

  set(PY32_HOME $ENV{PYTHON32_HOME})
  if (NOT DEFINED PY32_HOME)
    message (FATAL_ERROR "PYTHON32_HOME is not defined")
  endif()
  set(PY32_INCLUDE ${PY32_HOME}/include)
endif()

if (${JNI_PLATFORM_INLCUDE} STREQUAL "")
  message (FATAL_ERROR "Operating system not supported")
endif()

##
# define operating system dependent
# target directory
#
if (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
  set(LIBRARY_OUTPUT_PATH target/linux)
  set(EXECUTABLE_OUTPUT_PATH target/linux)
endif()

if (${CMAKE_SYSTEM_NAME} MATCHES "Windows")
  set(LIBRARY_OUTPUT_PATH target/winx86)
  set(EXECUTABLE_OUTPUT_PATH target/winx86)
endif()

##
# define input files
#
file(GLOB_RECURSE C_API_SOURCES src/main/c/beam_util.c)
file(GLOB_RECURSE C_API_SOURCES src/main/c/gen/beam_capi.c)
file(GLOB_RECURSE PY_API_SOURCES src/main/c/gen/beampy.c)

include_directories(
  ${JDK_HOME}/include
  ${JDK_HOME}/include/${JNI_PLATFORM_INLCUDE}
  ${PY32_INCLUDE}
)

##
# build beam c-API
#
add_library(beam_capi SHARED ${C_API_SOURCES})

##
# build beam python-API
#
add_library(beam-pyapi SHARED ${PY_API_SOURCES})
