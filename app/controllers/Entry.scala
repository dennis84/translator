package translator.controllers

import play.api.data._
import play.api.data.Forms._
import translator._
import translator.models._
import translator.forms._

object EntryController extends BaseController {

  def read(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(EntryDAO.findOneById(id) map (_.toMap) getOrElse Map())
  }

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    val filter = Filter(
      getOr("untranslated", "false"),
      getAllOr("untranslated_languages", Seq.empty[String]))

    JsonOk(EntryDAO.findAllByProjectAndFilter(ctx.project.get, filter) map (_.toMap))
  }

  def create(project: String) = SecuredWithProject(project) { implicit ctx =>
    DataForm.entry.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        var created = Entry(formData._1, formData._2, ctx.project.get.id)
        EntryDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    EntryDAO.findOneById(id) map { entry =>
      DataForm.entry.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var updated = entry.copy(
            name = formData._1,
            description = formData._2
          )

          EntryDAO.save(updated)
          JsonOk(updated.toMap)
        }
      )
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = SecuredWithProject(project) { implicit ctx =>
    (for {
      entry <- EntryDAO.findOneById(id)
      user  <- ctx.user
      if (user.roles(ctx.project.get).contains("ROLE_ADMIN"))
    } yield {
      EntryDAO.remove(entry)
      JsonOk(entry.toMap)
    }) getOrElse JsonNotFound
  }

  def export(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(List())
    //JsonOk(EntryDAO.findAllByProject(ctx.project.get).map { entry =>
      //entry.name -> entry.translations.find(_.code == getOr("language", "en")).map(_.text).getOrElse("")
    //}.toMap)
  }
}
