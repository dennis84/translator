package translator.controllers

import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._
import translator._
import translator.models._

object TranslationController extends BaseController {

  lazy val form = Form(single(
    "translations" -> Forms.list(tuple(
      "code" -> nonEmptyText,
      "text" -> text
    ))
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

  def update = SecuredIO { implicit ctx =>
    JsonOk(List())
  }
}
