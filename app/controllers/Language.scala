package translator.controllers

import translator._
import translator.models._
import translator.forms._

object LanguageController extends BaseController {

  def list(project: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    JsonOk(LanguageAPI.list(ctx.project))
  }

  def create(project: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    DataForm.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        var created = Language(formData._1, formData._2, ctx.project.id)
        LanguageDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    LanguageDAO.findOneById(id) map { language =>
      DataForm.language.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(formWithErrors.errors),
        formData => {
          val updated = language.copy(code = formData._1, name = formData._2)
          LanguageDAO.save(updated)
          JsonOk(updated.toMap)
        }
      )
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = TODO
}
