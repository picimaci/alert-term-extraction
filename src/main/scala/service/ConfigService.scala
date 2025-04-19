package service

import com.typesafe.scalalogging.Logger
import model.AppConf
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import util.ErrorHandler._

object ConfigService {
  private implicit val logger: Logger = Logger(getClass.getName)

  def loadAppConfig: Either[String, AppConf] =
    ConfigSource.default
      .load[AppConf]
      .left
      .map(_.prettyPrint())
      .handleErrors("reading application config file")
}
