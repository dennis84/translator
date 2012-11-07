package translator.utils

import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile
import java.io.File

object Importer {

  def parse(file: FilePart[TemporaryFile]) = file.filename.substring(file.filename.lastIndexOf(".") + 1, file.filename.length) match {
    case "json" => Some("json")
    case "po" => Some("po")
    case _ => None
  }

  def json(file: FilePart[TemporaryFile]) = {
  }

  def po(file: FilePart[TemporaryFile]) = {
  }
}
