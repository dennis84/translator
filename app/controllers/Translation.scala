package translator.controllers

import com.mongodb.casbah.Imports._
import translator.core._

object TranslationController extends BaseController {

  def read(project: String, id: String) = WithProject(project) { implicit ctx ⇒
    JsonOk(env.transAPI.entry(ctx.project, id) map(_.serialize))
  }

  def list(project: String) = WithProject(project) { implicit ctx ⇒
    get("name") map { name ⇒
      JsonOk(env.transAPI.list(ctx.project, name) map(_.serialize))
    } getOrElse {
      val filter = Filter(
        getBoolean("untranslated"),
        getAll("untranslated_languages"),
        getBoolean("activatable"))

      JsonOk(env.transAPI.entries(filter) map(_.serialize))
    }
  }

  def create(project: String) = WithProject(project) { implicit ctx ⇒
    env.forms.translation.bindFromRequest.fold(
      formWithErrors ⇒ JsonBadRequest(formWithErrors.errors),
      formData ⇒ {
        val (code, name, text) = formData
        JsonOk(env.transAPI.create(code, name, text) map(_.serialize))
      }
    )
  }

  def update(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.forms.translation.bindFromRequest.fold(
      formWithErrors ⇒ JsonBadRequest(formWithErrors.errors),
      formData ⇒ {
        val (_, _, text) = formData
        JsonOk(env.transAPI.update(id, text) map(_.serialize))
      }
    )
  }

  def activate(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.transAPI.switch(ctx.user, ctx.project, id) match {
      case Some(trans) ⇒ JsonOk(List())
      case None ⇒ JsonBadRequest(Map("error" -> "fail"))
    }
  }

  def delete(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.transAPI.delete(ctx.project, id) match {
      case Some(trans) ⇒ JsonOk(List())
      case None ⇒ JsonBadRequest(Map("error" -> "fails"))
    }
  }

  def search(project: String) = WithProject(project) { implicit ctx ⇒
    get("term") map { term ⇒
      JsonOk(env.transAPI.search(ctx.project, term) map(_.serialize))
    } getOrElse JsonOk(List())
  }
}
