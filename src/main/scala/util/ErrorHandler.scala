package util

import com.typesafe.scalalogging.Logger
import sttp.client4.Response

import scala.util.Try

object ErrorHandler {
  // Log error in case of left
  implicit class EitherErrorHandlingOps[T](block: => Either[Serializable, T])(implicit logger: Logger) {
    def handleErrors(context: String): Either[String, T] =
      block.left.map { error =>
        logger.error(s"An error occurred while $context: $error")
        s"An error occurred: $error"
      }
  }

  // Apart from catching exceptions, handle non-success api responses
  implicit class ApiCallErrorHandlingOps(block: => Response[String])(implicit logger: Logger) {
    def handleErrors(context: String): Either[String, Response[String]] =
      handleAndWrapException(block, context, logger) match {
        case Right(response) =>
          if (!response.code.isSuccess) {
            logger.error(s"When $context, the response received was not success, status code: ${response.code}")
            Left(s"Response received was not success, status code: ${response.code}")
          } else Right(response)
        case x               => x
      }
  }

  implicit class ExceptionHandlingOps[T](block: => T)(implicit logger: Logger) {
    def handleErrors(context: String): Either[String, T] =
      handleAndWrapException(block, context, logger)
  }

  // Catch exception, wrap result in either and log error
  private def handleAndWrapException[T](block: => T, context: String, logger: Logger): Either[String, T] =
    Try(block).toEither.left.map { e =>
      logger.error(s"An exception was caught while $context: ${e.toString}", e)
      s"An exception was caught: ${e.getMessage}"
    }
}
