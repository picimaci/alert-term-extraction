package service

import cats.syntax.traverse._
import com.typesafe.scalalogging.Logger
import io.circe.parser.decode
import model.{ Alert, QueryTerm }
import sttp.client4.{ quickRequest, Response, UriContext }
import sttp.client4.quick.RichRequest
import util.ErrorHandler._

object ApiService {
  private implicit val logger: Logger = Logger(getClass.getName)

  private def sendRequest(url: String, apiKey: String): Either[String, Response[String]] = {
    logger.info(s"Sending request to url: $url")
    quickRequest.get(uri"$url?key=$apiKey").send().handleErrors("sending the API request")
  }

  def getQueryTerms(url: String, apiKey: String): Either[String, Seq[QueryTerm]] = {
    logger.info("Fetching query terms")
    for {
      apiResult  <- sendRequest(url, apiKey)
      queryTerms <- decode[Seq[QueryTerm]](apiResult.body).handleErrors("decoding query terms")
    } yield {
      logger.info("Successfully fetched and parsed query terms")
      queryTerms
    }
  }

  private def getAlerts(url: String, apiKey: String): Either[String, Seq[Alert]] =
    for {
      apiResult <- sendRequest(url, apiKey)
      alerts    <- decode[Seq[Alert]](apiResult.body).handleErrors("decoding alerts")
    } yield alerts

  def getAlertsNTimes(url: String, apiKey: String, numberOfAlertsFetch: Int): Either[String, List[Seq[Alert]]] = {
    val alertApiIterator = (1 to numberOfAlertsFetch).toList
    logger.info(s"Alert api will be called $numberOfAlertsFetch times")
    alertApiIterator.traverse(_ => ApiService.getAlerts(url, apiKey)).handleErrors("fetching alerts").map { res =>
      logger.info(s"Successfully fetched alerts $numberOfAlertsFetch times")
      res
    }
  }
}
