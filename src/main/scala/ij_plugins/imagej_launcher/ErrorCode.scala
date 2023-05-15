/*
 * Copyright (c) 2000-2023 Jarek Sacha. All Rights Reserved.
 * Author's e-mail: jpsacha at gmail.com
 */
package ij_plugins.imagej_launcher

import scala.collection.immutable

enum ErrorCode(val value: Int, val message: String):
  case OK                          extends ErrorCode(0, "OK")
  case InvalidCommandLineArguments extends ErrorCode(-10, "Invalid command line arguments")
  case GeneralError                extends ErrorCode(-100, "General error")
  case UnhandledNonFatalError      extends ErrorCode(-200, "Unhandled non-fatal error")
  case UnhandledFatalError         extends ErrorCode(-300, "Unhandled fatal error")
  case NotImplemented              extends ErrorCode(-400, "Functionality not implemented")

  override def toString: String = s"$message [exit code: $value]"
