package com.github.nyukhalov.akkaactorperrequest

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

trait AppLogger {
  def logger: Logger = Logger(LoggerFactory.getLogger(AppLogger.this.getClass.getSimpleName))
}
