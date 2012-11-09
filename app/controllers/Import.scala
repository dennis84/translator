package translator.controllers

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import translator.models._
import translator.utils.Parser

object ImportController extends BaseController {

  val form = Form(tuple(
    "content"  -> text,
    "type"     -> nonEmptyText,
    "language" -> nonEmptyText
  ))

  def entries(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "form error")),
        formData => {
          Parser.parse(formData._1, formData._2) map { row =>
            EntryDAO.findOneByNameAndProject(row._1, project) map { entry =>
              if (!entry.translations.exists(_.code == formData._1)) {
                val updated = entry.copy(translations = entry.translations ++ List(Translation(formData._1, row._2, ctx.user.get.id, true)))
                EntryDAO.save(updated)
              }
            } getOrElse {
              val created = Entry(row._1, "", project.id, List(Translation(formData._1, row._2, ctx.user.get.id, true)))
              EntryDAO.insert(created)
            }
          }

          JsonOk(List())
        }
      )
    } getOrElse JsonNotFound
  }
}
