package translator
package core

import play.api.libs.json._

case class Lang(
  val id: String,
  val code: String,
  val name: String,
  val projectId: String,
  val project: Option[Project] = None) {

  def withProject(p: Project) = copy(project = Some(p))

  def toJson = Json.obj(
    "id" -> id,
    "code" -> code,
    "name" -> name)
}
