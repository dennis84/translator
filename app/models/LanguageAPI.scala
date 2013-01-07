package translator.models

import com.mongodb.casbah.Imports._

object LanguageAPI {

  import Implicits._

  def by(id: ObjectId, p: Project): Option[Language] =
    LanguageDAO.findOneById(id) map(makeLanguage(_, p))

  /** Lists all languages by project.
   */
  def list(project: Project): List[Language] =
    LanguageDAO.findAllByProject(project) map(makeLanguage(_, project))

  /** Returns the first language in database. This is also the primary.
   */
  def first(project: Project): Option[Language] =
    LanguageDAO.findFirstByProject(project).headOption map(makeLanguage(_, project))

  /** Returns the language code by language code. At first we check if the code
   *  is empty then returns the primary language code. If the code is not empty
   *  then the code will be checked if it exists in database.
   */
  def code(project: Project, code: String): Option[String] = code match {
    case "" => LanguageAPI.first(project).map(_.code)
    case _  => LanguageDAO.findOneByProjectAndCode(project, code).map(_.code)
  }

  /** Creates a new language.
   */
  def create(cd: String, name: String, project: Project): Option[Language] = for {
    c <- code(project, cd)
    lang = DbLanguage(c, name, project.id)
    _ <- LanguageDAO.insert(lang)
  } yield makeLanguage(lang, project)

  /** Updates a language with code and name.
   */
  def update(lang: Language, code: String, name: String): Language = {
    val updated = lang.copy(code, name)
    LanguageDAO.save(updated)
    makeLanguage(updated, lang.project)
  }

  def makeLanguage(l: DbLanguage, project: Project): Language =
    Language(l.code, l.name, project, l.id)
}
