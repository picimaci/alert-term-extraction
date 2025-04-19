package model

import io.circe.Decoder

final case class AlertId(id: String) extends AnyVal

final case class Alert(
    id: AlertId,
    contents: Seq[Content]
)

object Alert {
  implicit val decoder: Decoder[Alert] = Decoder.instance { h =>
    for {
      id       <- h.get[String]("id")
      contents <- h.get[Seq[Content]]("contents")
    } yield Alert(AlertId(id), contents)
  }
}
