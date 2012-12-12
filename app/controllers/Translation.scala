package translator.controllers

import com.mongodb.casbah.Imports._
import translator._
import translator.models._
import translator.forms._

object TranslationController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      lang <- LanguageDAO.findFirstByProject(ctx.project.get).headOption
      project <- ctx.project
    } yield {
      JsonOk(TranslationDAO.findAllByProjectAndCode(project, lang.code) map(_.toMap))
    }) getOrElse JsonBadRequest(List())
  }

  def listByName(project: String, name: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationDAO.findAllByProjectAndName(ctx.project.get, name) map (_.toMap))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      project <- ctx.project
      user    <- ctx.user
    } yield {
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          val status  = if (user.roles(project).contains("ROLE_ADMIN")) translator.models.Status.Active else translator.models.Status.Inactive
          val created = Translation(formData._1, formData._2, formData._3, project.id, user.id, status)
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
      TranslationDAO.remove(translation)
      JsonOk(translation.toMap)
    }) getOrElse JsonNotFound
  }
}
