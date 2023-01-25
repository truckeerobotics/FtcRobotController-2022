# swatbotics/apriltags-cpp/CMakeLists.txt

cmake_minimum_required(VERSION 2.6)

project(APRILTAGS)

set(CMAKE_C_FLAGS "-Wall -g")
set(CMAKE_CXX_FLAGS "-Wall -g")

if (APPLE OR UNIX)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wsign-compare -frounding-math")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wsign-compare -frounding-math")
endif()

set(CMAKE_C_FLAGS_DEBUG "-O")
set(CMAKE_CXX_FLAGS_DEBUG "-O")

set(CMAKE_C_FLAGS_RELEASE "-O2")
set(CMAKE_CXX_FLAGS_RELEASE "-O2")

add_subdirectory(src)

