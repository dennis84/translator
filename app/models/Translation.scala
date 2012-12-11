package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val authorId: ObjectId,
  val active: Boolean,
  @Key("_id") val id: String) {

  lazy val author = UserDAO.findOneById(authorId)

  lazy val nbWords = text split(" ") length

  def toMap = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author.map(_.username).getOrElse("unknown"),
    "active" -> active,
    "nb_activatable" -> 0,
    "progress" -> 0
  )
}

object EmptyTranslation {

  def apply(code: String, t: Translation) = Translation(code, t.name, "", t.projectId, t.authorId, true, "")
}
