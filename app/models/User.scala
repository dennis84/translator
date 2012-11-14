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

  def roles(project: Project): List[String] = roles filter (_.projectId == project.id) map (_.role)

  def toMap = Map(
    "id" -> id.toString,
    "username" -> username,
    "email" -> email.getOrElse(""))
}

case class Role(
  val role: String,
  val userId: ObjectId,
  val projectId: ObjectId)
