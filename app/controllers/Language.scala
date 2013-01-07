package translator.controllers

import translator.models._
import translator.forms._
import translator.models.Implicits._

object LanguageController extends BaseController {

  def list(project: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    JsonOk(LanguageAPI.list(ctx.project).map(_.serialize))
  }

  def create(project: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    DataForm.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        var created = LanguageAPI.create(formData._1, formData._2, ctx.project)
        JsonOk(created.serialize)
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    LanguageAPI.by(id, ctx.project) map { language =>
      DataForm.language.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(formWithErrors.errors),
        formData => {
          val updated = LanguageAPI.update(language, formData._1, formData._2)
          JsonOk(updated.serialize)
        }
      )
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = TODO
}
