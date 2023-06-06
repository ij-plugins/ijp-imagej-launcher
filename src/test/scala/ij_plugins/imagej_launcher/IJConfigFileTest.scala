/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */

package ij_plugins.imagej_launcher

import org.scalatest.flatspec.AnyFlatSpec

class IJConfigFileTest extends AnyFlatSpec:

  given logger:Logger = new Logger(Logger.Level.All)

  "IJConfigFile" should "read valid config files" in:
    val path = os.pwd / "test" / "data" / "config_valid" / "ImageJ.cfg"

    IJConfigFile.readFromFile(path) match
      case Right(cfg) =>
        logger.debug(cfg.toString)
        assert(cfg.nonEmpty)
      case Left(err) =>
        logger.debug(s"Unexpected error: $err")
        fail()

  it should "fail reading invalid config files" in:
    val path = os.pwd / "test" / "data" / "config_invalid" / "ImageJ.cfg"

    IJConfigFile.readFromFile(path) match
      case Right(cfg) =>
        logger.debug(cfg.toString)
        fail(s"Expected to return errors when reading config, but read config as: $cfg")
      case Left(err) =>
        logger.debug(s"Expected error\n:$err")
