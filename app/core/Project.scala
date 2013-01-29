package translator.core

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import translator.core.Implicits._

case class DbProject(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  val open: Boolean = false,
  val repo: Option[String] = None,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Project(
  val name: String,
  val token: String,
  val adminId: ObjectId,
  val admin: Option[User] = None,
  val open: Boolean = false,
  val repo: Option[String] = None,
  val progress: Option[Map[String, Float]] = None,
  val nbWords: Option[Map[String, Any]] = None,
  val id: ObjectId = new ObjectId) {

  def withUser(u: User) = copy(admin = Some(u))

  def withStats(trans: List[Translation], langs: List[Language]) = {
    val pct = (l: Language) ⇒ List(
      trans.filterTranslated(_.code == l.code).length.toFloat,
      trans.uniqueNames.length.toFloat) reduceLeft(100 * _ / _)

    val nb = (l: Language) ⇒
      trans.filterTranslated(_.code == l.code).map(_.nbWords).reduceLeftOption(_+_).getOrElse(0)

    copy(
      progress = Some(langs.map(l ⇒ l.code -> pct(l)).toMap),
      nbWords = Some(langs.map(l ⇒ l.code -> nb(l)).toMap))
  }

  def encode = DbProject(name, adminId, token, open, repo, id)

  def serialize = Map(
    "id" -> id.toString,
    "name" -> name,
    "progress" -> progress,
    "nb_words" -> nbWords,
    "open" -> open,
    "repo" -> repo)
}
