package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val authorId: ObjectId,
  val active: Boolean = false,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val author = UserDAO.findOneById(authorId)

  lazy val nbWords = text split(" ") length

  def toMap = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author.map(_.username).getOrElse("unknown"),
    "active" -> active)
}
