package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import translator.models.Implicits._

case class DbTranslation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val author: String,
  val status: Int,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val author: String,
  val status: Status,
  val projectId: ObjectId,
  val project: Option[Project] = None,
  val id: ObjectId = new ObjectId) {

  lazy val nbWords = text.split(" ").filterNot(_ == "").length

  def withProject(p: Project) = copy(project = Some(p))

  def encode = DbTranslation(code, name, text, projectId, author, status.id, id)

  def serialize = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author,
    "status" -> status.toString)
  
  override def toString = """%s (%s): %s (%s)""" format(name, code, text, status)
}

object Translation {

  def apply(c: String, n: String, t: String, u: User, p: Project): Translation =
    Translation(c, n, t, u.username, Status.Inactive, p.id, Some(p))

  def empty(c: String, n: String, p: Project) =
    Translation(c, n, "", "", Status.Empty, p.id, Some(p))
}
