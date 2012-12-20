package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class DbLanguage(
  val code: String,
  val name: String,
  val projectId: ObjectId,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Language(
  val id: ObjectId,
  val code: String,
  val name: String,
  val project: Project) {

  def encode = DbLanguage(code, name, project.id, id)

  def serialize = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name)
}
