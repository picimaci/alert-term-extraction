package service

import model.AppConf
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object ConfigService {
  def loadAppConfig: Either[String, AppConf] =
    ConfigSource.default
      .load[AppConf]
      .left
      .map(cf => cf.prettyPrint())
}
