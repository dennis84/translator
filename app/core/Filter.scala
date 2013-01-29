package translator.core

import com.mongodb.casbah.Imports._
import Implicits._

case class Filter(
  val untranslated: Boolean,
  val untranslatedLanguages: List[String],
  val activatable: Boolean) {

  def filter(entries: List[Entry]) = {
    val f1 = (e: List[Entry]) ⇒
      if(untranslated) e.filter(_.nbUntranslated(untranslatedLanguages) > 0)
      else e

    val f2 = (e: List[Entry]) ⇒
      if(activatable) e.filter(_.nbActivatable > 0)
      else e

    List(f1, f2).foldLeft(entries) { (a, b) ⇒ b(a) }
  }
}
