package translator.controllers

import translator._
import translator.models._

object LanguageController extends BaseController {

  def list(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      JsonOk(LanguageDAO.findAllByProject(project) map (_.toMap))
    } getOrElse JsonNotFound
  }
}
