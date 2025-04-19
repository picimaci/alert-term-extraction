import service.{ AlertTermMatchService, ApiService, ConfigService, FileService }
import cats.syntax.traverse._
import com.typesafe.scalalogging.Logger

object Main extends App {
  val logger = Logger(getClass.getName)

  for {
    appConf         <- ConfigService.loadAppConfig
    queryTerms      <- ApiService.getQueryTerms(appConf.queryTermUrl, appConf.apiKey)
    alertApiIterator = (1 to appConf.numberOfAlertsFetch).toList
    _                = logger.info(s"Alert api will be called ${appConf.numberOfAlertsFetch} times as per the app configuration")
    alertsList      <- alertApiIterator.traverse(_ => ApiService.getAlerts(appConf.alertUrl, appConf.apiKey))
    matchResultsList = alertsList.map(alerts => AlertTermMatchService.findMatchingTermsForAlerts(queryTerms, alerts))
    _               <- FileService.saveResults(appConf.resultFolderName, matchResultsList)
  } yield logger.info(
    s"Successfully matched alerts and query terms, results can be viewed in folder '${appConf.resultFolderName}'"
  )
}
