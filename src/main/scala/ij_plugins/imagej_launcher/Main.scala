/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import scopt.OParser

import java.io.File

object Main:
  private var logger         = new Logger()
  private val AppName        = "IJP-ImageJ-Launcher"
  private val AppVersion     = BuildInfo.version
  private val VersionMessage = s"v.$AppVersion"
  private val AppDescription =
    """Native launcher for ImageJ2
      |""".stripMargin

  def main(args: Array[String]): Unit =
    setupLogger(Logger.Level.All)

    parseCommandLine(args) match
      case Some(config) =>
        setupLogger(config.logLevel)
        runLauncher(config)
      case None =>

  private def parseCommandLine(args: Array[String]): Option[Config] =
    logger.debug("Command line: " + args.map("'" + _ + "'").mkString(", "))
    val builder = OParser.builder[Config]
    val parser1 =
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
        opt[File]("ij-dir")
          .valueName("<path>")
          .action((path, c) => c.copy(ijDir = Option(path)))
          .text("set the ImageJ directory to <path> (used to find jars/, plugins/ and macros/)")
      )

    OParser.parse(parser1, args, Config())
  end parseCommandLine

  private def setupLogger(logLevel: Logger.Level): Unit = logger = new Logger(logLevel)

  private def runLauncher(config: Config): Unit = new Launcher(using logger).run(config)

  case class Config(
    logLevel: Logger.Level = Logger.Level.Error,
    dryRun: Boolean = false,
    javaHome: Option[File] = None,
    ijDir: Option[File] = None
  )
end Main
