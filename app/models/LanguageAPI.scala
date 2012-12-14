package translator.models

object LanguageAPI {

  def list(project: Project) =
    LanguageDAO.findAllByProject(project) map (_.toMap)
}
