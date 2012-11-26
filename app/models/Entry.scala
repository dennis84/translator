package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val text: String,
  val authorId: ObjectId,
  val active: Boolean = false,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val author = UserDAO.findOneById(authorId)

  lazy val nbWords = text split(" ") length

  def toMap = Map(
    "id" -> id.toString,
    "code" -> code,
    "text" -> text,
    "author" -> author.map(_.username).getOrElse("unknown"),
    "active" -> active)
}

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String])

case class Entry(
  val name: String,
  val description: String,
  val projectId: ObjectId,
  val translationIds: List[ObjectId] = Nil,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val project = ProjectDAO.findOneById(projectId) get

  lazy val translations = TranslationDAO.findAllByIds(translationIds)

  lazy val progress = {
    val languages = LanguageDAO.findAllByProjectId(projectId)

    languages.filter { lang =>
      translations exists { trans => trans.code == lang.code && trans.text != "" }
    }.length.toFloat / languages.length * 100
  }

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "description" -> description,
    "progress" -> progress)

  def toSearchMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "description" -> description,
    "translations" -> translations.map(_.toMap))
}
