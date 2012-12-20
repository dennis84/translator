package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String],
  val activatable: String)

case class DbTranslation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val author: String,
  val status: Status,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val author: String,
  val status: Status,
  val projectId: ObjectId,
  val project: Option[Project] = None,
  val id: ObjectId = new ObjectId) {

  lazy val nbWords = text.split(" ").filterNot(_ == "").length

  def withProject(p: Project) = copy(project = Some(p))

  def encode = DbTranslation(code, name, text, projectId, author, status, id)

  def serialize = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author,
    "status" -> status.toString,
    "nb_activatable" -> 0,
    "progress" -> 0)
}
