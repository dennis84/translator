package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class DbTranslation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val author: String,
  val status: Status,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val author: String,
  val status: Status,
  val projectId: ObjectId,
  val project: Option[Project] = None,
  val nbActivatable: Option[Int] = None,
  val progress: Option[Float] = None,
  val id: ObjectId = new ObjectId) {

  lazy val nbWords = text.split(" ").filterNot(_ == "").length

  def withProject(p: Project) = copy(project = Some(p))

  def withStats(others: List[Translation], langs: List[Language]) = copy(
    nbActivatable = Some(others.filter { trans =>
      trans.name == name &&
      trans.status == Status.Inactive
    }.length),
    progress = Some(others.filter { trans =>
      trans.name == name &&
      trans.text != "" &&
      trans.status == Status.Active
    }.length.toFloat / langs.length * 100)
  )

  def encode = DbTranslation(code, name, text, projectId, author, status, id)

  def serialize = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author,
    "status" -> status.toString,
    "nb_activatable" -> nbActivatable,
    "progress" -> progress)
  
  override def toString = """%s (%s): %s""" format(name, code, text)
}

object Translation {

  def apply(c: String, n: String, t: String, u: User, p: Project): Translation =
    Translation(c, n, t, u.username, status(u), p.id, Some(p))

  def empty(c: String, n: String, p: Project) =
    Translation(c, n, "", "", Status.Empty, p.id, Some(p))

  private def status(user: User) =
    user.roles contains (Role.ADMIN) match {
      case true => Status.Active
      case false => Status.Inactive
    }
}
