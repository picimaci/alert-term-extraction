package service

import io.circe.Error
import io.circe.parser.decode
import model.{Alert, QueryTerm}
import sttp.client4.{Response, UriContext, quickRequest}
import sttp.client4.quick.RichRequest

object ApiService {
  private def sendRequest(url: String, apiKey: String): Response[String] =
    quickRequest.get(uri"$url?key=$apiKey").send()

  def getQueryTerms(url: String, apiKey: String): Either[Error, Seq[QueryTerm]] = {
    val response = sendRequest(url, apiKey)
    decode[Seq[QueryTerm]](response.body)
  }

  def getAlerts(url: String, apiKey: String): Either[Error, Seq[Alert]] = {
    val response = sendRequest(url, apiKey)
    decode[Seq[Alert]](response.body)
  }
}
