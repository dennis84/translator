package translator.controllers

import com.roundeights.hasher.Implicits._
import translator._
import translator.core._
import translator.forms._

object ProjectController extends BaseController {

  def list = Secured { implicit ctx =>
    JsonOk(ctx.projects map(_.serialize))
  }

  def read(id: String) = WithProject(id) { implicit ctx =>
    JsonOk(ProjectAPI.show.serialize)
  }

  def create = Secured { implicit ctx =>
    DataForm.newProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(ProjectAPI.create(formData, ctx.user) map(_.serialize))
    )
  }

  def update(id: String) = Secured { implicit ctx =>
    DataForm.updateProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(ProjectAPI.update(id, formData._1, formData._2) map(_.serialize))
    )
  }

  def signUp = Open { implicit req =>
    DataForm.signUp.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(ProjectAPI.signup(formData._1, formData._2, formData._3) map {
          case (user, project) => project.serialize
        }) withSession("username" -> formData._2)
    )
  }
}
