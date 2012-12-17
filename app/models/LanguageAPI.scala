package translator.models

object LanguageAPI {

  def list(project: Project) =
    LanguageDAO.findAllByProject(project) map (_.toMap)

  def first(project: Project) =
    LanguageDAO.findFirstByProject(project) headOption
}
