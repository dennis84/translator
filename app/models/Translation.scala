package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val text: String,
  val authorId: ObjectId,
  val active: Boolean = false) {

  def toMap = Map(
    "code" -> code,
    "text" -> text,
    "author" -> "",
    "active" -> active)
}

case class Entry(
  val name: String,
  val description: String,
  val projectId: ObjectId,
  val translations: List[Translation] = Nil,
  @Key("_id") val id: ObjectId = new ObjectId) {

  def project = ProjectDAO.findOneById(projectId) get

  def translationsFixed = translations ++ LanguageDAO.findAllByProjectId(projectId).map(_.code).diff(translations.map(_.code)).map { code =>
    Translation(code, "", project.adminId, true)
  }

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "description" -> description,
    "translations" -> translationsFixed.map(_.toMap))
}
