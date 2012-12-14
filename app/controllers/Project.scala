package translator.controllers

import com.roundeights.hasher.Implicits._
import translator._
import translator.models._
import translator.forms._

object ProjectController extends BaseController {

  def list = Secured { implicit ctx =>
    JsonOk(ctx.projects map (_.toMap))
  }

  def create = Secured { implicit ctx =>
    DataForm.newProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = Project(formData, ctx.user.id, uuid)
        UserDAO.save(ctx.user.copy(
          roles = ctx.user.roles ++ List(Role("ROLE_ADMIN", created.id))
        ))

        ProjectDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def signUp = Open { implicit req =>
    DataForm.signUp.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val _user = User(formData._2, formData._3 sha512)
        val project = Project(formData._1, _user.id, uuid)
        val user = _user.copy(roles = _user.roles ++ List(Role("ROLE_ADMIN", project.id)))

        UserDAO.insert(user)
        ProjectDAO.insert(project)
        JsonOk(List()) withSession ("username" -> formData._2)
      }
    )
  }

  private def uuid = java.util.UUID.randomUUID.toString
}
