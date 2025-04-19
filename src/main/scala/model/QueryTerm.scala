package model

final case class QueryTermId(id: Int) extends AnyVal
final case class QueryTerm(
    id: QueryTermId,
    text: String,
    language: String,
    keepOrder: Boolean
)