package translator.controllers

import com.mongodb.casbah.Imports._
import translator.models._
import translator.forms._
import translator.models.Implicits._

object TranslationController extends BaseController {

  def read(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationAPI.entry(id, ctx.project) map(_.serialize))
  }

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    get("name") map { name =>
      JsonOk(TranslationAPI.list(ctx.project, name) map(_.serialize))
    } getOrElse {
      val filter = Filter(
        getOr("untranslated", "false"),
        getAllOr("untranslated_languages", Seq.empty[String]),
        getOr("activatable", "false"))

      JsonOk(TranslationAPI.entries(filter) map(_.serialize))
    }
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.translation.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        TranslationAPI.create(formData._1, formData._2, formData._3)
        JsonOk(List())
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project, Role.ADMIN) { implicit ctx =>
    DataForm.translation.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "fail")),
      formData => {
        JsonOk(TranslationAPI.update(id, formData._3) map(_.serialize))
      }
    )
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
