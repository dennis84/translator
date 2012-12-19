package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import translator._

case class User(
  val username: String,
  val password: String,
  val roles: List[Role] = List.empty[Role],
  val email: Option[String] = None,
  @Key("_id") val id: ObjectId = new ObjectId) {

  def roles(project: Project): List[String] =
    roles filter (_.projectId == project.id) map (_.role)
}

case class UserVeiw(
  val model: User,
  val roles: List[String] = Nil) {

  def serialize = Map(
    "id" -> model.id.toString,
    "username" -> model.username,
    "email" -> model.email.getOrElse(""),
    "roles" -> roles)
}

case class Role(
  val role: String,
  val projectId: ObjectId)

object Role {

  val ADMIN  = "ROLE_ADMIN"
  val AUTHOR = "ROLE_AUTHOR"

  def Admin(id: ObjectId) = Role(ADMIN, id)
  def Author(id: ObjectId) = Role(AUTHOR, id)
}
