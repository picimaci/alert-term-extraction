package service

import better.files.Dsl.{ mkdir, rm }
import better.files._
import com.typesafe.scalalogging.Logger
import model.AlertTermMatchResult
import io.circe.generic.auto._
import io.circe.syntax._
import util.ErrorHandler._

object FileService {
  private implicit val logger: Logger = Logger(getClass.getName)

  /*
    In order to have a clean slate on each run remove results folder
    if exists and create new one for saving the results
   */
  private def resetResultFolder(resultFolderName: String): File = {
    rm(file"$resultFolderName")
    mkdir(file"$resultFolderName")
  }

  /*
    Reset results folder and save match results in json files per fetched alert list
   */
  def saveResults(resultFolderName: String, resultsList: Seq[Seq[AlertTermMatchResult]]): Either[String, Unit] = {
    val dir = resetResultFolder(resultFolderName)
    logger.info(s"Directory $dir removed and recreated successfully")
    resultsList
      .map(_.asJson.toString)
      .zipWithIndex
      .foreach { case (results, i) => file"$dir/$resultFolderName$i.json".write(results) }
    logger.info("Results saved into files successfully")
  }.handleErrors("saving results into files")
}
