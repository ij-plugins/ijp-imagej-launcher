Feature release: support better system integration on macOS - support installing ImageJ/Fiji in the application
directory.
See macOS installation info in the [ReadMe](https://github.com/ij-plugins/ijp-imagej-launcher)

* Use launcher location to infer `ij-dir`. #5
* Session log is saved to `~/.ijp_imagej_launcher.log` to facilitate troubleshooting when not running from command
  prompt. The log is reset for each session. #6
* Better inference of ImageJ directory on macOS - consider launcher being in subdirectory "Contents/MacOS" #7
