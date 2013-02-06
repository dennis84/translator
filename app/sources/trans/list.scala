package translator
package trans

import language._
import translator.lang._

object list {

  implicit class TransList(list: List[Trans]) {

    def filterTranslated =
      list filter { trans ⇒
        trans.text != "" &&
        trans.status == Status.Active
      }

    def filterUntranslated =
      list filter { trans ⇒
        trans.text == "" &&
       (trans.status == Status.Active ||
        trans.status == Status.Empty)
      }

    def filterActivatable =
      list filter(_.status == Status.Inactive)

    def filterActive =
      list filter(_.status == Status.Active)

    def filterMustActivated =
      list.groupBy(_.code).filter { case (code, trans) ⇒
        trans.exists(_.status == Status.Inactive) &&
        (!trans.exists(_.status == Status.Active) ||
          trans.exists(t ⇒ t.status == Status.Active && t.text == ""))
      }.map(_._2).flatten.toList

    def mkComplete(langs: List[Lang]): List[Trans] =
      (for {
        h ← list.headOption
        p ← h.project
        if (!list.isEmpty)
      } yield {
        val codes = list.filterActive.map(_.code)
        val diff = langs.map(_.code).diff(codes)
        val unsorted = (list ++ diff.map { c ⇒
          Trans.empty(c, h.name, p)
        }) sortBy(_.status.id)

        langs map { l ⇒
          unsorted.filter(_.code == l.code)
        } flatten
      }) getOrElse list

    def mkEntries(langs: List[Lang]): List[Entry] =
      (for {
        lang ← langs.headOption
        head ← list.headOption
        project ← head.project
      } yield {
        list.filter { trans ⇒
          trans.status == Status.Active &&
          trans.code == lang.code
        } map { trans ⇒
          Entry(trans, project, langs, list.filter(_.name == trans.name))
        }
      }) getOrElse List.empty[Entry]
  }
}
