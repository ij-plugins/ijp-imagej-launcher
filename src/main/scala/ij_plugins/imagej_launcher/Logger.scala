/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import ij_plugins.imagej_launcher.Logger.{Level, logToFile}
import os.Path

import java.nio.file.{Files, StandardOpenOption}
import scala.util.control.NonFatal

class Logger(val level: Level = Level.Info):

  def debug(msg: String): Unit = pprint(Level.Debug, msg)
  def info(msg: String): Unit  = pprint(Level.Info, msg)
  def error(msg: String): Unit = pprint(Level.Error, msg)

  private def pprint(l: Level, message: String): Unit =
    val m = f"${l.name}%-5s: $message"
    if l.level <= level.level then println(f"${l.name}%-5s: $message")
    logToFile(m)

object Logger:

  private val LogFile: Path         = os.home / ".ijp_imagej_launcher.log"
  private var logFileReset: Boolean = true

  enum Level(val level: Int, val name: String):
    case Off   extends Level(0, "OFF")
    case Error extends Level(200, "ERROR")
    case Info  extends Level(400, "INFO")
    case Debug extends Level(500, "DEBUG")
    case All   extends Level(Int.MaxValue, "DEBUG")

  private def logToFile(message: String): Unit =
    try
      if logFileReset && os.exists(LogFile) then
        val _ = os.remove(LogFile)
        logFileReset = false

      val data  = readableTimeStamp() + ": " + message + "\n"
      val lines = data.getBytes()
      val _ = Files.write(
        LogFile.toNIO,
        lines,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND,
        StandardOpenOption.WRITE
      )
    catch
      case NonFatal(_) =>
      // Ignore exceptions

  private def readableTimeStamp(): String =
    // Simple implementation returns GMT time (no date)
    // Use custom code as DateTimeFormatter is not available in Scala Native 0.4.14
    val t   = System.currentTimeMillis()
    val sss = t                               % 1000
    val ss  = (t / 1000L).toInt               % 60
    val mm  = (t / (1000L * 60L)).toInt       % 60
    val hh  = (t / (1000L * 60L * 60L)).toInt % 24
    f"$hh%02d-$mm%02d-$ss%02d_$sss%03dZ"
