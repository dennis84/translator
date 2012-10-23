package translator.controllers

import translator.models._

object EntryController extends BaseController {

  def list(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      JsonOk(EntryDAO.findAllByProject(project) map (_.toMap))
    } getOrElse JsonNotFound
  }
}
