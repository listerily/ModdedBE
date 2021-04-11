# Preparation
cmake_minimum_required( VERSION 3.4.1 )

message( "CMAKE_VERSION: ${CMAKE_VERSION}" )
message( "CMAKE_SOURCE_DIR: ${CMAKE_SOURCE_DIR}" )
message( "ANDROID_ABI: ${ANDROID_ABI}" )

# System libraries
find_library( log-lib-path #
              log )

message( "Path of liblog.so: ${log-lib-path}" )

# Import libraries
add_library( xhook
             SHARED IMPORTED )

set_target_properties( xhook
                       PROPERTIES
                       IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/${ANDROID_ABI}/libxhook.so )

add_library( yurai
             SHARED IMPORTED )

set_target_properties( yurai
                       PROPERTIES
                       IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/${ANDROID_ABI}/libyurai.so )

add_library( substrate
             SHARED IMPORTED )

set_target_properties( substrate
                       PROPERTIES
                       IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/${ANDROID_ABI}/libsubstrate.so )


# endercore
add_library( endercore
             SHARED
             endercore.cpp )

# mjscript
add_library( mjscript
             SHARED
             mjscript.cpp )

target_link_libraries( mjscript
                       ${log-lib-path}
                       xhook yurai substrate )
