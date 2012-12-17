package translator.models

import com.mongodb.casbah.Imports._
import translator._
import translator.controllers.{ Context, ProjectContext }

object ProjectAPI {

  def list(projects: List[Project]) = projects map (generateMap(_))

  def listMine(user: User) = user.roles map { role =>
    ProjectDAO.findOneById(role.projectId)
  } flatten

  def admin(project: Project) = UserDAO.findOneById(project.adminId)

  def progress(project: Project) = LanguageDAO.findAllByProject(project) map { lang =>
    lang.code -> (TranslationAPI.list(project).filter { trans =>
      trans.code == lang.code && trans.text != "" && trans.status == Status.Active
    }.length.toFloat) / TranslationAPI.list(project).map(_.name).distinct.length * 100
  } toMap

  def nbWords(project: Project) = LanguageDAO.findAllByProject(project).map { lang =>
    lang.code -> (TranslationAPI.list(project).filter { trans =>
      trans.code == lang.code && (trans.status == Status.Active || trans.status == Status.Empty)
    }.map(_.nbWords).reduceLeft(_ + _))
  } toMap

  def contributors(project: Project) = UserDAO.findAllByProject(project)

  private def generateMap(project: Project) = Map(
    "id" -> project.id.toString,
    "name" -> project.name,
    "admin" -> admin(project).map(_.toMap).getOrElse(Map()),
    "statistics" -> Map(
      "progress" -> progress(project),
      "nb_entries" -> TranslationAPI.list(project).map(_.name).distinct.length,
      "nb_words" -> nbWords(project)
    ))
}
