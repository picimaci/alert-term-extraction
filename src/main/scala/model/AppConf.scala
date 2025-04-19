package model

final case class AppConf(
    apiKey: String,
    alertUrl: String,
    queryTermUrl: String,
    numberOfAlertsFetch: Int
)
