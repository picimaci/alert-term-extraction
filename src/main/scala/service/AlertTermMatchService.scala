package service

import model.{ Alert, AlertTermMatchResult, Content, QueryTerm, QueryTermId }

object AlertTermMatchService {

  private def lowerCaseSplitText(text: String): Array[String] = text.toLowerCase.split("\\s+")

  def contentContainsTerm(content: Content, term: QueryTerm): Boolean = {
    val contentParts = lowerCaseSplitText(content.text)
    val termParts    = lowerCaseSplitText(term.text)
    if (term.keepOrder) {
      contentParts.containsSlice(termParts)
    } else {
      termParts.forall(contentParts.contains(_))
    }
  }

  def findContainedTermIds(content: Content, terms: Seq[QueryTerm]): Seq[QueryTermId] =
    terms
      .filter(_.language == content.language)
      .filter(term => contentContainsTerm(content, term))
      .map(_.id)

  def findMatchingTermsForAlerts(terms: Seq[QueryTerm], alerts: Seq[Alert]): Seq[AlertTermMatchResult] =
    alerts
      .map { alert =>
        val containedTermIds = alert.contents
          .filter(_.`type` == "text")
          .flatMap(content => findContainedTermIds(content, terms))
          .toSet
        AlertTermMatchResult(alertId = alert.id, termIds = containedTermIds)
      }
      .filter(_.termIds.nonEmpty)
}
