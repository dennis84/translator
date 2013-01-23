package translator.controllers

import translator._
import translator.core._
import translator.forms._

object ExportController extends BaseController {

  def translations(project: String) = WithProject(project) { implicit ctx =>
    JsonOk(TranslationAPI.export(ctx.project, getOr("language", "")))
  }
}
