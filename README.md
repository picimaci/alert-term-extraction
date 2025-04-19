# Alert Term Extraction

One run of the application will
* download the query terms once
* download a list of alerts the number of times defined in the configuration
* for each list of alerts
    * determine which alerts contained which query terms
    * save these results into `extraction-results` folder into separate files per alert list in `json` format

## Usage

Copy `appication-default.conf` contents into `application.conf` and fill with the relevant data:
* `alert-url`: url to fetch alerts from
* `query-term-url`: url to fetch query terms from
* `api-key`: api key to use above urls with
* `number-of-alerts-fetch`: number of iterations of fetching alerts
* `result-folder-name`: name of the folder we want to save the results into

To run application, execute `sbt run` in project root folder.

To run tests, execute `sbt test` in project root folder.

## Decisions and thoughts

* alert content - query term matching:
    * matching is case-insensitive as content texts might not adhere to grammatical rules
    * if the content and query term languages are different, automatically consider it non-matching, as one term in one language might mean something completely different in another
    * if the content type is not `text`, matching is not applicable to a text based query term
    * if the query term is keep order, check not only that the whole text is included in the content, but that they are not part of other texts
        * e.g. for query term 'ig metall' content 'richtig Metallica' should not be a match
* the currently irrelevant fields from the api are not included in the models
* even though it is said to be stable and constant, I call the query term api on every run
    * it doesn't take a lot of resources
    * this way it is for sure up-to-date on each run
* made number of alert test calls configurable
* results for different alert lists are in separate json files
    * both separate files and an aggregate could be useful in different scenarios, decided arbitrarily
* project written in Scala 2 as that is what I'm most comfortable with

## Future regards

* even better matching for query terms
    * e.g. using regex to disregard punctuation
* include previously non-used fields from the api in models if the need arises
* upgrade to Scala 3