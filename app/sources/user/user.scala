package translator
package user

import play.api.libs.json._
import translator.core._
import translator.project._

case class User(
  val id: String,
  val username: String,
  val password: String,
  val email: String = "",
  val dbRoles: List[Role] = Nil,
  val roles: List[String] = Nil) {

  def isAnon = roles contains Role.ANONYMOUS

  def isAdmin = roles contains Role.ADMIN

  def withRoles(p: Project) = copy(
    roles = dbRoles.filter { role ⇒
      role.projectId == p.id
    }.map(_.role))

  def toJson = Json.obj(
    "id" -> id,
    "username" -> username,
    "email" -> email,
    "roles" -> roles)
}

object User {

  def Anonymous = User(Doc.mkID, "anonymous", "", "", Nil, List(Role.ANONYMOUS))
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
