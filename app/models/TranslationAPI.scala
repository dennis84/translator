package translator.models

import com.mongodb.casbah.Imports._
import translator.controllers.{ Context, ProjectContext }

object TranslationAPI {

  def listByFilter(project: Project, filter: Filter) = {
    LanguageDAO.findFirstByProject(project).headOption map { lang =>
      TranslationDAO.findAllByProjectAndFilter(project, filter, lang.code) map (generateMap(_, project))
    } getOrElse Nil
  }

  def listByIds(project: Project, ids: List[ObjectId]) =
    TranslationDAO.findAllByProjectAndIds(project, ids) map (generateMap(_, project))

  def listByName(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name) map (generateMap(_, project))

  def activatable(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name) filter (_.status == Status.Inactive)

  def progress(project: Project, name: String) = {
    val languages = LanguageDAO.findAllByProject(project)
    val translations = TranslationDAO.findAllByProjectAndName(project, name)

    languages.filter { lang =>
      translations exists { trans => trans.code == lang.code && trans.text != "" && trans.status == Status.Active }
    }.length.toFloat / languages.length * 100
  }

  private def generateMap(trans: Translation, project: Project) = Map(
    "id" -> trans.id.toString,
    "code" -> trans.code,
    "name" -> trans.name,
    "text" -> trans.text,
    "author" -> UserDAO.findOneById(trans.authorId).map(_.username).getOrElse("unknown"),
    "status" -> trans.status.toString,
    "nb_activatable" -> activatable(project, trans.name).length,
    "progress" -> progress(project, trans.name))
}
