package model

final case class AlertTermMatchResult(
    alertId: AlertId,
    termIds: Set[QueryTermId]
)
