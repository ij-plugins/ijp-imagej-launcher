/*
 * Copyright (c) 2000-2025 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import java.io.File

object Main:
  private var logger         = new Logger()
  private val AppName        = "IJP-ImageJ-Launcher"
  private val AppVersion     = BuildInfo.version
  private val VersionMessage = s"v.$AppVersion"
  private val AppDescription = "Native launcher for ImageJ2."

  def main(args: Array[String]): Unit =
    setupLogger(Logger.Level.All)

    parseCommandLine(args) match
      case Some(config) =>
        setupLogger(config.logLevel)
        runLauncher(config)
      case None =>

  private def parseCommandLine(args: Array[String]): Option[Config] =
    logger.debug("Command line: " + args.map("'" + _ + "'").mkString(", "))

    val appName = s"$AppName $VersionMessage"
    mainargs.ParserForClass[CLConfig].constructEither(
      args.toSeq,
      customName = appName,
      customDoc =
        s"""
           |$AppDescription
           | 
           |Usage: $AppName [options]
           | 
           |""".stripMargin,
      docsOnNewLine = true,
      sorted = false
    ) match
      case Right(cl) =>
        if cl.version.value then
          println(s"$AppName $VersionMessage")
          None
        else
          var c = Config()
          if cl.dryRun.value then c = c.copy(dryRun = true)
          if cl.info.value then c = c.copy(logLevel = Logger.Level.Info)
          if cl.debug.value then c = c.copy(logLevel = Logger.Level.Debug)
          cl.javaHome.foreach(s => c = c.copy(javaHome = Option(new File(s))))
          cl.ijDir.foreach(s => c = c.copy(ijDir = Option(new File(s))))
          Option(c)
      case Left(error) =>
        println(error)
        System.exit(-1)
        None
  end parseCommandLine

  private def setupLogger(logLevel: Logger.Level): Unit = logger = new Logger(logLevel)

  private def runLauncher(config: Config): Unit = new Launcher(using logger).run(config)

  case class Config(
    logLevel: Logger.Level = Logger.Level.Error,
    dryRun: Boolean = false,
    javaHome: Option[File] = None,
    ijDir: Option[File] = None
  )

  @mainargs.main(name = "<NAME>", doc = "<DOC>")
  private case class CLConfig(
    @mainargs.arg(doc = "prints this usage text")
    help: mainargs.Flag = mainargs.Flag(false),
    @mainargs.arg(doc = "prints version")
    version: mainargs.Flag = mainargs.Flag(false),
    @mainargs.arg(doc = "show the command line, but do not run anything")
    dryRun: mainargs.Flag = mainargs.Flag(false),
    @mainargs.arg(doc = "informational output")
    info: mainargs.Flag = mainargs.Flag(false),
    @mainargs.arg(doc = "verbose output")
    debug: mainargs.Flag = mainargs.Flag(false),
    @mainargs.arg(doc = "specify JAVA_HOME explicitly")
    javaHome: Option[String] = None,
    @mainargs.arg(doc = "set the ImageJ directory to <path> (used to find jars/, plugins/ and macros/)")
    ijDir: Option[String] = None
  )
end Main
