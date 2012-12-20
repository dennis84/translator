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

  def filterUntranslated(project: Project) =
    fixed(project) filter { trans =>
      trans.text == "" && (trans.status == Status.Active || trans.status == Status.Empty)
    }

  def fixed(project: Project) = {
    val langs = LanguageDAO.findAllByProject(project.encode).map(_.code)
    val diff  = langs.diff(list.filter(_.status == Status.Active).map(_.code))

    list match {
      case Nil => List.empty[Translation]
      case _   => {
        val unsorted = (list ++ diff.map { code =>
          Translation(code, list.head.name, "", "", Status.Empty, project.id, Some(project))
        }) sortBy (_.status.id)

        langs map { l =>
          unsorted.filter(_.code == l)
        } flatten
      }
    }
  }
}
