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

  lazy val loginForm = DataForm.login
  lazy val createForm = DataForm.createUser
  lazy val updateForm = DataForm.updateUser

  def authenticate = OpenIO { implicit ctx =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest("authentication failed"),
      formData => JsonOk(List()) withSession ("username" -> formData._1)
    )
  }

  def logout = OpenIO { implicit ctx =>
    JsonOk(List()) withNewSession
  }

  def current = SecuredIO { implicit ctx =>
    ctx.user map (user => JsonOk(user.toMap)) getOrElse JsonUnauthorized
  }

  def updateCurrent = SecuredIO { implicit ctx =>
    updateForm.bindFromRequest.fold(
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

  def currentByProject(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      ctx.user map { user =>
        JsonOk(user.toMap ++ Map("roles" -> user.roles(project)))
      } getOrElse JsonUnauthorized
    } getOrElse JsonNotFound
  }

  def list(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      JsonOk(project.contributors.map { user =>
        user.toMap ++ Map("roles" -> user.roles(project))
      })
    } getOrElse JsonNotFound
  }

  def create(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      createForm.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          val created = User(
            formData._1,
            formData._2,
            formData._3.map(Role(_, project.id))
          )
          UserDAO.insert(created)
          JsonOk(created.toMap)
        }
      )
    } getOrElse JsonNotFound
  }
}
