package translator.controllers

import translator._
import translator.models._

object SearchController extends BaseController {

  def translations(project: String) = SecuredWithProject(project) { implicit ctx =>
    get("term") map { term =>
      JsonOk(TranslationAPI.search(ctx.project, term) map(_.serialize))
    } getOrElse JsonOk(List())
  }
}
