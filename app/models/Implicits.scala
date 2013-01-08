package translator.models

import com.mongodb.casbah.Imports._

object Implicits {

  implicit def objectId2string(id: ObjectId): String = id.toString

  implicit def string2objectId(id: String): ObjectId = new ObjectId(id)

  implicit def project2DbProject(p: Project) = p.encode

  implicit def translation2DbTranslation(t: Translation) = t.encode

  implicit def language2DbLanguage(l: Language) = l.encode

  implicit def user2DbUser(u: User) = u.encode

  implicit def list2TranslationCollection(list: List[Translation]) =
    new TranslationCollection(list)
}

class TranslationCollection(list: List[Translation]) {

  import Implicits._

  def filterUntranslated =
    fixed filter { trans =>
      trans.text == "" &&
     (trans.status == Status.Active ||
      trans.status == Status.Empty)
    }

  def filterActivatable =
    list filter(_.status == Status.Inactive)

  def filterActive =
    list filter(_.status == Status.Active)

  def fixed: List[Translation] = (for {
    h <- list.headOption
    p <- h.project
  } yield {
    makeItFixed(LanguageDAO.findAllByProject(p) map(_.code))
  }) getOrElse list

  def fixed(langs: List[Language]): List[Translation] =
    makeItFixed(langs map(_.code))

  private def makeItFixed(langs: List[String]): List[Translation] =
    (for {
      h <- list.headOption
      n = h.name
      p <- h.project
      if (!list.isEmpty)
    } yield {
      val codes = list.filterActive.map(_.code)
      val diff = langs.diff(codes)
      val unsorted = (list ++ diff.map { c =>
        Translation.empty(c, n, p)
      }) sortBy (_.status.id)

      langs map { l =>
        unsorted.filter(_.code == l)
      } flatten
    }) getOrElse list
}
