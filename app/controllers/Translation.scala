package translator.controllers

import com.mongodb.casbah.Imports._
import translator._
import translator.models._
import translator.forms._

object TranslationController extends BaseController {

  def list(entryId: String) = Secured { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
    } yield {
      JsonOk(entry.translations.map(_.toMap))
    }) getOrElse JsonNotFound
  }

  def create(entryId: String) = Secured { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
      user    <- ctx.user
    } yield {
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          val active  = if (user.roles(project).contains("ROLE_ADMIN")) true else false
          val created = Translation(formData._1, formData._2, user.id, active)
          TranslationDAO.insert(created)
          EntryDAO.save(entry.copy(translationIds = entry.translationIds ++ List(created.id)))
          JsonOk(created.toMap)
        }
      )
    }) getOrElse JsonNotFound
  }

  def update(entryId: String, id: String) = Secured { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
      trans   <- TranslationDAO.findOneById(id)
      user    <- ctx.user
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      DataForm.translation.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var updated = trans.copy(text = formData._2)
          TranslationDAO.save(updated)
          JsonOk(updated.toMap)
        }
      )
    }) getOrElse JsonNotFound
  }

  def activate(entryId: String, id: String) = Secured { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
      trans   <- TranslationDAO.findOneById(id)
      user    <- ctx.user
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      val updated = trans.copy(active = true)
      entry.translations find { t =>
        t.code == trans.code && t.active == true
      } map (t => TranslationDAO.remove(t))
      TranslationDAO.save(updated)
      JsonOk(updated.toMap)
    }) getOrElse JsonNotFound
  }

  def delete(entryId: String, id: String) = Secured { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
      trans   <- TranslationDAO.findOneById(id)
      user    <- ctx.user
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      EntryDAO.save(entry.copy(translationIds = entry.translationIds filterNot (_ == trans.id)))
      TranslationDAO.remove(trans)
      JsonOk(trans.toMap)
    }) getOrElse JsonNotFound
  }
}
