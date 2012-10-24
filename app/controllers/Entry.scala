package translator.controllers

import play.api.data._
import play.api.data.Forms._
import translator._
import translator.models._

object EntryController extends BaseController {

  lazy val form = Form(tuple(
    "name" -> nonEmptyText,
    "description" -> text,
    "translations" -> Forms.list(tuple(
      "code" -> nonEmptyText,
      "text" -> text
    ))
  ))

  def list(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      JsonOk(EntryDAO.findAllByProject(project) map (_.toMap))
    } getOrElse JsonNotFound
  }

  def create(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var created = Entry(formData._1, formData._2, project.id, (formData._3 map { data =>
            new Translation(data._1, data._2, ctx.user.get.id, true)
          }))

          EntryDAO.insert(created)
          JsonOk(created.toMap)
        }
      )
    } getOrElse JsonNotFound
  }

  def update(project: String, id: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      EntryDAO.findOneById(id) map { entry =>
        form.bindFromRequest.fold(
          formWithErrors => JsonBadRequest(Map("error" -> "fail")),
          formData => {
            var updated = entry.copy(
              name = formData._1,
              description = formData._2,
              translations = (formData._3 map { data =>
                new Translation(data._1, data._2, ctx.user.get.id, true)
              })
            )

            EntryDAO.save(updated)
            JsonOk(updated.toMap)
          }
        )
      } getOrElse JsonNotFound
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = TODO
}
