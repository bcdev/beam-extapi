cmake_minimum_required(VERSION 2.6)


project(beam-extapi)

##
# set platform independent environment variables and
# check for existence
#

find_package(JNI REQUIRED)

if (JNI_FOUND)
    message (JNI_INCLUDE_DIRS =${JNI_INCLUDE_DIRS})
    message (JNI_LIBRARIES=${JNI_LIBRARIES})
else()
    message (FATAL_ERROR "No valid JDK detected")
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

include_directories(
  ${JNI_INCLUDE_DIRS}
)

##
# build beam c-API
#
add_library(beam_capi SHARED ${C_API_SOURCES})
target_link_libraries(beam_capi ${JNI_LIBRARIES})

##
# build c-Api test program
#
add_executable(beam_capi_test ${C_API_TEST_SOURCES})
target_link_libraries(beam_capi_test beam_capi)
target_link_libraries(beam_capi_test ${JNI_LIBRARIES})
