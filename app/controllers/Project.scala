package translator.controllers

import com.roundeights.hasher.Implicits._
import translator._
import translator.models._
import translator.forms._

object ProjectController extends BaseController {

  def list = Secured { implicit ctx =>
    JsonOk(ProjectAPI.list(ctx.projects) map(_.serialize))
  }

  def create = Secured { implicit ctx =>
    DataForm.newProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = ProjectAPI.create(formData, ctx.user)
        JsonOk(created.serialize)
      }
    )
  }

  def signUp = Open { implicit req =>
    DataForm.signUp.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = ProjectAPI.signup(formData._1, formData._2, formData._3)
        JsonOk(created.serialize) withSession ("username" -> formData._2)
      }
    )
  }
}
