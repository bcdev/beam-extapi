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
#### Linux
if (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
  set(JNI_PLATFORM_INLCUDE "linux")

  #### python
  set(PY32_INCLUDE $ENV{PYTHON32_INCLUDE})
  if (NOT DEFINED PY32_INCLUDE)
    message (FATAL_ERROR "PYTHON32_INCLUDE is not defined")
  endif()

  #### jvm paths
  # @todo - check 32/64 bit and adapt accordingly
  if(CMAKE_SYSTEM_PROCESSOR MATCHES "^i.86$")
    set(JAVA_LIB_ARCHITECTURE "i386")
  elseif(CMAKE_SYSTEM_PROCESSOR STREQUAL "x86_64" OR CMAKE_SYSTEM_PROCESSOR STREQUAL "amd64")
    set(JAVA_LIB_ARCHITECTURE "amd64")
  else()
    set(JAVA_LIB_ARCHITECTURE ${CMAKE_SYSTEM_PROCESSOR})
  endif()

  set(JVM_SHARED_LIB ${JDK_HOME}/jre/lib/${JAVA_LIB_ARCHITECTURE}/server/libjvm.so)
endif()

#### Windows
if (${CMAKE_SYSTEM_NAME} MATCHES "Windows")

  #### python
  set(PY32_HOME $ENV{PYTHON32_HOME})
  if (NOT DEFINED PY32_HOME)
    message (FATAL_ERROR "PYTHON32_HOME is not defined")
  endif()
  set(PY32_INCLUDE ${PY32_HOME}/include)

  #### jvm paths
  set(JNI_PLATFORM_INLCUDE "win32")
  set(JVM_SHARED_LIB ${JDK_HOME}/jre/bin/server/jvm.dll)
endif()

#### unsupported
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
set(C_API_SOURCES src/main/c/beam_util.c src/main/c/gen/beam_capi.c)

file(GLOB C_API_TEST_SOURCES src/main/c/beam_capi_test.c)
file(GLOB PY_API_SOURCES src/main/c/gen/beampy.c)

message(${C_API_SOURCES})

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
# build c-Api test program
#
add_executable(beam_capi_test ${C_API_TEST_SOURCES})
target_link_libraries(beam_capi_test beam_capi)
target_link_libraries(beam_capi_test ${JVM_SHARED_LIB})

##
# build beam python-API
#
add_library(beam-pyapi SHARED ${PY_API_SOURCES})
