package util

import cats.data.EitherT

import scala.concurrent.{ ExecutionContext, Future }

object EitherTHelper {
  implicit class EitherTWrapper[T](block: => Either[String, T])(implicit ec: ExecutionContext) {
    def toEitherT: EitherT[Future, String, T] =
      EitherT.fromEither[Future](block)
  }
}
