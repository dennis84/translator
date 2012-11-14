package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Project(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  @Key("_id") val id: ObjectId = new ObjectId) {

  def admin = UserDAO.findOneById(adminId)

  def contributors = UserDAO.findAllByProject(this)

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "admin" -> admin.map(_.toMap).getOrElse(Map()))
}
