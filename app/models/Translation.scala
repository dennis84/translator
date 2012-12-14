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

object EmptyTranslation {

  def apply(code: String, t: Translation) = Translation(code, t.name, "", t.projectId, t.authorId, Status.Empty)
}
