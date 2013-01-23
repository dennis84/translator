package translator.controllers

import com.roundeights.hasher.Implicits._

object ProjectController extends BaseController {

  def list = Secured { implicit ctx =>
    JsonOk(ctx.projects map(_.serialize))
  }

  def read(id: String) = WithProject(id) { implicit ctx =>
    JsonOk(env.projectAPI.show.serialize)
  }

  def create = Secured { implicit ctx =>
    env.forms.newProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(env.projectAPI.create(formData, ctx.user) map(_.serialize))
    )
  }

  def update(id: String) = Secured { implicit ctx =>
    env.forms.updateProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(env.projectAPI.update(id, formData._1, formData._2) map(_.serialize))
    )
  }

  def signUp = Open { implicit req =>
    env.forms.signUp.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData =>
        JsonOk(env.projectAPI.signup(formData._1, formData._2, formData._3) map {
          case (user, project) => project.serialize
        }) withSession("username" -> formData._2)
    )
  }
}
