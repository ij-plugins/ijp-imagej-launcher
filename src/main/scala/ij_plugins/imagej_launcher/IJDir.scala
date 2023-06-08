/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import ij_plugins.imagej_launcher.Main.Config
import os.{FilePath, Path}

import scala.util.control.NonFatal

object IJDir:

  /** Name of an ImageJ sub-directory containing jars */
  val jarsDirName = "jars"

  /** Locate ImageJ directory */
  def locate(config: Config)(using logger: Logger): Either[String, Path] =
    logger.debug("Looking for ImageJ directory")

    config.ijDir match
      case Some(d) =>
        logger.debug(s"  Considering provided ij-dir: '$d'")
        asPath(d.getPath).flatMap: p =>
          logger.debug(s"    '$p'")
          if isIJDir(p) then Right(p)
          else Left(s"ij-dir is not an ImageJ directory [$p]")
      case None =>
        logger.debug("  Considering application directory")
        val appPath = Native.applicationPath()
        logger.debug(s"  Application directory: '$appPath'")
        if isIJDir(appPath) then
          Right(appPath)
        else
          logger.debug("  Application directory is not an ImageJ directory")
          logger.debug("  Considering current working directory")
          val cwd = os.pwd
          logger.debug(s"  Current working directory: '$cwd'")
          if isIJDir(cwd) then
            Right(cwd)
          else
            logger.debug("  Current working directory is not an ImageJ directory.")
            Left("Cannot locate ImageJ directory.")

  private def isIJDir(path: Path): Boolean =
    os.exists(path) &&
      os.isDir(path) &&
      os.list(path).exists(f => f.last == jarsDirName && os.isDir(f))

  private def asPath(filePath: String): Either[String, Path] =
    if filePath.trim.startsWith("~") then
      try
        Right(Path.expandUser(filePath))
      catch
        case NonFatal(ex) =>
          Left(s"Not an absolute path: '$filePath' [${ex.getMessage}]")
    else
      FilePath(filePath) match
        case p: Path => Right(p)
        case _       => Left(s"Not an absolute path: '$filePath'")

end IJDir
