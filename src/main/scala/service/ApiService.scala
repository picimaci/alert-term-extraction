package service

import cats.data.EitherT
import cats.syntax.traverse._
import com.typesafe.scalalogging.Logger
import io.circe.parser.decode
import model.{ Alert, QueryTerm }
import sttp.client4.DefaultFutureBackend
import sttp.client4.{ quickRequest, Response, UriContext }
import util.ErrorHandler._

import scala.concurrent.Future

object ApiService {
  private implicit val logger: Logger = Logger(getClass.getName)
  import scala.concurrent.ExecutionContext.Implicits.global

  private def sendRequest(url: String, apiKey: String): Future[Either[String, Response[String]]] = {
    val backend = DefaultFutureBackend()
    logger.info(s"Sending request to url: $url")
    quickRequest.get(uri"$url?key=$apiKey").send(backend).map(_.handleErrors("sending the API request"))
  }

  def getQueryTerms(url: String, apiKey: String): EitherT[Future, String, Seq[QueryTerm]] = {
    logger.info("Fetching query terms")
    for {
      apiResult  <- EitherT(sendRequest(url, apiKey))
      queryTerms <-
        EitherT.fromEither[Future](decode[Seq[QueryTerm]](apiResult.body).handleErrors("decoding query terms"))
    } yield {
      logger.info("Successfully fetched and parsed query terms")
      queryTerms
    }
  }

  private def getAlerts(url: String, apiKey: String): EitherT[Future, String, Seq[Alert]] =
    for {
      apiResult <- EitherT(sendRequest(url, apiKey))
      alerts    <- EitherT.fromEither[Future](decode[Seq[Alert]](apiResult.body).handleErrors("decoding alerts"))
    } yield alerts

  def getAlertsNTimes(
      url: String,
      apiKey: String,
      numberOfAlertsFetch: Int
  ): EitherT[Future, String, List[Seq[Alert]]] = {
    val alertApiIterator = (1 to numberOfAlertsFetch).toList
    logger.info(s"Alert api will be called $numberOfAlertsFetch times")
    alertApiIterator
      .traverse(_ => ApiService.getAlerts(url, apiKey))
      .map { res =>
        logger.info(s"Successfully fetched alerts $numberOfAlertsFetch times")
        res
      }
  }
}
