package model

final case class Alert(
    id: String,
    contents: Seq[Content],
    date: String,
    inputType: String
)
