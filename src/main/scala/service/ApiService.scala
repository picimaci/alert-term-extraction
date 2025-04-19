package service

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

  def getAlerts(url: String, apiKey: String): Either[String, Seq[Alert]] = {
    logger.info("Fetching alerts")
    for {
      apiResult <- sendRequest(url, apiKey)
      alerts    <- decode[Seq[Alert]](apiResult.body).handleErrors("decoding alerts")
    } yield {
      logger.info("Successfully fetched and parsed alerts")
      alerts
    }
  }
}
