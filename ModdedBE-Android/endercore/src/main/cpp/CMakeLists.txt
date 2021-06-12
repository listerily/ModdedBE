# Preparation
cmake_minimum_required( VERSION 3.4.1 )

message( "CMAKE_VERSION: ${CMAKE_VERSION}" )
message( "CMAKE_SOURCE_DIR: ${CMAKE_SOURCE_DIR}" )
message( "ANDROID_ABI: ${ANDROID_ABI}" )

# System libraries
find_library( log-lib-path
              log )

message( "Path of liblog.so: ${log-lib-path}" )

# endercore
add_library( endercore
             SHARED
             endercore.cpp )

# mjscript
add_library( mjscript
             SHARED
             mjscript.cpp )

target_link_libraries(
        mjscript
        ${log-lib-path}
        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libxhook.so
        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libsubstrate.so
)
