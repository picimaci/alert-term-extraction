package model

import io.circe.Decoder

final case class QueryTermId(id: Int) extends AnyVal

final case class QueryTerm(
    id: QueryTermId,
    text: String,
    language: String,
    keepOrder: Boolean
)

object QueryTerm {
  implicit val decoder: Decoder[QueryTerm] = Decoder.instance { h =>
    for {
      id        <- h.get[Int]("id")
      text      <- h.get[String]("text")
      language  <- h.get[String]("language")
      keepOrder <- h.get[Boolean]("keepOrder")
    } yield QueryTerm(QueryTermId(id), text, language, keepOrder)
  }
}
