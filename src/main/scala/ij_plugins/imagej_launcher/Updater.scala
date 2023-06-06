/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import os.{Path, RelPath}

import scala.util.control.NonFatal

object Updater:

  /**
   * Perform update using files in `update` subdirectory, if it exists.
   * @param ijDir imagej
   * @param dryRun locate files to process but do not make any changes, but count the files that would be processed
   * @param logger configured logger
   * @return Number of files processed or an error message.
   */
  def update(ijDir: Path, dryRun: Boolean)(using logger: Logger): Either[String, Long] =
    try
      val updateDir = ijDir / "update"
      // Count used only for debug info
      var count = 0
      if os.exists(updateDir) then
        logger.info("Performing update..")
        os.walk(updateDir)
          .filter(os.isFile)
          .foreach: src =>
            val dst = ijDir / src.relativeTo(updateDir)
            if os.size(src) == 0 then
              logger.debug(s"remove: $dst")
              if !dryRun then os.remove(dst)
              logger.debug(s"remove: $src")
              if !dryRun then os.remove(src)
            else
              logger.debug(s"move  : $src -> $dst")
              if !dryRun then os.move(src, dst, replaceExisting = true, createFolders = true)
            count += 1
        logger.debug(s"Delete update directory: $updateDir")
        if !dryRun then deleteEmptyDirs(updateDir)
        Right(count)
      else
        logger.info("No update found")
        Right(count)
    catch
      case NonFatal(ex) =>
        ex.printStackTrace()
        Left(s"Failed to perform update: ${ex.getMessage} - ${ex.getClass.getSimpleName}")

  private def deleteEmptyDirs(dir: Path)(using logger: Logger): Unit =
    logger.debug(s"Cleaning directory: $dir")
    os.list(dir)
      .filter(os.isDir)
      .foreach(p => deleteEmptyDirs(p))

    if os.list(dir).isEmpty then
      logger.debug(s"Removing empty dir: $dir")
      os.remove(dir)
    else
      logger.debug(s"Cannot delete directory, not empty: $dir")

end Updater
