package translator.models

import com.mongodb.casbah.Imports._
import Implicits._

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String],
  val activatable: String) {

  def makeQuery = {
    var query = MongoDBObject()

    if ("true" == activatable) {
      query ++ MongoDBObject("status" -> Status.Inactive)
    }

    query
  }

  def filter(t: List[Translation]) = {
    var translations = t

    if ("true" == untranslated) {
      translations = translations.filterUntranslated
    }

    translations
  }
}
