package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

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
  val nbActivatable: Option[Int] = None,
  val nbMustActivated: Option[Int] = None,
  val progress: Option[Float] = None,
  val id: ObjectId = new ObjectId) {

  lazy val nbWords = text.split(" ").filterNot(_ == "").length

  def withProject(p: Project) = copy(project = Some(p))

  def withStats(others: List[Translation], langs: List[Language]) = copy(
    nbActivatable = Some(others.filter { trans =>
      trans.name == name &&
      trans.status == Status.Inactive
    }.length),
    nbMustActivated = Some(others.filter(_.name == name).groupBy(_.code).filter { g =>
      g._2.exists(_.status == Status.Inactive) &&
      (!g._2.exists(_.status == Status.Active) ||
        g._2.exists(t => t.status == Status.Active && t.text == ""))
    }.map(_._2).toList.length),
    progress = Some(others.filter { trans =>
      trans.name == name &&
      trans.text != "" &&
      (trans.status == Status.Active ||
       trans.status == Status.Inactive)
    }.length.toFloat / langs.length * 100)
  )

  def encode = DbTranslation(code, name, text, projectId, author, status.id, id)

  def serialize = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author,
    "status" -> status.toString,
    "nb_activatable" -> nbActivatable,
    "nb_must_activated" -> nbMustActivated,
    "progress" -> progress)
  
  override def toString = """%s (%s): %s (%s)""" format(name, code, text, status)
}

object Translation {

  def apply(c: String, n: String, t: String, u: User, p: Project): Translation =
    Translation(c, n, t, u.username, Status.Inactive, p.id, Some(p))

  def empty(c: String, n: String, p: Project) =
    Translation(c, n, "", "", Status.Empty, p.id, Some(p))
}
