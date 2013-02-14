package translator
package project

import play.api.libs.json._
import translator.user._
import translator.lang._
import translator.trans._
import translator.trans.list._

case class Project(
  val id: String,
  val name: String,
  val token: String,
  val adminId: String,
  val admin: Option[User] = None,
  val open: Boolean = false,
  val repo: String = "",
  val progress: Option[Map[String, Float]] = None,
  val nbWords: Option[Map[String, Int]] = None) {

  def withUser(u: User) = copy(admin = Some(u))

  def withStats(trans: List[Trans], langs: List[Lang]) = {
    val pct = (l: Lang) ⇒ List(
      trans.filterTranslated.filter(_.code == l.code).length.toFloat,
      trans.map(_.name).distinct.length.toFloat) reduceLeft(100 * _ / _)

    val nb = (l: Lang) ⇒
      trans.filterTranslated.filter(_.code == l.code).map(_.nbWords).reduceLeftOption(_+_).getOrElse(0)

    copy(
      progress = Some(langs.map(l ⇒ l.code -> pct(l)).toMap),
      nbWords = Some(langs.map(l ⇒ l.code -> nb(l)).toMap))
  }

  def toJson = Json.obj(
    "id" -> id,
    "name" -> name,
    "open" -> open,
    "repo" -> repo,
    "progress" -> progress,
    "nb_words" -> nbWords)
}
