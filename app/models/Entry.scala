package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String],
  val activatable: String)

case class Entry(
  val name: String,
  val description: String,
  val projectId: ObjectId,
  val translations: List[Translation] = Nil,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val project = ProjectDAO.findOneById(projectId)

  lazy val activatableTranslations = translations filter (_.active == false)

  lazy val progress = {
    val languages = LanguageDAO.findAllByProjectId(projectId)

    languages.filter { lang =>
      translations exists { trans => trans.code == lang.code && trans.text != "" && trans.active == true }
    }.length.toFloat / languages.length * 100
  }

  def translation(id: String) = translations find (_.id == id)

  def +(trans: Translation) = copy(translations = translations :+ trans)

  def update(trans: Translation) = copy(translations = translations map {
    case t if(t.id == trans.id) => trans
    case t => t
  })

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "description" -> description,
    "progress" -> progress,
    "nb_activatable" -> activatableTranslations.length)

  def toSearchMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "description" -> description,
    "translations" -> translations.map(_.toMap))
}
