package translator
package user

import play.api.libs.json._
import translator.core._

case class User(
  val id: String,
  val username: String,
  val password: String,
  val email: String = "",
  val roles: List[String] = Nil,
  val dbRoles: List[Role] = Nil) {

  def isAnon = roles contains Role.ANONYMOUS

  def toJson = Json.obj(
    "id" -> id,
    "username" -> username,
    "email" -> email)
}

object User {

  def Anonymous = User(Doc.mkID, "anonymous", "", "", List(Role.ANONYMOUS))
}

case class Role(
  val role: String,
  val projectId: String)

object Role {

  val ADMIN  = "ROLE_ADMIN"
  val AUTHOR = "ROLE_AUTHOR"
  val ANONYMOUS = "ROLE_ANONYMOUS"

  def Admin(id: String) = Role(ADMIN, id)
  def Author(id: String) = Role(AUTHOR, id)
}
