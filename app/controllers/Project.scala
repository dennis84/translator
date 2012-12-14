package translator.controllers

import com.roundeights.hasher.Implicits._
import translator._
import translator.models._
import translator.forms._

object ProjectController extends BaseController {

  def list = Secured { (user, projects, req) =>
    JsonOk(projects map (_.toMap))
  }

  def create = Secured { (user, projects, _req) =>
    implicit val req = _req
    DataForm.newProject.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        val created = Project(formData, user.id, uuid)
        UserDAO.save(user.copy(
          roles = user.roles ++ List(Role("ROLE_ADMIN", created.id))
        ))

        ProjectDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def signUp = Open { implicit ctx =>
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
