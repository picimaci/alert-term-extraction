import service.{ AlertTermMatchService, ApiService, ConfigService, FileService }
import cats.syntax.traverse._
import com.typesafe.scalalogging.Logger

object Main extends App {
  val logger = Logger(getClass.getName)

  for {
    appConf         <- ConfigService.loadAppConfig
    queryTerms      <- ApiService.getQueryTerms(appConf.queryTermUrl, appConf.apiKey)
    alertsList      <- ApiService.getAlertsNTimes(appConf.alertUrl, appConf.apiKey, appConf.numberOfAlertsFetch)
    matchResultsList = alertsList.map(alerts => AlertTermMatchService.findMatchingTermsForAlerts(queryTerms, alerts))
    _               <- FileService.saveResults(appConf.resultFolderName, matchResultsList)
  } yield logger.info(
    s"Successfully matched alerts and query terms, results can be viewed in folder '${appConf.resultFolderName}'"
  )
}
