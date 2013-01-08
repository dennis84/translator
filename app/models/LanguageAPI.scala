package translator.models

import com.mongodb.casbah.Imports._

object LanguageAPI {

  import Implicits._

  def list(project: Project): List[Language] =
    LanguageDAO.list(project)

  def create(code: String, name: String, project: Project): Option[Language] = for {
    c <- LanguageDAO.validateCode(project, code)
    l = Language(c, name, project.id)
    _ <- LanguageDAO.insert(l)
  } yield l.withProject(project)

  def update(id: String, code: String, name: String): Option[Language] = for {
    l <- LanguageDAO.byId(id)
    u = l.copy(code = code, name = name)
    wc = LanguageDAO.save(u)
  } yield u

  def delete(p: Project, id: String) = for {
    l <- LanguageDAO.byId(id)
    wc = LanguageDAO.remove(l.encode)
  } yield l
}
