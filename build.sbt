scalaVersion := "3.2.2"

enablePlugins(ScalaNativePlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

libraryDependencies ++= Seq(
  "com.github.scopt" %%% "scopt" % "4.1.0"
)

Compile/run/mainClass := Some("ij_plugins.imagej_launcher.Main")

// import to add Scala Native options
import scala.scalanative.build._


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
