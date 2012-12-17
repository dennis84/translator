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

    JsonOk(TranslationAPI.entries(ctx.project, filter))
  }

  def listByName(project: String, name: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationAPI.list(ctx.project, name))
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

  def update(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      translation <- TranslationDAO.findOneById(id)
      if (ctx.user.roles(ctx.project).contains("ROLE_ADMIN"))
    } yield {
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          val updated = translation.copy(text = formData._3)
          TranslationDAO.save(updated)
          //JsonOk(updated.toMap)
          JsonOk(List())
        }
      )
    }) getOrElse JsonNotFound
  }

  def activate(project: String, id: String) = SecuredWithProject(project, List("ROLE_ADMIN")) { implicit ctx =>
    TranslationAPI.switch(ctx.user, ctx.project, id) match {
      case Some(trans) => JsonOk(List())
      case None => JsonBadRequest(Map("error" -> "fail"))
    }
  }

  def delete(project: String, id: String) = SecuredWithProject(project, List("ROLE_ADMIN")) { implicit ctx =>
    TranslationAPI.delete(ctx.project, id) match {
      case Some(trans) => JsonOk(List())
      case None => JsonBadRequest(Map("error" -> "fails"))
    }
  }
}
