package translator.controllers

object ExportController extends BaseController {

  def translations(project: String) = WithProject(project) { implicit ctx =>
    JsonOk(env.transAPI.export(ctx.project, getOr("language", "")))
  }
}
