package translator.controllers

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import translator.models._
import translator.utils.Parser

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
        Parser.parse(formData._1, formData._2) map { row =>
          if (!TranslationDAO.findOneByProjectNameAndCode(ctx.project, row._1, formData._3).isDefined) {
            val created = Translation(formData._3, row._1, row._2, ctx.project.id, ctx.user.id, translator.models.Status.Active)
            TranslationDAO.insert(created)
          }
        }

        JsonOk(List())
      }
    )
  }
}
