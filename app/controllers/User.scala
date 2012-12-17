package translator.controllers

import com.roundeights.hasher.Implicits._
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
        var updated = ctx.user.copy(
          password = formData.sha512
        )

        UserDAO.save(updated)
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
        val created = User(
          formData._1,
          formData._2,
          formData._3.map(Role(_, ctx.project.id))
        )
        UserDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }
}
