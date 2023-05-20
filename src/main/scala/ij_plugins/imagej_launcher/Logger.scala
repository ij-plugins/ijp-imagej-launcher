/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import ij_plugins.imagej_launcher.Logger.Level

class Logger(val level: Level = Level.Info):
  def debug(msg: String): Unit = pprint(Level.Debug, msg)
  def info(msg: String): Unit  = pprint(Level.Info, msg)
  def error(msg: String): Unit = pprint(Level.Error, msg)

  private def pprint(l: Level, message: String): Unit =
    if l.level <= level.level then println(f"${l.name}%-5s: $message")

object Logger:
  enum Level(val level: Int, val name: String):
    case Off   extends Level(0, "OFF")
    case Error extends Level(200, "ERROR")
    case Info  extends Level(400, "INFO")
    case Debug extends Level(500, "DEBUG")
    case All   extends Level(Int.MaxValue, "DEBUG")
