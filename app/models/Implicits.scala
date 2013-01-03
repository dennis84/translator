package translator.models

object Implicits {

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

  def filterActivatable = list filter { trans =>
    trans.status == Status.Inactive
  }

  def fixed: List[Translation] = (for {
    head <- list.headOption
    project <- head.project
  } yield {
    makeItFixed(LanguageDAO.findAllByProject(project) map(_.code))
  }) getOrElse list

  def fixed(langs: List[Language]): List[Translation] =
    makeItFixed(langs map(_.code))

  private def makeItFixed(langs: List[String]): List[Translation] = (for {
    head <- list.headOption
    project <- head.project
    if (!list.isEmpty)
  } yield {
    val codes = list.filter(_.status == Status.Active).map(_.code)
    val diff = langs.diff(codes)
    val unsorted = (list ++ diff.map { code =>
      Translation(
        code,
        head.name,
        "",
        "",
        Status.Empty,
        project.id,
        Some(project))
    }) sortBy (_.status.id)

    langs map { l =>
      unsorted.filter(_.code == l)
    } flatten
  }) getOrElse list
}
