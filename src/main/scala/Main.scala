import service.{ AlertTermMatchService, ApiService, ConfigService, FileService }
import com.typesafe.scalalogging.Logger

import util.EitherTHelper._

/**   - load application configuration containing information for calling apis and number of test runs
  *   - fetch query terms
  *   - fetch alerts the number of times defined in config
  *   - check which alerts contain which query terms
  *   - write results into files
  */
object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  val logger = Logger(getClass.getName)

  for {
    appConf         <- ConfigService.loadAppConfig.toEitherT
    queryTerms      <- ApiService.getQueryTerms(appConf.queryTermUrl, appConf.apiKey)
    alertsList      <- ApiService.getAlertsNTimes(appConf.alertUrl, appConf.apiKey, appConf.numberOfAlertsFetch)
    matchResultsList = alertsList.map(alerts => AlertTermMatchService.findMatchingTermsForAlerts(queryTerms, alerts))
    _               <- FileService.saveResults(appConf.resultFolderName, matchResultsList).toEitherT
  } yield logger.info(
    s"Successfully matched alerts and query terms, results can be viewed in folder '${appConf.resultFolderName}'"
  )
}
