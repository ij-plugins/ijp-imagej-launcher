/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import ij_plugins.imagej_launcher.ErrorCode
import scopt.{DefaultOEffectSetup, OParser}

import java.io.File

object Main {
  private val logger  = new Logger()
  private val AppName = "ijp-imagej-launcher"
  //  private val AppVersion = s"${Version.version} [${Version.buildTimeStr}]"
  private val AppVersion     = s"0.1.0"
  private val VersionMessage = s"v.$AppVersion"
  private val AppDescription =
    """Native launcher for ImageJ2
      |""".stripMargin

  case class Config(
    logLevel: Logger.Level = Logger.Level.Error,
    dryRun: Boolean = false,
    javaHome: Option[File] = None
  )

  def main(args: Array[String]): Unit =
    setupLogger(Logger.Level.All)

    val ret: ErrorCode =
      parseCommandLine(args) match
        case Some(config) =>
          setupLogger(config.logLevel)
          runLauncher(config)
        case None =>
          // arguments are bad
          ErrorCode.InvalidCommandLineArguments

    val msg = s"${ret.message} [exit code: ${ret.value}]"
    if ret == ErrorCode.OK then
      logger.info(msg)
    else
      logger.error(msg)

//    System.exit(ret.value)
  end main

  private def parseCommandLine(args: Array[String]): Option[Config] =
    val builder = OParser.builder[Config]
    val parser1 = {
      import builder.*
      OParser.sequence(
        programName(AppName),
        head(AppName, VersionMessage),
        note(AppDescription),
        //
        help('h', "help").text("prints this usage text"),
        //
        version("version").text("prints version"),
        //
        opt[Unit]("dry-run")
          .action((_, c) => c.copy(dryRun = true))
          .text("show the command line, but do not run anything"),
        //
        opt[Unit]("info")
          .action((_, c) => c.copy(logLevel = Logger.Level.Info))
          .text("informational output"),
        //
        opt[Unit]("debug")
          .action((_, c) => c.copy(logLevel = Logger.Level.Debug))
          .text("verbose output"),
        //
        opt[File]("java-home")
          .valueName("<path>")
          .action((path, c) => c.copy(javaHome = Option(path)))
          .text("specify JAVA_HOME explicitly"),
        //
        opt[Unit]("print-java-home")
          .action((_, c) => c.copy(dryRun = true))
          .text("print ImageJ's idea of JAVA_HOME")
      )
    }

    OParser.parse(parser1, args, Config())
  end parseCommandLine

  private def setupLogger(logLevel: Logger.Level): Unit = logger.level = logLevel

  private def runLauncher(config: Config): ErrorCode = new Launcher(logger).run(config)
}
