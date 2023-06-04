
#include <stdio.h>

#if defined(__linux__)
#include <unistd.h>
#include <limits.h>
#elif defined(__APPLE__)
#include <stdint.h>
#include <sys/syslimits.h>
#elif defined(_WIN32)
#include <windows.h>
#endif

size_t path_max() {
#ifdef _WIN32
    return MAX_PATH;
#else
    return PATH_MAX + 1;
#endif
}

void get_exe_path(char* exe_path, size_t size) {

    exe_path[0] = 0;

#if defined(__linux__)
//    char arg1[20];
//    sprintf( arg1, "/proc/%d/exe", getpid() );
//    printf("arg1 = %d\n", arg1);
//    readlink( arg1, exepath, PATH_MAX );
    readlink( "/proc/self/exe", exe_path, size );
#elif defined(__APPLE__)
    if (_NSGetExecutablePath(exe_path, &size) != 0)
        printf("ERROR: buffer too small; need size %lu\n", size);
#elif defined(_WIN32)
    unsigned int len = GetModuleFileNameA(GetModuleHandleA(0x0), exe_path, size);
    if (len == 0) // memory not sufficient or general error occurred
        printf("ERROR: buffer too small or general error.\n");
#endif
}