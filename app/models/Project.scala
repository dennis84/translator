package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class DbProject(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Project(
  val name: String,
  val token: String,
  val admin: Option[User] = None,
  val id: ObjectId = new ObjectId) {

  def withUser(u: User) = copy(admin = u)

  def encode = DbProject(name, admin.map(_.id).getOrElse(new ObjectId()), token, id)

  def serialize = Map(
    "id" -> id.toString,
    "name" -> name)
}
