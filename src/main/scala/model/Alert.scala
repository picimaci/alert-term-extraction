package model

final case class AlertId(id: String) extends AnyVal
final case class Alert(
    id: AlertId,
    contents: Seq[Content]
)
