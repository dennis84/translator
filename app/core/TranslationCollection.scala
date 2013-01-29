package translator.core

import translator.core.Implicits._

class TranslationCollection(list: List[Translation]) {

  def uniqueNames = list map(_.name) distinct

  def filterTranslated(f: Translation ⇒ Boolean) =
    list filter { trans ⇒
      trans.text != "" &&
      (trans.status == Status.Active ||
       trans.status == Status.Inactive) &&
      f(trans)
    }

  def filterUntranslated =
    list filter { trans ⇒
      trans.text == "" &&
     (trans.status == Status.Active ||
      trans.status == Status.Empty)
    }

  def filterUntranslated(langs: List[String]) =
    list filter { trans ⇒
      trans.text == "" &&
     (trans.status == Status.Active ||
      trans.status == Status.Empty) &&
      langs.contains(trans.code)
    }

  def filterMustActivated =
    list.groupBy(_.code).filter { case (code, trans) ⇒
      trans.exists(_.status == Status.Inactive) &&
      (!trans.exists(_.status == Status.Active) ||
        trans.exists(t ⇒ t.status == Status.Active && t.text == ""))
    }.map(_._2).flatten.toList

  def filterActivatable =
    list filter(_.status == Status.Inactive)

  def filterActive =
    list filter(_.status == Status.Active)

  def filterCompleted =
    list.filter { trans ⇒
      trans.text != "" &&
      (trans.status == Status.Active ||
       trans.status == Status.Inactive)
    }

  def fixed(langs: List[Language]): List[Translation] =
    makeItFixed(langs map(_.code))

  private def makeItFixed(langs: List[String]): List[Translation] =
    (for {
      h ← list.headOption
      p ← h.project
      if (!list.isEmpty)
    } yield {
      val codes = list.filterActive.map(_.code)
      val diff = langs.diff(codes)
      val unsorted = (list ++ diff.map { c ⇒
        Translation.empty(c, h.name, p)
      }) sortBy(_.status.id)

      langs map { l ⇒
        unsorted.filter(_.code == l)
      } flatten
    }) getOrElse list
}
