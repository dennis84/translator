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
    ctx.user map { user =>
      JsonOk(user.toMap)
    } getOrElse JsonUnauthorized
  }
}
