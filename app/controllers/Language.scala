package translator.controllers

import translator.core._

object LanguageController extends BaseController {

  def list(project: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    JsonOk(env.langAPI.list(ctx.project).map(_.serialize))
  }

  def create(project: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    env.forms.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(env.langAPI.create(formData._1, formData._2, ctx.project) map(_.serialize))
    )
  }

  def update(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    env.forms.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(env.langAPI.update(id, formData._1, formData._2) map(_.serialize))
    )
  }

  def delete(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    env.langAPI.delete(ctx.project, id) match {
      case Some(lang) => JsonOk(lang.serialize)
      case None => JsonBadRequest(Map("error" -> "fail"))
    }
  }
}
