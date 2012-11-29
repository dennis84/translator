package translator.controllers

import translator._
import translator.models._
import translator.forms._

object ExportController extends BaseController {

  def entries(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(EntryDAO.findAllByProject(ctx.project.get).map { entry =>
      entry.name -> entry.translations.find(_.code == getOr("language", "en")).map(_.text).getOrElse("")
    }.toMap)
  }
}
