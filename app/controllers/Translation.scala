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
      JsonOk(entry.translationsFixed map (_.toMap))
    }) getOrElse JsonNotFound
  }

  def create = SecuredIO { implicit ctx =>
    JsonOk(List())
  }

  def update(entryId: String, id: String) = SecuredIO { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(entryId)
      project <- ctx.projects.find(_.id == entry.projectId)
      trans   <- TranslationDAO.findOneById(id)
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
}
