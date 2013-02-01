package translator.core

import play.api.libs.json._

case class Trans(
  val id: String,
  val code: String,
  val name: String,
  val text: String,
  val author: String,
  val status: Status,
  val projectId: String,
  val project: Option[Project] = None) {

  def toJson = Json.obj(
    "id" -> id,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author)
}
