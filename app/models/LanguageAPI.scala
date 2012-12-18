package translator.models

object LanguageAPI {

  /** Lists all languages by project.
   */
  def list(project: Project) = Collection[Language, Lang](
    LanguageDAO.findAllByProject(project))

  /** Returns the first language in database. This is also the primary.
   */
  def first(project: Project) =
    LanguageDAO.findFirstByProject(project) headOption

  /** Returns the language code by language code. At first we check if the code
   *  is empty then returns the primary language code. If the code is not empty
   *  then the code will be checked if it exists in database. At least it wil
   *  be checked if the result is defined if not then return "en", but this
   *  should not be possible.
   */
  def code(project: Project, code: String) = (code match {
    case "" => LanguageAPI.first(project)
    case _  => LanguageDAO.findOneByProjectAndCode(project, code)
  }) map(_.code) getOrElse "en"

  class Lang(lang: Language) extends Item {
    def response = Map(
      "id"   -> lang.id.toString,
      "code" -> lang.code,
      "name" -> lang.name)
  }
}
