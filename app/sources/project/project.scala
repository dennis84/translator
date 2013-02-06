package translator
package project

import play.api.libs.json._
import translator.user._

case class Project(
  val id: String,
  val name: String,
  val token: String,
  val adminId: String,
  val admin: Option[User] = None,
  val open: Boolean = false,
  val repo: String = "",
  val progress: Option[Map[String, Float]] = None,
  val nbWords: Option[Map[String, Any]] = None) {

  def withUser(u: User) = copy(admin = Some(u))

  def toJson = Json.obj(
    "id" -> id,
    "name" -> name,
    "open" -> open,
    "repo" -> repo)
}
