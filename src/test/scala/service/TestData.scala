package service

import model.{ Alert, AlertId, Content, QueryTerm, QueryTermId }

object TestData {
  val termEn: QueryTerm        = QueryTerm(id = QueryTermId(1), text = "IG Metall", language = "en", keepOrder = false)
  val termEnKo: QueryTerm      = QueryTerm(id = QueryTermId(2), text = "IG Metall", language = "en", keepOrder = true)
  val termDe: QueryTerm        = QueryTerm(id = QueryTermId(3), text = "IG Metall", language = "de", keepOrder = false)
  val termEnRandom: QueryTerm  = QueryTerm(id = QueryTermId(4), text = "Cica mica", language = "en", keepOrder = false)
  val allTerms: Seq[QueryTerm] = Seq(termEn, termEnKo, termDe, termEnRandom)

  val contentAll: Content        = Content(text = "This contains iG meTall", contentType = "text", language = "en")
  val contentPartial: Content    = Content(text = "This contains meTall", contentType = "text", language = "en")
  val contentWrongOrder: Content = Content(text = "This meTall contains iG", contentType = "text", language = "en")
  val contentFr: Content         = Content(text = "This contains iG meTall", contentType = "text", language = "fr")
  val contentNotText: Content    = Content(text = "This contains iG meTall", contentType = "other", language = "en")
  val allContents: Seq[Content]  = Seq(contentAll, contentPartial, contentWrongOrder, contentFr, contentNotText)

  val alertWithSomeContent: Alert = Alert(id = AlertId("alert-id1"), contents = Seq(contentWrongOrder, contentFr))
  val alertWithAllContent: Alert  = Alert(id = AlertId("alert-id2"), contents = allContents)
  val alertWithDuplicate: Alert   = Alert(id = AlertId("alert-id3"), contents = Seq(contentAll, contentAll))
  val alertForNotText: Alert      = Alert(id = AlertId("alert-id4"), contents = Seq(contentNotText))
  val alertNoMatch: Alert         = Alert(id = AlertId("alert-id5"), contents = Seq(contentFr))
  val allAlerts: Seq[Alert]       =
    Seq(alertWithSomeContent, alertWithAllContent, alertWithDuplicate, alertForNotText, alertNoMatch)
}
