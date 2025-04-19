package service

import TestData._
import model.AlertTermMatchResult
import org.scalatest.freespec._

/*
  To execute text, run:
  testOnly service.AlertTermMatchServiceTest
 */
class AlertTermMatchServiceTest extends AnyFreeSpec {

  "AlertTermMatchService" - {
    "contentContainsTerm" - {
      "match if content contains term" in {
        val result = AlertTermMatchService.contentContainsTerm(contentAll, termEn)
        assert(result)
      }
      "not match if text doesn't contain term at all" in {
        val result = AlertTermMatchService.contentContainsTerm(contentAll, termEnRandom)
        assert(!result)
      }
      "not match if text doesn't contain all parts of term" in {
        val result = AlertTermMatchService.contentContainsTerm(contentPartial, termEn)
        assert(!result)
      }
      "not match if term is keep order and text contains all parts but not consecutively" in {
        val result = AlertTermMatchService.contentContainsTerm(contentWrongOrder, termEnKo)
        assert(!result)
      }
      "match if term is not keep order and text contains all parts but not consecutively" in {
        val result = AlertTermMatchService.contentContainsTerm(contentWrongOrder, termEn)
        assert(result)
      }
      "match if term is keep order and text contains all parts consecutively" in {
        val result = AlertTermMatchService.contentContainsTerm(contentAll, termEnKo)
        assert(result)
      }
    }
    "findContainedTermIds" - {
      "return matching term ids" in {
        val result = AlertTermMatchService.findContainedTermIds(contentAll, allTerms)
        assert(result.size === 2)
        assert(Seq(termEn.id, termEnKo.id).forall(result.contains(_)))
      }
      "return no match when content and term languages don't match" in {
        val result = AlertTermMatchService.findContainedTermIds(contentFr, allTerms)
        assert(result.isEmpty)
      }
    }
    "findMatchingTermsForAlerts" - {
      "return alert id - term id matches for all alert inputs" in {
        val result = AlertTermMatchService.findMatchingTermsForAlerts(allTerms, allAlerts)
        assert(result.size === 3)
        assert(result.contains(AlertTermMatchResult(alertWithSomeContent.id, Set(termEn.id))))
        assert(result.contains(AlertTermMatchResult(alertWithAllContent.id, Set(termEn.id, termEnKo.id))))
        assert(result.contains(AlertTermMatchResult(alertWithDuplicate.id, Set(termEn.id, termEnKo.id))))
      }
      "return no duplicate term id per alert even if multiple matches present" in {
        val result = AlertTermMatchService.findMatchingTermsForAlerts(allTerms, Seq(alertWithDuplicate))
        assert(result.size === 1)
        assert(result.contains(AlertTermMatchResult(alertWithDuplicate.id, Set(termEn.id, termEnKo.id))))
      }
      "return nothing when content type is not text" in {
        val result = AlertTermMatchService.findMatchingTermsForAlerts(allTerms, Seq(alertForNotText))
        assert(result.isEmpty)
      }
      "not include alert when no matches resulted from findContainedTermIds for it" in {
        val result = AlertTermMatchService.findMatchingTermsForAlerts(allTerms, Seq(alertNoMatch))
        assert(result.isEmpty)
      }
    }
  }
}
