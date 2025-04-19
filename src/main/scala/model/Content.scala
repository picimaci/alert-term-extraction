package model

import io.circe.Decoder

final case class Content(
    text: String,
    contentType: String,
    language: String
)

object Content {
  implicit val decoder: Decoder[Content] = Decoder.instance { h =>
    for {
      text        <- h.get[String]("text")
      contentType <- h.get[String]("type")
      language    <- h.get[String]("language")
    } yield Content(text, contentType, language)
  }
}
