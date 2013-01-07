package translator.controllers

import translator._
import translator.models._
import translator.forms._

object UserController extends BaseController {

  def authenticate = Open { implicit req =>
    DataForm.login.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => JsonOk(List()) withSession ("username" -> formData._1)
    )
  }

  def logout = Open { implicit ctx =>
    JsonOk(List()) withNewSession
  }

  def current = Secured { implicit ctx =>
    JsonOk(ctx.user.serialize)
  }

  def updateCurrent = Secured { implicit ctx =>
    DataForm.updateUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        JsonOk(UserAPI.update(ctx.user, formData) map(_.serialize))
      }
    )
  }

  def currentByProject(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(ctx.user.withRoles(ctx.project).serialize)
  }

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(UserAPI.contributors(ctx.project) map(_.serialize))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.createUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        JsonOk(UserAPI.create(ctx.project, formData._1, formData._2, formData._3) map(_.serialize))
      }
    )
  }
}
