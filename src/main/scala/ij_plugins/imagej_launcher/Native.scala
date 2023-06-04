package ij_plugins.imagej_launcher

import os.{Path, up}

import scala.scalanative.unsafe
import scala.scalanative.unsafe.*

object Native:

  def applicationPath(): Path =
    val maxPath: CSize = argv0.path_max()
    // use Zone to manage native memory
    val exePath =
      Zone { implicit z =>
        val buffer: CString = alloc[CChar](maxPath)
        argv0.get_exe_path(buffer, maxPath)
        unsafe.fromCString(buffer)
      }
    Path(exePath) / up

  @extern
  object mem:
    def determineTotalSystemMemory(): Long = extern

  @extern
  private object argv0:
    def path_max(): CSize = extern

    def get_exe_path(exe_path: CString, size: CSize): Unit = extern
