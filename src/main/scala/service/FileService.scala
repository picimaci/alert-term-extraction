package service

import better.files.Dsl.{ mkdir, rm }
import better.files._
import model.AlertTermMatchResult
import io.circe.generic.auto._
import io.circe.syntax._

object FileService {
  private val resultFolderName = "extraction-results"

  private def resetResultFolder: File                                = {
    rm(file"$resultFolderName")
    mkdir(file"$resultFolderName")
  }
  def saveResults(resultsList: Seq[Seq[AlertTermMatchResult]]): Unit = {
    val dir = resetResultFolder
    resultsList
      .map(_.asJson.toString)
      .zipWithIndex
      .foreach { case (results, i) => file"$dir/$resultFolderName$i.json".write(results) }
  }
}
