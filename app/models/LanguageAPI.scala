package translator.models

object LanguageAPI {

  def list(project: Project) =
    LanguageDAO.findAllByProject(project) map (_.toMap)

  def first(project: Project) =
    LanguageDAO.findFirstByProject(project) headOption

  def code(project: Project, code: String) = (code match {
    case "" => LanguageAPI.first(project)
    case _  => LanguageDAO.findOneByProjectAndCode(project, code)
  }) map(_.code) getOrElse "en"
}
