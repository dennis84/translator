package translator.controllers

import play.api.data._
import play.api.data.Forms._
import translator._
import translator.models._

object ProjectController extends BaseController {

  val form = Form(single(
    "name" -> nonEmptyText
  ))

  def list = SecuredIO { implicit ctx =>
    JsonOk(ctx.projects map (_.toMap))
  }

  def create = SecuredIO { implicit ctx =>
    form.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        val created = Project(formData, ctx.user.get.id, uuid)
        UserDAO.save(ctx.user.get.copy(
          roles = ctx.user.get.roles ++ List(Role("ROLE_ADMIN", created.id))
        ))

        ProjectDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  private def uuid = java.util.UUID.randomUUID.toString
}
