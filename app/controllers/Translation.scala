package translator.controllers

import com.mongodb.casbah.Imports._
import translator._
import translator.models._
import translator.forms._

object TranslationController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    val filter = Filter(
      getOr("untranslated", "false"),
      getAllOr("untranslated_languages", Seq.empty[String]),
      getOr("activatable", "false"))

    JsonOk(TranslationAPI.entries(ctx.project, filter) map(_.serialize))
  }

  def listByName(project: String, name: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationAPI.list(ctx.project, name) map(_.serialize))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.translation.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        TranslationAPI.create(formData._1, formData._2, formData._3, ctx.project, ctx.user)
        JsonOk(List())
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    TranslationAPI.by(id) map { trans =>
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          TranslationAPI.update(trans, formData._3)
          JsonOk(List())
        }
      )
    } getOrElse JsonNotFound
  }

  def activate(project: String, id: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    TranslationAPI.switch(ctx.user, ctx.project, id) match {
      case Some(trans) => JsonOk(List())
      case None => JsonBadRequest(Map("error" -> "fail"))
    }
  }

  def delete(project: String, id: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    TranslationAPI.delete(ctx.project, id) match {
      case Some(trans) => JsonOk(List())
      case None => JsonBadRequest(Map("error" -> "fails"))
    }
  }
}
