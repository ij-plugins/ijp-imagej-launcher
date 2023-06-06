/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import os.Path

object IJConfigFile:

  private val FileName: String         = "ImageJ.cfg"
  private val MagicHeader: String      = "# ImageJ startup properties"
  private val MaxHeapMBKey: String     = "maxheap.mb"
  private val JvmArgsKey: String       = "jvmargs"
  private val LegacyModeKey: String    = "legacy.mode"
  private val AssignmentSymbol: String = "="
  private val CommentSymbol: String    = "#"

  /**
   * Read `ImageJ.cfg` from a specified directory.
   * The configuration file must be in ImageJ2 format (with magic header "# ImageJ startup properties").
   *
   * @param dir directory where `ImageJ.cfg` is located
   * @param logger logger
   * @return option containing the configuration file (Right).
   *         The option is empty if file is not present in input `dir`.
   *         An error message is returned if file is present but cannot be read without errors (Left).
   */
  def readFromDir(dir: Path)(using logger: Logger): Either[String, Option[IJCConfig]] =
    logger.debug(s"Reading ImageJ configuration from directory: $dir")
    val path = dir / FileName
    logger.debug(s"  Looking for $FileName in: '$path'")
    readFromFile(path)

  private[imagej_launcher] def readFromFile(ijConfigFile: Path)(using logger: Logger): Either[String, Option[IJCConfig]] =
    if os.exists(ijConfigFile) then
      val lines = os.read.lines(ijConfigFile)
      val cfgE =
        lines.headOption match
          case Some(line) =>
            if line.trim.startsWith(MagicHeader) then
              logger.debug(s"  Parsing $FileName")
              for
                props      <- parseProps(lines)
                maxHeapMB  <- parseLong(props, MaxHeapMBKey)
                jvmArgs    <- parseString(props, JvmArgsKey)
                legacyMode <- parseBoolean(props, LegacyModeKey)
              yield Option(IJCConfig(maxHeapMB, jvmArgs, legacyMode))
            else
              Left(s"$FileName is invalid, does not contain header: '$MagicHeader'")
          case None =>
            Left(s"$FileName is empty.")

      // Add context to the error message, if there is one
      cfgE.left.map(e => s"Failed to read $FileName. $e")
    else
      logger.debug(s"  $FileName not found: $ijConfigFile")
      Right(None)

  private[imagej_launcher] def parseProps(lines: Seq[String]): Either[String, Map[String, String]] =
    // Parse each line
    val lineResults: Seq[Either[String, (String, String)]] =
      lines
        .zipWithIndex
        .filter { case (line, _) => !line.startsWith(CommentSymbol) }
        .map { case (line, lineNumber) =>
          val index = line.indexOf(AssignmentSymbol)
          if index > 0 then
            val key = line.substring(0, index).trim
            if !key.isBlank then
              Right(key -> line.substring(index + 1).trim)
            else
              Left(s"Error in line $lineNumber: key cannot be empty: '$line'")
          else if index == 0 then
            Left(s"Error in line $lineNumber: line cannot start with an assignment symbol '$AssignmentSymbol': '$line'")
          else
            Left(s"Error in line $lineNumber: no assignment symbol '$AssignmentSymbol' present: '$line'")
        }

    // Separate lines with errors
    val errors = lineResults.collect { case e: Left[?, ?] => e }
    if errors.isEmpty then
      // Convert key->value pairs to a map
      val r =
        lineResults
          .collect { case e: Right[?, ?] => e }
          .map(_.value)
          .toMap
      Right(r)
    else
      Left(errors.map(_.value).mkString("\n"))

  private def parseLong(props: Map[String, String], key: String): Either[String, Long] =
    parseString(props, key).flatMap: s =>
      s.toLongOption match
        case Some(v) => Right(v)
        case None    => Left(s"Failed to parse '$s' as an integer.")

  private def parseString(props: Map[String, String], key: String): Either[String, String] =
    props.get(key) match
      case Some(v) => Right(v)
      case None    => Left(s"No key named '$key'.")

  private def parseBoolean(props: Map[String, String], key: String): Either[String, Boolean] =
    parseString(props, key).flatMap: s =>
      s.toBooleanOption match
        case Some(v) => Right(v)
        case None    => Left(s"Failed to parse '$s' as a boolean.")

  /** Configuration loaded from `ImageJ.cfg`. */
  case class IJCConfig(maxHeapMB: Long, jvmArgs: String, legacyMode: Boolean)

end IJConfigFile
