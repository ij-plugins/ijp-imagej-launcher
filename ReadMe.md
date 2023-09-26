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
        * [Start-up log `~/.ijp_imagej_launcher.log`](#start-up-log-ijpimagejlauncherlog)
        * [Starting from command prompt](#starting-from-command-prompt)
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

Go to https://imagej.net/software/fiji/downloads and download the **"No JRE"** version (not specific to any OS).
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

* Operating System: `macOS`
* Architecture: `aarch64` also known as Apple Silicon or Arm64
* Package Type: `JRE` (`JDK` is fine too, is larger and supports Java compilation)
* Version: `11-LTS` (`17-LTS` will work too, but you will not have JavaScript available, if you want to use it)

Click on `tar.gz` button to download and save to the `java` folder you created earlier.
You should have file like `OpenJDK11U-jre_aarch64_mac_hotspot_11.0.20_8.tar.gz`.

**5. Uncompress into the `Fiji.app/java` folder**

That will create folder like `jdk-11.0.20+8-jre`.
This is the Java VM that IJP ImageJ Launcher will use to start Fiji.

**6. Download the IJP ImageJ Launcher and uncompress**

Go to [Releases], download "IJP-ImageJ-Launcher-0.2.0-macosx-arm64.zip"

Uncompress "IJP-ImageJ-Launcher-0.2.0-macosx-arm64.zip".
Inside you will get `ImageJ-macosx`.

**7. Add to Fiji.app**

Inside `Fiji.app` locate folder `Contents/MacOS`.

Copy `ImageJ-macosx` to the `Contents/MacOS` folder, replacing `ImageJ-macosx` that was there.

**8. Move Fiji.app to the Application folder**

At this point you can move the `Fiji.app` folder to the Applications folder and use is as a regular msOS application.

**9. Troubleshooting**

When you attempt to run Fiji with the new Launcher you may get a warning dialog
![macOS_warning_dialog_01.png](assets%2FmacOS_warning_dialog_01.png)

Possible work-around

1. Delete `Fuji.app` folder
2. Uncompressed `fiji-nojre.zip` to recreate `Fuji.app` folder, but do not make any changes to it yet. You may need to do it is different folder than before.
3. Control-clock on `Fuji.app` and select "Open". You will see dialog saying 
    "macOS cannot verify the developer of “Fiji”. Are you sure you want to open it?"
4. Click on "Open". You will see Fiji logo, but the application will close since it is not setup yet
5. Now you can repeat steps "3. Create place for Java (JRE)" to "7. Add to Fiji.app" above

If you have problems installing, please report in [Discussions] or [Image.sc Forum]

### Installing Fiji on Windows x64

This example will show how to:

* Download FIJI and unzip installation.
* Download and install JVM for it.
* Download the IJP ImageJ Launcher and use it to start Fiji

**1. Download FIJI without JRE**

Go to https://imagej.net/software/fiji/downloads and download the **"No JRE"** version (not specific to any OS).
That should get file called `fiji-nojre.zip`

**2. Unzip the `fiji-nojre.zip` in a folder of choice**

You should get new app folder called `Fiji.app`.
Inside you should see folders and files like "Contents", "db.xml.gz", "ImageJ-linux64", ...

**3. Create place for Java (JRE)**

Inside the `Fiji.app` folder create a new folder called `java`.

**4. Download Java JRE or JDK**

In browser open https://adoptium.net/temurin/releases/
Select:

* Operating System: `Windows`
* Architecture: `x64` also known as Apple Silicon or Arm64
* Package Type: `JRE` (`JDK` is fine too, is larger and supports Java compilation)
* Version: `11-LTS` (`17-LTS` will work too, but you will not have JavaScript available, if you want to use it)

Click on `.zip` button to download and save to the `java` folder you created earlier.
You should have file like `OpenJDK11U-jre_x64_windows_hotspot_11.0.20_8.zip`.

**5. Uncompress into the `Fiji.app/java` folder**

That will create folder like `jdk-11.0.20+8-jre`.
This is the Java VM that IJP ImageJ Launcher will use to start Fiji.

**6. Download the IJP ImageJ Launcher to the Fiji.app directory**

Go to [Releases], download "IJP-ImageJ-Launcher-0.2.0-windows_x64.exe", save it to the `Fiji.app` folder.

**7. Start ImageJ**

In the `Fiji.app` folder double-click on `IJP-ImageJ-Launcher-0.2.0-windows_x64.exe`.
That should start Fiji.

You can also create a shortcut on the Desktop to avoid navigating to the `Fiji.app` folder each time.

**_Left_**-click on the `IJP-ImageJ-Launcher-0.2.0-windows_x64.exe` and drag it to the Desktop.
Once you release mouse button, a pop-up manu will open, select "Create shortcut here".
Now you can double-click on the new shortcut on the Desktop to start Fiji.

You can rename the Desktop alias to whatever you like, for instance, `Fiji`.

If you have problems installing, please report in [Discussions] or [Image.sc Forum]

### Troubleshooting

#### Start-up log `~/.ijp_imagej_launcher.log`

The IJP-ImageJ-Launcher writes diagnostic info to a file `.ijp_imagej_launcher.log` in the users home directory.
The information recorded is some as using `--debug` on command line.

#### Starting from command prompt

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