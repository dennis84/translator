package translator.controllers

import translator._
import translator.models._
import translator.forms._

object ExportController extends BaseController {

  def translations(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationDAO.findAllByProject(ctx.project).filter(_.code == getOr("language", "en")).map { trans =>
      trans.name -> trans.text
    }.toMap)
  }
}
