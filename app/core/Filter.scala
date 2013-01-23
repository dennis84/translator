package translator.core

import com.mongodb.casbah.Imports._
import Implicits._

case class Filter(
  val untranslated: Boolean,
  val untranslatedLanguages: List[String],
  val activatable: Boolean) {

  def filter(e: List[Entry]) = {
    var entries = e

    if (untranslated) {
      entries = entries.filter(_.nbUntranslated(untranslatedLanguages) > 0)
    }

    if (activatable) {
      entries = entries.filter(_.nbActivatable > 0)
    }

    entries
  }
}
