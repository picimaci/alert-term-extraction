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

  private def resetResultFolder(resultFolderName: String): File = {
    rm(file"$resultFolderName")
    mkdir(file"$resultFolderName")
  }

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
