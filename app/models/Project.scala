package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Project(
  val name: String,
  val adminId: ObjectId,
  @Key("_id") val id: ObjectId = new ObjectId) {

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name)
}
