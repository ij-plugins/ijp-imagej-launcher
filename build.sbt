scalaVersion := "3.3.0"
//name                 := "IJP-ImageJ-Launcher"
version              := "0.1.1-SNAPSHOT"
versionScheme        := Some("early-semver")
organization         := "net.sf.ij-plugins"
homepage             := Some(new URI("https://github.com/ij-plugins/ijp-imagej-launcher").toURL)
startYear            := Some(2023)
ThisBuild / licenses := Seq(("Apache-2", new URI("https://opensource.org/license/apache-2-0").toURL))
ThisBuild / developers := List(
  Developer(id = "jpsacha", name = "Jarek Sacha", email = "jpsacha@gmail.com", url = url("https://github.com/jpsacha"))
)

enablePlugins(ScalaNativePlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

libraryDependencies ++= Seq(
  "com.github.scopt" %%% "scopt"     % "4.1.0",
  "com.lihaoyi"      %%% "os-lib"    % "0.9.1",
  "org.scalatest"    %%% "scalatest" % "3.2.16" % Test
)

Compile / run / mainClass := Some("ij_plugins.imagej_launcher.Main")

// import to add Scala Native options
import scala.scalanative.build.*

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none)     // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix)     // commix
}

//// Enable verbose reporting during compilation
//nativeConfig ~= { c =>
//  c.withCompileOptions(c.compileOptions ++ Seq("-v"))
//}
