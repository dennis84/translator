package translator.models

object LanguageAPI {

  /** Lists all languages by project.
   */
  def list(project: Project) =
    LanguageDAO.findAllByProject(project) map(LanguageView(_))

  /** Returns the first language in database. This is also the primary.
   */
  def first(project: Project) =
    LanguageDAO.findFirstByProject(project).headOption map(new LanguageView(_))

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
    new LanguageView(lang)
  }

  /** Updates a language with code and name.
   */
  def update(lang: Language, code: String, name: String) = {
    val updated = lang.copy(code, name)
    LanguageDAO.save(updated)
    new LanguageView(updated)
  }
}
