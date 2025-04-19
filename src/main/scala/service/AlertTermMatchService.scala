package service

import model.{ Alert, AlertTermMatchResult, Content, QueryTerm, QueryTermId }

object AlertTermMatchService {

  // Convert string to lower case and split by whitespace characters
  private def lowerCaseSplitText(text: String): Array[String] = text.toLowerCase.split("\\s+")

  /*
    Create a normalized list of words for both the content and the query term
      -> normalized as in lower case and split by whitespaces, see #lowerCaseSplitText

    If keepOrder on the query term is true
      - the content wordlist must contain the term wordlist in the exact order defined on the term
    If keepOrder on the query term is false
      - the content wordlist must contain all the elements in the term wordlist, the order is irrelevant

    Return whether content contains query term
   */
  def contentContainsTerm(content: Content, term: QueryTerm): Boolean = {
    val contentParts = lowerCaseSplitText(content.text)
    val termParts    = lowerCaseSplitText(term.text)
    if (term.keepOrder) {
      contentParts.containsSlice(termParts)
    } else {
      termParts.forall(contentParts.contains(_))
    }
  }

  /*
    Only check terms that are in the same language as the content
    Filter query terms using #contentContainsTerm to find terms included in the content

    Returns ids of query terms that the content contained
   */
  def findContainedTermIds(content: Content, terms: Seq[QueryTerm]): Seq[QueryTermId] =
    terms
      .filter(_.language == content.language)
      .filter(term => contentContainsTerm(content, term))
      .map(_.id)

  /*
    Only check matches in contents that have the content type 'text'
    Get the ids of the query terms that are contained in the alerts' contents by calling #findContainedTermIds
    Convert term id list to set in order to remove duplicates
      -> happens when a query term is contained in multiple contents of an alert
    Remove results where no matching query term was found

    Returns match results, which include an alert id and the query term ids that were included in the alert's contents
   */
  def findMatchingTermsForAlerts(terms: Seq[QueryTerm], alerts: Seq[Alert]): Seq[AlertTermMatchResult] =
    alerts
      .map { alert =>
        val containedTermIds = alert.contents
          .filter(_.contentType == "text")
          .flatMap(content => findContainedTermIds(content, terms))
          .toSet
        AlertTermMatchResult(alertId = alert.id, termIds = containedTermIds)
      }
      .filter(_.termIds.nonEmpty)
}
