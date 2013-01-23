package translator.core

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import translator._

case class DbUser(
  val username: String,
  val password: String,
  val roles: List[DbRole] = List.empty[DbRole],
  val email: String = "",
  @Key("_id") val id: ObjectId = new ObjectId)

case class User(
  val username: String,
  val password: String,
  val email: String = "",
  val roles: List[String] = Nil,
  val rawRoles: List[DbRole] = Nil,
  val id: ObjectId = new ObjectId) {

  def isAnon = roles contains "ROLE_ANONYMOUS"

  def withRoles(p: Project) = copy(roles =
    rawRoles.filter { role =>
      role.projectId == p.id
    }.map(_.role))

  def encode = DbUser(username, password, rawRoles, email, id)

  def serialize = Map(
    "id" -> id.toString,
    "username" -> username,
    "email" -> email,
    "roles" -> roles)
}

object User {

  def Anonymous = User(
    username = "anonymous",
    password = "",
    roles = List("ROLE_ANONYMOUS"))
}

case class DbRole(
  val role: String,
  val projectId: ObjectId)

object Role {

  val ADMIN  = "ROLE_ADMIN"
  val AUTHOR = "ROLE_AUTHOR"

  def Admin(id: ObjectId) = DbRole(ADMIN, id)
  def Author(id: ObjectId) = DbRole(AUTHOR, id)
}
