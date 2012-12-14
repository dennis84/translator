package translator.controllers

import translator._
import translator.models._
import translator.forms._

object LanguageController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(LanguageAPI.list(ctx.project.get))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        var created = Language(formData._1, formData._2, ctx.project.get.id)
        LanguageDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
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
