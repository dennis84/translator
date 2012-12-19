package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String],
  val activatable: String)

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val authorId: ObjectId,
  val status: Status,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val nbWords = text.split(" ").filterNot(_ == "").length
}

case class TranslationView(
  val model: Translation,
  val nbActivatable: Option[Int] = None,
  val progress: Option[Float] = None) {

  def serialize = Map(
    "id" -> model.id.toString,
    "code" -> model.code,
    "name" -> model.name,
    "text" -> model.text,
    "author" -> "",
    "status" -> model.status.toString,
    "nb_activatable" -> nbActivatable,
    "progress" -> progress)
}

object EmptyTranslation {

  def apply(code: String, t: Translation) = Translation(code, t.name, "", t.projectId, t.authorId, Status.Empty)
}

case class TranslationImport(
  val translation: Translation,
  val status: Status)
