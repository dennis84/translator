package translator.controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import validation.{ValidationError, Valid, Invalid, Constraint}
import com.roundeights.hasher.Implicits._
import translator._
import translator.models._
import translator.forms._

object UserController extends BaseController {

  def authenticate = Open { implicit ctx =>
    DataForm.login.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => JsonOk(List()) withSession ("username" -> formData._1)
    )
  }

  def logout = Open { implicit ctx =>
    JsonOk(List()) withNewSession
  }

  def current = Secured { implicit ctx =>
    ctx.user map (user => JsonOk(user.toMap)) getOrElse JsonUnauthorized
  }

  def updateCurrent = Secured { implicit ctx =>
    DataForm.updateUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        var updated = ctx.user.get.copy(
          password = formData.sha512
        )

        UserDAO.save(updated)
        JsonOk(updated.toMap)
      }
    )
  }

  def currentByProject(project: String) = SecuredWithProject(project) { implicit ctx =>
    ctx.user map { user =>
      JsonOk(user.toMap ++ Map("roles" -> user.roles(ctx.project.get)))
    } getOrElse JsonUnauthorized
  }

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(ctx.project.get.contributors.map { user =>
      user.toMap ++ Map("roles" -> user.roles(ctx.project.get))
    })
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.createUser.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = User(
          formData._1,
          formData._2,
          formData._3.map(Role(_, ctx.project.get.id))
        )
        UserDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }
}
