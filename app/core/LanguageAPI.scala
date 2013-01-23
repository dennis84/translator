package translator.core

import com.mongodb.casbah.Imports._

class LanguageAPI(langDAO: LanguageDAO) {

  import Implicits._

  def list(project: Project): List[Language] =
    langDAO.list(project)

  def create(code: String, name: String, project: Project): Option[Language] = for {
    c <- Some(code)
    l = Language(c, name, project.id)
    _ <- langDAO.insert(l)
  } yield l.withProject(project)

  def update(id: String, code: String, name: String): Option[Language] = for {
    l <- langDAO.byId(id)
    u = l.copy(code = code, name = name)
    wc = langDAO.save(u)
  } yield u

  def delete(p: Project, id: String) = for {
    l <- langDAO.byId(id)
    wc = langDAO.remove(l.encode)
  } yield l
}
