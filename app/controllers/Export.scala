package translator.controllers

import translator._
import translator.models._
import translator.forms._

object ExportController extends BaseController {

  def translations(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationAPI.export(ctx.project, get("language")))
  }
}
