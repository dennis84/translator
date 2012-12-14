package translator.controllers

import com.mongodb.casbah.Imports._
import translator._
import translator.models._
import translator.forms._

object TranslationController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    LanguageDAO.findFirstByProject(ctx.project).headOption map { lang =>
      val filter = Filter(
        getOr("untranslated", "false"),
        getAllOr("untranslated_languages", Seq.empty[String]),
        getOr("activatable", "false"))

      JsonOk(TranslationDAO.findAllByProjectAndFilter(ctx.project, filter, lang.code) map (_.toMap))
    } getOrElse JsonBadRequest(List())
  }

  def listByName(project: String, name: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationDAO.findAllByProjectAndName(ctx.project, name) map (_.toMap))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    LanguageDAO.findFirstByProject(ctx.project).headOption map { lang =>
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          val code = if (formData._1 == "") lang.code else formData._1
          val status  = if (ctx.user.roles(ctx.project).contains("ROLE_ADMIN")) translator.models.Status.Active else translator.models.Status.Inactive
          val created = Translation(code, formData._2, formData._3, ctx.project.id, ctx.user.id, status)
          TranslationDAO.insert(created)
          JsonOk(created.toMap)
        }
      )
    } getOrElse JsonNotFound
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
          JsonOk(updated.toMap)
        }
      )
    }) getOrElse JsonNotFound
  }

  def activate(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      translation <- TranslationDAO.findOneById(id)
      old <- TranslationDAO.findOneBy(ctx.project, translation.name, translation.code)
      if (ctx.user.roles(ctx.project).contains("ROLE_ADMIN"))
    } yield {
      val updated = translation.copy(status = translator.models.Status.Active)
      TranslationDAO.save(updated)
      TranslationDAO.remove(old)
      JsonOk(updated.toMap)
    }) getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      translation <- TranslationDAO.findOneById(id)
      if (ctx.user.roles(ctx.project).contains("ROLE_ADMIN"))
    } yield {
      if (translation.status == translator.models.Status.Active) {
        TranslationDAO.removeAllByProjectAndName(ctx.project, translation.name)
      } else {
        TranslationDAO.remove(translation)
      }

      JsonOk(translation.toMap)
    }) getOrElse JsonNotFound
  }
}
