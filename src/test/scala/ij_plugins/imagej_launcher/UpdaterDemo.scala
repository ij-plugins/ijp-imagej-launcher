package ij_plugins.imagej_launcher

import os.Path

@main
def updaterDemo(ijDir: String): Unit =
  given logger: Logger = new Logger(Logger.Level.Debug)
  Updater.update(Path(ijDir), dryRun = false) match
    case Right(count) => println(s"Processed $count files")
    case Left(error)  => println(s"Failed with error: $error")
