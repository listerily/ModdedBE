# Require a minimum version of cmake.
cmake_minimum_required( VERSION
                        3.4.1 )             # min

message( "CMAKE_SOURCE_DIR: ${CMAKE_SOURCE_DIR}" )
message( "ANDROID_ABI: ${ANDROID_ABI}" )

# Add a library to the project using the specified source files.
add_library( endercore                      # name
             SHARED                         # STATIC | SHARED | MODULE
             endercore.cpp )                # source...

add_library( mjscript
             SHARED
             mjscript.cpp )

# Find a library.
find_library( log-lib-path                  # VAR
              log )                         # NAMES

# Specify libraries or flags to use when linking a given target and/or its dependents.
target_link_libraries( mjscript             # target...
                       ${log-lib-path}      # item...
                       ${CMAKE_SOURCE_DIR}/../cmakeLibs/${ANDROID_ABI}/libxhook.so
                       ${CMAKE_SOURCE_DIR}/../cmakeLibs/${ANDROID_ABI}/libyurai.so )
