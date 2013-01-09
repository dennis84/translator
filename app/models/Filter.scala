package translator.models

import com.mongodb.casbah.Imports._
import Implicits._

case class Filter(
  val untranslated: Boolean,
  val untranslatedLanguages: List[String],
  val activatable: Boolean) {

  def filter(t: List[Translation]) = {
    var translations = t

    if (untranslated) {
      translations = translations.filterUntranslated(untranslatedLanguages)
    }

    if (activatable) {
      translations = translations.filterActivatable
    }

    translations
  }
}
