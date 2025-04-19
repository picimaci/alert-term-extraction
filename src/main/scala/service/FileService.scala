package service

import better.files.Dsl.{ mkdir, rm }
import better.files._
import model.AlertTermMatchResult
import io.circe.generic.auto._
import io.circe.syntax._

object FileService {
  private def resetResultFolder(resultFolderName: String): File                                = {
    rm(file"$resultFolderName")
    mkdir(file"$resultFolderName")
  }
  def saveResults(resultFolderName: String, resultsList: Seq[Seq[AlertTermMatchResult]]): Unit = {
    val dir = resetResultFolder(resultFolderName)
    resultsList
      .map(_.asJson.toString)
      .zipWithIndex
      .foreach { case (results, i) => file"$dir/$resultFolderName$i.json".write(results) }
  }
}
