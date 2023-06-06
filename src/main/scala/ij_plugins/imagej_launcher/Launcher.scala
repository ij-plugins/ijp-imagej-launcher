/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import ij_plugins.imagej_launcher.IJDir.jarsDirName
import ij_plugins.imagej_launcher.Launcher.javaExeFileName
import ij_plugins.imagej_launcher.Main.Config
import os.Path

import java.io.File
import java.lang.ProcessBuilder.Redirect

class Launcher(using logger: Logger):

  def run(config: Config): Unit =
    prepareLaunch(config) match
      case Right(commandLine) =>
        if config.dryRun then
          logger.debug("dry-run")
          println(commandLine.mkString(" "))
        else
          launch(commandLine)
      case Left(errorMessage) =>
        logger.error(errorMessage)

  private def prepareLaunch(config: Config): Either[String, Seq[String]] =
    for
      ijDir       <- IJDir.locate(config, logger)
      _           <- Updater.update(ijDir, config.dryRun, logger)
      ijConfig    <- IJConfigFile.readFromDir(ijDir)
      launcherJar <- findImageJLauncherJar(ijDir.toIO)
      javaExe     <- locateJavaExecutable(config, ijDir.toIO)
      systemType  <- determineSystemType()
    yield
      val maxMemoryMB = determineMaxMemoryMB()
      logger.info(s"Max memory to use: ${maxMemoryMB}MB")
      buildCommandLine(ijDir.toIO, javaExe, launcherJar, systemType, maxMemoryMB)

  private def findImageJLauncherJar(ijDir: File): Either[String, File] =
    logger.debug("Looking for 'imagej-launcher*.jar'")
    val jarsDir = new File(ijDir, jarsDirName)
    if jarsDir.exists() then
      if jarsDir.isDirectory then
        // Locate launcher
        jarsDir
          .listFiles()
          .find { f =>
            val name = f.getName
            name.startsWith("imagej-launcher") && name.endsWith(".jar")
          } match
          case Some(f) =>
            logger.info(s"  Found launcher jar: '$f'")
            Right(f)
          case None =>
            Left(s"Cannot find 'imagej-launcher*.jar' in '$jarsDir'")
      else
        Left(s"'$jarsDir' is not a directory'")
    else
      Left(s"Cannot find subdirectory '$jarsDirName' [$jarsDir]")

  private def locateJavaExecutable(config: Config, ijDir: File): Either[String, File] =
    logger.debug("Looking for Java executable")

    val javaHomeE: Either[String, File] = config.javaHome match
      case Some(d) =>
        logger.debug(s"  Setting java-home to provided: '$d'")
        Right(d)
      case None =>
        locateJavaHomeIn(new File(ijDir, "java")) match
          case Some(d) =>
            logger.debug(s"  Located java dir in ImageJ home")
            Right(d)
          case None =>
            Option(System.getenv("JAVA_HOME")) match
              case Some(d) =>
                logger.debug("  Using JAVA_HOME environment variable")
                Right(new File(d).getAbsoluteFile)
              case None =>
                Left("Unable to determine Java home")

    javaHomeE.flatMap(javaHome =>
      logger.info(s"  Java home set to: '$javaHome'")
      logger.debug(s"  Looking for java executable bin/$javaExeFileName")
      val javaExe = new File(javaHome, s"bin/$javaExeFileName")
      if javaExe.exists() then
        logger.info(s"  Found '$javaExeFileName' in: '$javaHome/bin'")
        Right(javaExe)
      else
        Left("cannot find 'java.exe' in 'path'")
    )

  private def locateJavaHomeIn(dir: File): Option[File] =
    if dir.exists() && dir.isDirectory then
      val base                  = os.Path(dir.toPath)
      val candidates: Seq[Path] = os.walk(path = base)
      val c2 = candidates.filter(p =>
        p.toIO.isFile &&
          p.toIO.getName == javaExeFileName &&
          p.toIO.getParentFile.getName == "bin"
      )
      logger.debug("  Candidates: " + c2.mkString(", "))
      c2.map(_.toIO.getParentFile.getParentFile)
        .headOption
    else
      None

  private def determineSystemType(): Either[String, String] =
    logger.debug("Determining system type")
    val osName = System.getProperty("os.name")
    val osArch = System.getProperty("os.arch")
    logger.debug("  os.name: " + osName)
    logger.debug("  os.arch: " + osArch)

    val notSupportedError = s"$osName $osArch not supported"

    val r =
      if osName.toLowerCase.contains("windows") then
        if osArch.toLowerCase.contains("64") then Right("win64")
        else Left(notSupportedError)
      else if osName.toLowerCase.contains("mac os x") then Right("macosx")
      else if osName.toLowerCase.contains("linux") then
        if osArch.toLowerCase.contains("64") then Right("linux64")
        else Left(notSupportedError)
      else
        Left(notSupportedError)

    r.foreach(s => logger.info(s"  System type set to: '$s'"))
    r

  private def determineMaxMemoryMB(): Long =
    logger.debug("Determine memory to use")
    val sysMax = Native.mem.determineTotalSystemMemory()
    logger.debug(s"  Available RAM: ${sysMax / 1024 / 1024}MB")
    val ijMaxMemMB = Math.round(sysMax * 0.75 / 1024 / 1024)
    logger.debug(s"  Using 3/4 of that: ${ijMaxMemMB}MB")
    ijMaxMemMB

  private def buildCommandLine(
    ijDir: File,
    javaExe: File,
    launcherJar: File,
    systemType: String,
    maxMemoryMB: Long
  ): Seq[String] =
    val ijDirPath = ijDir.getAbsolutePath

    Seq(
      javaExe.getAbsolutePath,
      "--add-opens",
      "java.base/java.lang=ALL-UNNAMED",
      "--add-opens",
      "java.base/java.util=ALL-UNNAMED",
      "--add-opens",
      "java.desktop/sun.awt=ALL-UNNAMED",
      "--add-opens",
      "java.desktop/sun.awt.X11=ALL-UNNAMED",
      "--add-opens",
      "java.desktop/com.apple.eawt=ALL-UNNAMED",
      "-Dpython.cachedir.skip=true",
      s"-Dplugins.dir=$ijDirPath",
      s"-Xmx${maxMemoryMB}m",
      "-Dimagej.splash=true",
      s"-Djava.class.path=${launcherJar.getAbsolutePath}",
      s"-Dimagej.dir=$ijDirPath",
      s"-Dij.dir=$ijDirPath",
      s"-Dfiji.dir=$ijDirPath",
      s"-Dfiji.executable=$ijDirPath/ImageJ-$systemType.exe",
      s"-Dij.executable=$ijDirPath/ImageJ-$systemType.exe",
      s"-Djava.library.path=$ijDirPath/lib/$systemType;$ijDirPath/mm/$systemType",
      "-Dscijava.context.strict=false",
      "-Dpython.console.encoding=UTF-8",
      "net.imagej.launcher.ClassLauncher",
      "-ijjarpath",
      "jars",
      "-ijjarpath",
      "plugins",
      "net.imagej.Main"
    )
  end buildCommandLine

  private def launch(command: Seq[String]): Unit =
    logger.debug("launchImageJ ...")
    logger.debug(s"  Executing command:\n" + command.mkString(" "))

    val builder = new ProcessBuilder(command*)
    builder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    builder.redirectError(ProcessBuilder.Redirect.INHERIT)

    builder.start()
end Launcher

object Launcher:
  lazy val javaExeFileName: String = if Utils.isWindows then "java.exe" else "java"
