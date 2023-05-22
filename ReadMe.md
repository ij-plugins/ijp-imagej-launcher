IJP ImageJ Launcher
===================

IJP ImageJ Launcher is a native launcher that starts [ImageJ 2] or [Fiji].
It is intended to be a general drop-in replacement for the original [ImageJ Launcher].
IJP ImageJ Launcher is a clean implementation on the core function of starting [ImageJ 2] or [Fiji].


Why Another ImageJ Launcher
---------------------------
I needed to use Fji with the current versions of Java, version 11 and newer.
The original [ImageJ Launcher] works with Java 8, but not that well with the current versions,
in particular not well on Mac with Arm64 processors (Apple Silicon).
I attempted to fix the original [ImageJ Launcher source].
The source is burdened by technical debt,
the logic flow is too complex to correct without a significant rewrite.


Features [work in progress]
--------

* Uses similar options to the original ImageJ Launcher, si IJP Launcher can be drop-in replacement
* Provides native executable for various OS/Hardware systems
    - Windows
    - Mac OS X Arm64 (Apple Silicon)
    - Mac OS X Intel
    - Linux
* Selects location of the ImageJ directory
    - Startup directory or
    - Directory specified by `--ij-dir` command line option
* Locates Java Virtual Machine for ImageJ:
    - Use Java VM requested by the user (`--java-home`)
    - Use `JAVA_HOME` environment variable
    - Search ImageJ directory for available Java executables
* Determines the amount of memory used by JVM based on total system memory use 75% of the max
* Determines available `imagej-launcher*.jar`
* Performs updates pending after the last time ImageJ was closed

### Full list of command line options

```
  -h, --help          prints this usage text
  --version           prints version
  --dry-run           show the command line, but do not run anything
  --info              informational output
  --debug             verbose output
  --java-home <path>  specify JAVA_HOME explicitly
  --ij-dir <path>     set the ImageJ directory to <path> (used to find jars/, plugins/ and macros/)
```

Installation
------------
The IJP ImageJ Launcher executables will be available on the [Releases] page.


[//]: # (Links)

[Fiji]: https://imagej.net/software/fiji/

[ImageJ 2]: https://imagej.net/software/imagej2/

[ImageJ Launcher]:https://imagej.net/learn/launcher

[ImageJ Launcher source]:https://github.com/imagej/imagej-launcher/