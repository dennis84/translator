package translator.controllers

import play.api.data._
import play.api.data.Forms._
import translator._
import translator.models._

object ImportController extends BaseController {

  val form = Form(tuple(
    "content"  -> text,
    "type"     -> nonEmptyText,
    "language" -> nonEmptyText
  ))

  def translations(project: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    form.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val (c, t, l) = formData
        JsonOk(TranslationAPI.inject(ctx.project, ctx.user, c, t, l) map(_.serialize))
      }
    )
  }
}
