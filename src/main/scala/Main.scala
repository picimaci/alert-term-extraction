import service.{ AlertTermMatchService, ApiService, ConfigService, FileService }
import cats.syntax.traverse._

object Main extends App {
  for {
    appConf         <- ConfigService.loadAppConfig
    queryTerms      <- ApiService.getQueryTerms(appConf.queryTermUrl, appConf.apiKey)
    alertApiIterator = (1 to appConf.numberOfAlertsFetch).toList
    alertsList      <- alertApiIterator.traverse(_ => ApiService.getAlerts(appConf.alertUrl, appConf.apiKey))
    matchResultsList = alertsList.map(alerts => AlertTermMatchService.findMatchingTermsForAlerts(queryTerms, alerts))
  } yield FileService.saveResults(appConf.resultFolderName, matchResultsList)
}
