# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#设置生成的so动态库最后输出的路径，更改了这个就不能直接运行,需要将so copy到jniLibs以引用的方式进行
#默认的路径在build/intermediates/cmake下面
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

#将src/main/cpp/native-lib.cpp编译成SHARED类型的库
add_library( # Sets the name of the library.
             loader

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # 可能出在多个源文件依次罗列
             src/main/cpp/loader-lib.cpp )

add_library( # Sets the name of the library.
             cat

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # 可能出在多个源文件依次罗列
             src/main/cpp/cat-lib.cpp )

add_library( # Sets the name of the library.
             ps

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # 可能出在多个源文件依次罗列
             src/main/cpp/ps-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

#把jni-test链接到log-lib
target_link_libraries( # Specifies the target library.
                       loader

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

target_link_libraries( # Specifies the target library.
                       cat

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

target_link_libraries( # Specifies the target library.
                       ps

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
