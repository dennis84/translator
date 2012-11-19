package translator.controllers

import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._
import translator._
import translator.models._

object TranslationController extends BaseController {

  lazy val form = Form(tuple(
    "code" -> nonEmptyText,
    "text" -> text
  ))

  def list(entryId: String) = SecuredIO { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
    } yield {
      JsonOk(entry.translations map (_.toMap))
    }) getOrElse JsonNotFound
  }

  def create(entryId: String) = SecuredIO { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
    } yield {
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var created = Translation(formData._1, formData._2, ctx.user.get.id, false)
          TranslationDAO.insert(created)
          EntryDAO.save(entry.copy(translationIds = entry.translationIds ++ List(created.id)))
          JsonOk(created.toMap)
        }
      )
    }) getOrElse JsonNotFound
  }

  def update(entryId: String, id: String) = SecuredIO { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
      trans   <- TranslationDAO.findOneById(id)
      user    <- ctx.user
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var updated = trans.copy(text = formData._2)
          TranslationDAO.save(updated)
          JsonOk(updated.toMap)
        }
      )
    }) getOrElse JsonNotFound
  }

  def delete(entryId: String, id: String) = SecuredIO { implicit ctx =>
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
