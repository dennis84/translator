package translator
package trans

import translator.trans.list._

case class Filter(
  val untranslated: Boolean,
  val untranslatedLanguages: List[String],
  val activatable: Boolean) {

  def filter(entries: List[Entry]) = {
    val f1 = (e: List[Entry]) ⇒
      if(!untranslated) e
      else
        e.filter { entry ⇒
          entry.children.filterUntranslated.filter { t ⇒
            untranslatedLanguages.contains(t.code)
          }.length > 0
        }

    val f2 = (e: List[Entry]) ⇒
      if(activatable) e.filter(_.nbActivatable > 0)
      else e

    List(f1, f2).foldLeft(entries) { (a, b) ⇒ b(a) }
  }
}
