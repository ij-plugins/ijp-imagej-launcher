package ij_plugins.imagej_launcher

object Utils:
  def isWindows: Boolean =
    System
      .getProperty("os.name")
      .toLowerCase()
      .startsWith("windows")
