package translator.models

import com.mongodb.casbah.Imports._

object LanguageAPI {

  import Implicits._

  def list(project: Project): List[Language] =
    LanguageDAO.findAllByProject(project)

  def first(project: Project): Option[Language] =
    LanguageDAO.findFirstByProject(project).headOption

  def code(project: Project, code: String): Option[String] = code match {
    case "" => first(project).map(_.code)
    case _  => LanguageDAO.findOneByProjectAndCode(project, code).map(_.code)
  }

  def create(cd: String, name: String, project: Project): Option[Language] = for {
    c <- code(project, cd)
    lang = Language(c, name, project.id)
    _ <- LanguageDAO.insert(lang)
  } yield lang.withProject(project)

  def update(id: String, code: String, name: String): Option[Language] = for {
    l <- LanguageDAO.byId(id)
    updated = l.copy(code = code, name = name)
    wc = LanguageDAO.save(updated)
  } yield updated
}
