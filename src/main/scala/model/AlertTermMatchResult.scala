package model

final case class AlertTermMatchResult(
    alertId: String,
    termIds: Set[Int]
)
