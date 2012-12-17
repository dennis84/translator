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
    JsonOk(ctx.user.toMap)
  }

  def updateCurrent = Secured { implicit ctx =>
    DataForm.updateUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        val updated = UserAPI.update(ctx.user, formData)
        JsonOk(updated.toMap)
      }
    )
  }

  def currentByProject(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(ctx.user.toMap ++ Map("roles" -> ctx.user.roles(ctx.project)))
  }

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(ProjectAPI.contributors(ctx.project).map { user =>
      user.toMap ++ Map("roles" -> user.roles(ctx.project))
    })
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.createUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = UserAPI.create(ctx.project, formData._1, formData._2, formData._3)
        JsonOk(created.toMap)
      }
    )
  }
}
