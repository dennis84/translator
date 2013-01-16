package translator.controllers

import translator.models._
import translator.forms._
import translator.models.Implicits._

object LanguageController extends BaseController {

  def list(project: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    JsonOk(LanguageAPI.list(ctx.project).map(_.serialize))
  }

  def create(project: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    DataForm.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        JsonOk(LanguageAPI.create(formData._1, formData._2, ctx.project) map(_.serialize))
      }
    )
  }

  def update(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    DataForm.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        JsonOk(LanguageAPI.update(id, formData._1, formData._2) map(_.serialize))
      }
    )
  }

  def delete(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    LanguageAPI.delete(ctx.project, id) match {
      case Some(lang) => JsonOk(lang.serialize)
      case None => JsonBadRequest(Map("error" -> "fail"))
    }
  }
}
