package translator.controllers

object SearchController extends BaseController {

  def translations(project: String) = WithProject(project) { implicit ctx =>
    get("term") map { term =>
      JsonOk(env.transAPI.search(ctx.project, term) map(_.serialize))
    } getOrElse JsonOk(List())
  }
}
