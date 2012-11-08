package translator.controllers

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import translator.models._

object ImportController extends BaseController {

  val form = Form(tuple(
    "language" -> text,
    "content" -> text
  ))

  def entries(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "form error")),
        formData => {
          val response = (Json parse formData._2).as[Map[String, String]].map { row =>
            EntryDAO.findOneByNameAndProject(row._1, project) map { entry =>
              entry
            } getOrElse {
              val created = Entry(row._1, "", project.id, List(Translation(formData._1, row._2, ctx.user.get.id, true)))
              EntryDAO.insert(created)

              created
            }
          }.toList.map(_.toMap)

          JsonOk(response)
        }
      )
    } getOrElse JsonNotFound
  }
}
