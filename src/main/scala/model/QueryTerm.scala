package model

final case class QueryTerm(
    id: Int,
    target: Int,
    text: String,
    language: String,
    keepOrder: Boolean
)