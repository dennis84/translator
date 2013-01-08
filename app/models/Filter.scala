package translator.models

import com.mongodb.casbah.Imports._
import Implicits._

case class Filter(
  val untranslated: String,
  val untranslatedLanguages: Seq[String],
  val activatable: String) {

  def filter(t: List[Translation]) = {
    var translations = t

    if ("true" == untranslated) {
      translations = translations.filterUntranslated(untranslatedLanguages)
    }

    if ("true" == activatable) {
      translations = translations.filterActivatable
    }

    translations
  }
}
