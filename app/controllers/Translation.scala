package translator.controllers

import com.mongodb.casbah.Imports._
import translator._
import translator.models._
import translator.forms._

object TranslationController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      project <- ctx.project
      lang <- LanguageDAO.findFirstByProject(project).headOption
    } yield {
      val filter = Filter(
        getOr("untranslated", "false"),
        getAllOr("untranslated_languages", Seq.empty[String]),
        getOr("activatable", "false"))

      JsonOk(TranslationDAO.findAllByProjectAndFilter(project, filter, lang.code) map (_.toMap))
    }) getOrElse JsonBadRequest(List())
  }

  def listByName(project: String, name: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationDAO.findAllByProjectAndName(ctx.project.get, name) map (_.toMap))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      project <- ctx.project
      lang <- LanguageDAO.findFirstByProject(project).headOption
      user    <- ctx.user
    } yield {
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          val code = if (formData._1 == "") lang.code else formData._1
          val status  = if (user.roles(project).contains("ROLE_ADMIN")) translator.models.Status.Active else translator.models.Status.Inactive
          val created = Translation(code, formData._2, formData._3, project.id, user.id, status)
          TranslationDAO.insert(created)
          JsonOk(created.toMap)
        }
      )
    }) getOrElse JsonNotFound
  }

  def update(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      project <- ctx.project
      user <- ctx.user
      translation <- TranslationDAO.findOneById(id)
      if (user.roles(project).contains("ROLE_ADMIN"))
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
      project <- ctx.project
      user <- ctx.user
      translation <- TranslationDAO.findOneById(id)
      old <- TranslationDAO.findOneBy(project, translation.name, translation.code)
      user <- ctx.user
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      val updated = translation.copy(status = translator.models.Status.Active)
      TranslationDAO.save(updated)
      TranslationDAO.remove(old)
      JsonOk(updated.toMap)
    }) getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      project <- ctx.project
      user    <- ctx.user
      translation <- TranslationDAO.findOneById(id)
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      if (translation.status == translator.models.Status.Active) {
        TranslationDAO.removeAllByProjectAndName(project, translation.name)
      } else {
        TranslationDAO.remove(translation)
      }

      JsonOk(translation.toMap)
    }) getOrElse JsonNotFound
  }
}
