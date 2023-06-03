IJP ImageJ Launcher
===================

IJP ImageJ Launcher is a native launcher that starts [ImageJ 2] or [Fiji].
It is intended to be a general drop-in replacement for the original [ImageJ Launcher].
IJP ImageJ Launcher is a clean implementation on the core function of starting [ImageJ 2] or [Fiji].

**Contents**
<!-- TOC -->

* [Why Another ImageJ Launcher?](#why-another-imagej-launcher)
* [Features](#features)
* [Full List of Command Line Options](#full-list-of-command-line-options)
* [Installation](#installation)
    * [Installing Fiji on Mac OS X Arm64](#installing-fiji-on-mac-os-x-arm64)
    * [Installing Fiji on Windows x64](#installing-fiji-on-windows-x64)
    * [Troubleshooting](#troubleshooting)
* [Developer Setup](#developer-setup)

<!-- TOC -->


Why Another ImageJ Launcher?
----------------------------
I needed to use Fji with the current versions of Java, version 11 and newer.
The original [ImageJ Launcher] works with Java 8, but not that well with the current versions,
in particular not well on Mac with Arm64 processors (Apple Silicon).
I attempted to fix the original [ImageJ Launcher source].
The source is burdened by technical debt,
the logic flow is too complex to correct without a significant rewrite.


Features
--------

Here are the futures that are already implemented (see release notes for futures ofa specific release):

* Uses similar options to the original ImageJ Launcher, so IJP Launcher can be used as a drop-in replacement
* Intended to be used with Java 11 or newer (the original launcher can be used for Java 8)
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
* **Performs updates** pending after the last time ImageJ was closed

Full List of Command Line Options
---------------------------------

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

### Installing Fiji on Mac OS X Arm64

This example will show how to:

* Download FIJI and unzip installation.
* Download and install JVM for it.
* Download the IJP ImageJ Launcher and use it to start Fiji

**1. Download FIJI without JRE**

Go to https://fiji.sc/ and select "Download the no-JRE version".
That should get file called `fiji-nojre.zip`

**2. Unzip the `fiji-nojre.zip` in a folder of choice**

You should get new app folder called `Fiji.app`.
"Right-click" to show a popup menu and select "Show Package Contents" to see what is inside the `Fiji.app` folder.
Inside you should see folders and files like "Contents", "db.xml.gz", "ImageJ-linux64", ...

**3. Create place for Java (JRE)**

Inside the `Fiji.app` folder create a new folder called `java`.

**4. Download Java JRE or JDK**

In browser open https://adoptium.net/temurin/releases/
Select:

* operating system: `macOS`
* architecture: `aarch64` also known as Apple Silicon or Arm64
* package: `JRE` (`JDK` is fine too, is larger and supports Java compilation)
* version: `11-LTS` (`17-LTS` will work too, but you will not have JavaScript available, if you want to use it)

Click on `tar.gz` button to download and save to the `java` folder you created earlier.
You should have file like `OpenJDK11U-jre_x64_windows_hotspot_11.0.19_7.tar.gz`.

**5. Uncompress into the `java` folder**

That will create folder like `jdk-11.0.19+7-jre`.
This is the Java VM that IJP ImageJ Launcher will use to start Fiji.

**6. Download the IJP ImageJ Launcher to the Fiji.app directory**

Go to [Releases], download "IJP-ImageJ-Launcher-0.1.0-macosx-arm64"
and "IJP-ImageJ-Launcher-0.1.0-macosx-arm64.command", save them to the `Fiji.app` folder.

The "*.command" file is a helper that can be used to launch Fiji without using command prompt.
Future versions of the IJP Launcher, after v.0.1.0, may eliminate the need for using this file.

**7. Set Executable Permissions**

When you download launcher files they may be saved without executable permissions.

* Open terminal
* Navigate to the Fiji.app folder, for instance, `cd ~/Download/Fiji.app`
* Add executable permission to the launcher and the "*.command" file using

```shell
chmod +x IJP-ImageJ-Launcher-0.1.0-macosx-arm64
chmod +x IJP-ImageJ-Launcher-0.1.0-macosx-arm64.command
```

**8. Start ImageJ**

You can start `IJP-ImageL-Launcher-0.1.0-macosx-arm64` from command line or in the `Fiji.app` folder double-click on `IJP-ImageL-Launcher-0.1.0-macosx-arm64.command` file (note the extension "*
.command")
That should start Fiji.
You may need to open Settings and allow the IJP ImageJ Launcher to run.

You can also create an alis on the Desktop to avoid navigating to the `Fiji.app` folder each time.
Using Finder, press `Option`+`Command` and drag the *.command file to the Desktop.
The original *.command file will stay were it is and a new icon/alias (wth a little arrow at the bottom) will be created
on the Desktop.
Now you can double-click on the new alias on the Desktop to start Fiji.
You can rename the Desktop alias to whatever you like, for instance, `Fiji`, but do not change names of the downloaded
files, otherwise the alias (and *.command) may no longer work, and you will need to use terminal to start the launcher.

If you have problems installing, please report in [Discussions] or [Image.sc Forum]

### Installing Fiji on Windows x64

This example will show how to:

* Download FIJI and unzip installation.
* Download and install JVM for it.
* Download the IJP ImageJ Launcher and use it to start Fiji

**1. Download FIJI without JRE**

Go to https://fiji.sc/ and select "Download the no-JRE version".
That should get file called `fiji-nojre.zip`

**2. Unzip the `fiji-nojre.zip` in a folder of choice**

You should get new app folder called `Fiji.app`.
Inside you should see folders and files like "Contents", "db.xml.gz", "ImageJ-linux64", ...

**3. Create place for Java (JRE)**

Inside the `Fiji.app` folder create a new folder called `java`.

**4. Download Java JRE or JDK**

In browser open https://adoptium.net/temurin/releases/
Select:

* operating system: `Windows`
* architecture: `x64` also known as Apple Silicon or Arm64
* package: `JRE` (`JDK` is fine too, is larger and supports Java compilation)
* version: `11-LTS` (`17-LTS` will work too, but you will not have JavaScript available, if you want to use it)

Click on `.zip` button to download and save to the `java` folder you created earlier.
You should have file like `OpenJDK11U-jre_x64_windows_hotspot_11.0.19_7.zip`.

**5. Uncompress into the `java` folder**

That will create folder like `jdk-11.0.19+7-jre`.
This is the Java VM that IJP ImageJ Launcher will use to start Fiji.

**6. Download the IJP ImageJ Launcher to the Fiji.app directory**

Go to [Releases], download "IJP-ImageJ-Launcher-0.1.0-windows_x64.exe", save it to the `Fiji.app` folder.

**7. Start ImageJ**

In the `Fiji.app` folder double-click on `IJP-ImageJ-Launcher-0.1.0-windows_x64.exe`.
That should start Fiji.

You can also create a shortcut on the Desktop to avoid navigating to the `Fiji.app` folder each time.

**_Left_**-click on the `IJP-ImageJ-Launcher-0.1.0-windows_x64.exe` and drag it to the Desktop.
Once you release mouse button, a pop-up manu will open, select "Create shortcut here".
Now you can double-click on the new shortcut on the Desktop to start Fiji.

You can rename the Desktop alias to whatever you like, for instance, `Fiji`.

If you have problems installing, please report in [Discussions] or [Image.sc Forum]

### Troubleshooting

You can start the IJP Image Launcher from the terminal and see diagnostic printouts that may help troubleshoot potential
issues.

1. Open the terminal (command prompt).
2. Navigate to `Fiji.app` directory, for instance `cd ~/Download/Fiji.app`
3. Run IJP ImageJ Launch typing: `./IJP-ImageJ-Launcher-0.1.0-macosx-arm64 --debug`

You should see diagnostic information about how the IJP ImageJ Launcher is attempting to start Fiji.
The error messages should help you to address the issue.
Please use [Discussions] or [Image.sc Forum] if you have additional questions.

Developer Setup
---------------
Information here is only applicable if you want to rebuild from sources.

The IJP ImageJ Launcher is written mostly in [Scala].
With about 10 lines of C code.
Native binaries are created with help from [Scala Native].

To rebuild the IJP ImageJ Launcher from sources,
you need to setup Scala Native dependencies following instructions at [Scala Native].
In brief, you will need to install the following: JDK, SBT, and LLVM/CLang.
Details depend on OS.
On Windows you will also need Visual Studio 2019 (the Community Editions is sufficient).
After requirements are installed, you should be able to build and run the launcher using command:

```shell
sbt run
```

You can pass additional command line arguments, for instance `--help`:

```shell
sbt "run --help"
```

Notice the use of quotes.

[Discussions]: https://github.com/ij-plugins/ijp-imagej-launcher/discussions

[Fiji]: https://imagej.net/software/fiji/

[Image.sc Forum]: https://forum.image.sc/tags/c/usage-issues/7/fiji

[ImageJ 2]: https://imagej.net/software/imag

[ImageJ Launcher]: https://imagej.net/learn/launcher

[ImageJ Launcher source]: https://github.com/imagej/imagej-launcher/

[Releases]: https://github.com/ij-plugins/ijp-imagej-launcher/releases

[Scala]: https://www.scala-lang.org/

[Scala Native]: https://scala-native.org/en/stable/