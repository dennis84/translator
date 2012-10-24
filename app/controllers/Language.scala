package translator.controllers

import play.api.data._
import play.api.data.Forms._
import translator._
import translator.models._

object LanguageController extends BaseController {

  lazy val form = Form(tuple(
    "code" -> nonEmptyText,
    "name" -> text
  ))

  def list(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      JsonOk(LanguageDAO.findAllByProject(project) map (_.toMap))
    } getOrElse JsonNotFound
  }

  def create(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var created = Language(formData._1, formData._2, project.id)
          LanguageDAO.insert(created)
          JsonOk(created.toMap)
        }
      )
    } getOrElse JsonNotFound
  }

  def update(project: String, id: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      LanguageDAO.findOneById(id) map { language =>
        form.bindFromRequest.fold(
          formWithErrors => JsonBadRequest(Map("error" -> "fail")),
          formData => {
            val updated = language.copy(code = formData._1, name = formData._2)
            LanguageDAO.save(updated)
            JsonOk(updated.toMap)
          }
        )
      } getOrElse JsonNotFound
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = TODO
}
