package translator.models

import com.mongodb.casbah.Imports._

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String],
  val activatable: String) {

  def makeQuery = {
    var query = MongoDBObject()

    if ("true" == untranslated) {
      query ++ MongoDBObject("text" -> "")
    }

    if ("true" == activatable) {
      query ++ MongoDBObject("status" -> Status.Inactive)
    }

    query
  }

  def filter(t: List[Translation]) = {
    var translations = t

    if ("true" == untranslated) {
      val untranslatedNames = translations.filter { trans =>
        trans.text == "" &&
        untranslatedLanguages.exists(_ == trans.code) &&
        trans.status == Status.Active
      } map (_.name)

      translations = untranslatedNames.map { name =>
        translations.find(t => name == t.name && lang.code == t.code)
      } flatten
    }

    if ("true" == activatable) {
      translations = translations filter { trans =>
        //TranslationAPI.activatable(project, trans.name).length > 0
        true
      }
    }

    translations
  }
}

