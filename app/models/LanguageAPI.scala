package translator.models

object LanguageAPI {

  /** Lists all languages by project.
   */
  def list(project: Project) =
    LanguageCollection(LanguageDAO.findAllByProject(project))

  /** Returns the first language in database. This is also the primary.
   */
  def first(project: Project): Option[LanguageItem] =
    LanguageDAO.findFirstByProject(project).headOption map(new LanguageItem(_))

  /** Returns the language code by language code. At first we check if the code
   *  is empty then returns the primary language code. If the code is not empty
   *  then the code will be checked if it exists in database. At least it will
   *  be checked if the result is defined if not then return "en", but this
   *  should not be possible.
   */
  def code(project: Project, code: String) = (code match {
    case "" => LanguageAPI.first(project).map(_.model)
    case _  => LanguageDAO.findOneByProjectAndCode(project, code)
  }) map(_.code) getOrElse "en"

  /** Creates a new language.
   */
  def create(code: String, name: String, project: Project) = {
    val lang = Language(code, name, project.id)
    LanguageDAO.insert(lang)
    new LanguageItem(lang)
  }

  /** Updates a language with code and name.
   */
  def update(lang: Language, code: String, name: String) = {
    val updated = lang.copy(code, name)
    LanguageDAO.save(updated)
    new LanguageItem(updated)
  }
}

class LanguageItem(val model: Language) extends Item {

  def serialize = Map(
    "id"   -> model.id.toString,
    "code" -> model.code,
    "name" -> model.name)
}

object LanguageCollection {

  def apply(langs: List[Language]) =
    new Collection(langs.map(new LanguageItem(_)))
}

trait Item {

  def serialize: Map[String, Any]
}

class Collection[A <: Item](val models: List[A]) {

  def response = models map(_.serialize)
}
