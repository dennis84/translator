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
    JsonOk(UserAPI.by(ctx.user).serialize)
  }

  def updateCurrent = Secured { implicit ctx =>
    DataForm.updateUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        val updated = UserAPI.update(ctx.user, formData)
        JsonOk(updated.serialize)
      }
    )
  }

  def currentByProject(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(UserAPI.by(ctx.user, ctx.project).serialize)
  }

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(ProjectAPI.contributors(ctx.project).map { user =>
      UserAPI.by(user, ctx.project).serialize
    })
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.createUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = UserAPI.create(ctx.project, formData._1, formData._2, formData._3)
        JsonOk(created.serialize)
      }
    )
  }
}
