package translator.controllers

import translator._
import translator.core._

object UserController extends BaseController {

  def authenticate = Open { implicit req =>
    env.forms.login.bindFromRequest.fold(
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
    env.forms.updateUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData =>
        JsonOk(env.userAPI.update(ctx.user, formData) map(_.serialize))
    )
  }

  def currentByProject(project: String) = WithProject(project) { implicit ctx =>
    JsonOk(ctx.user.withRoles(ctx.project).serialize)
  }

  def list(project: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    JsonOk(env.userAPI.contributors(ctx.project) map(_.serialize))
  }

  def create(project: String) = WithProject(project, Role.ADMIN) { implicit ctx =>
    env.forms.createUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val (username, password, roles) = formData
        JsonOk(env.userAPI.create(ctx.project, username, password, roles) map(_.serialize))
      }
    )
  }
}
