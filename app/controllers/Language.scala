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

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(LanguageDAO.findAllByProject(ctx.project.get) map (_.toMap))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    form.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        var created = Language(formData._1, formData._2, ctx.project.get.id)
        LanguageDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
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
  }

  def delete(project: String, id: String) = TODO
}
