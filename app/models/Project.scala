package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class DbProject(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  val repo: Option[String] = None,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Project(
  val name: String,
  val token: String,
  val adminId: ObjectId,
  val admin: Option[User] = None,
  val repo: Option[String] = None,
  val progress: Option[Map[String, Float]] = None,
  val nbWords: Option[Map[String, Any]] = None,
  val id: ObjectId = new ObjectId) {

  def withUser(u: User) = copy(admin = Some(u))

  def withStats(translations: List[Translation], languages: List[Language]) = copy(
    progress = Some(languages.map { lang =>
      lang.code -> (translations.filter { trans =>
        trans.code == lang.code &&
        trans.text != "" &&
        trans.status == Status.Active
      }.length.toFloat) / translations.map(_.name).distinct.length * 100
    }.toMap),
    nbWords = Some(languages.map { lang =>
      lang.code -> (translations.filter { trans =>
        trans.code == lang.code &&
        trans.status == Status.Active
      }.map(_.nbWords).reduceLeftOption(_+_).getOrElse(0))
    }.toMap)
  )

  def encode = DbProject(name, adminId, token, repo, id)

  def serialize = Map(
    "id" -> id.toString,
    "name" -> name,
    "progress" -> progress,
    "nb_words" -> nbWords)
}
