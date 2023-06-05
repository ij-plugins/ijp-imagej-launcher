/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.nio.file.FileSystems
import scala.scalanative.runtime.Platform.isWindows

class PathSpec extends AnyFlatSpec with should.Matchers:

  "A Path" should "should `relativize` jars on Windows (Scala Native #3293)" in {
    if isWindows() then
      // This to test that fix for Scala Native #3293 implemented, it should be part of Scala Native 0.4.13
      // See https://github.com/scala-native/scala-native/issues/3293

      //    val src = Path.of("C:\\a\\b\\c.jar")
      val src = FileSystems.getDefault.getPath("C:\\a\\b\\c.jar")
      //    val base = Path.of("C:\\a")
      val base = FileSystems.getDefault.getPath("C:\\a")

      val rel = base.relativize(src)
      rel.toString should be("b\\c.jar")
  }
