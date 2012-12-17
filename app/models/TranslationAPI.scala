package translator.models

import com.mongodb.casbah.Imports._
import translator._
import translator.controllers.{ Context, ProjectContext }

object TranslationAPI {

  def list(project: Project) = fixTranslations(
    TranslationDAO.findAllByProject(project), project)

  def listByFilter(project: Project, filter: Filter) = {
    LanguageAPI.first(project) map { lang =>
      var translations = fixTranslations(TranslationDAO.findAllByProject(project), project)

      if ("true" == filter.untranslated) {
        val untranslatedNames = translations.filter { trans =>
          trans.text == "" && filter.untranslatedLanguages.exists(_ == trans.code) && trans.status == Status.Active
        } map (_.name)

        translations = untranslatedNames.map { name =>
          translations.find(t => name == t.name && lang.code == t.code)
        } flatten
      }

      if ("true" == filter.activatable) {
        translations = translations filter { trans =>
          TranslationAPI.activatable(project, trans.name).length > 0
        }
      }

      translations filter (_.code == lang.code) map (generateMap(_, project))
    } getOrElse Nil
  }

  def listByIds(project: Project, ids: List[ObjectId]) =
    TranslationDAO.findAllByProjectAndIds(project, ids) map (generateMap(_, project))

  def listByName(project: Project, name: String) = fixTranslations(
    TranslationDAO.findAllByProjectAndName(project, name), project) map (generateMap(_, project))

  def activatable(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name) filter (_.status == Status.Inactive)

  def progress(project: Project, name: String) = {
    val languages = LanguageDAO.findAllByProject(project)
    val translations = TranslationDAO.findAllByProjectAndName(project, name)

    languages.filter { lang =>
      translations exists { trans => trans.code == lang.code && trans.text != "" && trans.status == Status.Active }
    }.length.toFloat / languages.length * 100
  }

  def getChangeable(project: Project, id: String) = {
    (for {
      actual <- TranslationDAO.findOneById(id)
      old <- TranslationDAO.findOneByProjectNameAndCode(project, actual.name, actual.code)
    } yield (actual, old))
  }

  private def fixTranslations(trans: List[Translation], project: Project) = {
    val langs = LanguageDAO.findAllByProject(project).map(_.code)
    val diff  = langs.diff(trans.filter(_.status == Status.Active).map(_.code))

    trans match {
      case Nil   => List.empty[Translation]
      case trans => {
        val unsorted = (trans ++ diff.map { code =>
          EmptyTranslation(code, trans.head)
        }) sortBy (_.status.id)

        langs map { l =>
          unsorted.filter(_.code == l)
        } flatten
      }
    }
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
