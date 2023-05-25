IJP ImageJ Launcher
===================

IJP ImageJ Launcher is a native launcher that starts [ImageJ 2] or [Fiji].
It is intended to be a general drop-in replacement for the original [ImageJ Launcher].
IJP ImageJ Launcher is a clean implementation on the core function of starting [ImageJ 2] or [Fiji].

**Contents**
<!-- TOC -->

* [Why Another ImageJ Launcher](#why-another-imagej-launcher)
* [Features [work in progress]](#features-work-in-progress)
    * [Full list of command line options](#full-list-of-command-line-options)
* [Installation](#installation)
* [Developer Setup](#developer-setup)

<!-- TOC -->


Why Another ImageJ Launcher
---------------------------
I needed to use Fji with the current versions of Java, version 11 and newer.
The original [ImageJ Launcher] works with Java 8, but not that well with the current versions,
in particular not well on Mac with Arm64 processors (Apple Silicon).
I attempted to fix the original [ImageJ Launcher source].
The source is burdened by technical debt,
the logic flow is too complex to correct without a significant rewrite.


Features
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

Example of Installing Fiji with the IJP ImageJ Launcher on Mac OS X Arm64
-------------------------------------------------------------------------

#### 1. Download FIJI without JRE

Go to https://fiji.sc/ and select "Download the no-JRE version".
That should het you file called `fiji-nojre.zip`

#### 2. Unzip the `fiji-nojre.zip` in the directory of choice

In Mac OS X, if you double-click on the file `fiji-nojre.zip`
You should get `Fiji.app` click and select "show Content"

#### 3. Create place for Java (JRE)

Inside `Fiji.app` create directory `java`.
Next to directories that are already there, like `Contents`, `images`, `jars`, ...

#### 4. Download Java JRE or JDK,

In browser open https://adoptium.net/temurin/releases/
Select:

* operating system: `macOS`
* architecture: `aarch64` also know as Apple Silicon or Arm64
* package: `jre` (`jdk` is fine too, is larger supports Java compilation)
* version: `11` (`17` will work too, but you will not have JavaScript available, if you use it)

Click on `tar.gz` button and download to the `java` directory you created earlier.
You should have file like `OpenJDK11U-jre_x64_windows_hotspot_11.0.19_7.tar.gz`.

#### 5. Uncompress into the `java` directory

That will create directory like `jdk-11.0.19+7-jre`

#### 6. Download IJP ImageJ Launcher to Fiji.app directory

Go to [Releases] and download `???` and `???.???`

#### 7. Start ImageJ

In Fiji.app double-click on `???.???` (note the extension ???) that should start FIJI.

You can also create an alis on the desktop to avoid navigating to Fiji.app directory each time.
Press ??? and ??? and drag  `???.???` to the Desktop.
It will create an alias.
Now you can double-click on the alias `???.???` on the Desktop to start Fiji.

If you have problems installing, please report in [Discussions]

Troubleshooting
---------------

1. Open command prompt (terminal).
2. Navigate to `Fiji.app` directory
3. Run IJP ImageJ Launch typing: `./ijp-imagej-launcher --debug`

You should see diagnostic information about how `ijp-imagej-launcher` is attempting to start Fiji.
The error messages shoudl help you to address the issue.
Please use [Discussions] if you have additional questions

Developer Setup
---------------
Information here is only applicable if you want to rebuild from sources.

Setup Scala Native dependencies following instructions at:https://scala-native.org/en/v0.4.12/user/setup.html
In brief, you will need the following installed: JDK, SBT, and LLVM/CLang.
Details depend on OS.
On Windows you will also need Visual Studio 2019 (the Community Editions is sufficient).
Alternatively on Windows, you can build using WSL Linux prompt, in that case, follow Linux installation instructions.

This should work with JDK 8 or newer, including the latest JDK 20.

After requirements are installed, you should be able to build and run the launcher using command:

```shell
sbt run
```

You can pass additional command line arguments, for instance `--help`:

```shell
sbt "run --help"
```

Notice the use of quotes.

[//]: # (Links)

[Fiji]: https://imagej.net/software/fiji/

[ImageJ 2]: https://imagej.net/software/imagej2/

[ImageJ Launcher]:https://imagej.net/learn/launcher

[ImageJ Launcher source]:https://github.com/imagej/imagej-launcher/