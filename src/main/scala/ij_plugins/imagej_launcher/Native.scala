package ij_plugins.imagej_launcher

import scala.scalanative.unsafe.extern

object Native:

  @extern
  object mem:
    def determineTotalSystemMemory(): Long = extern
