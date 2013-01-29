package translator.controllers

import play.api.data._
import play.api.data.Forms._

object EmbedController extends BaseController {

  val form = Form(single(
    "translations" -> list(tuple(
      "name" -> nonEmptyText,
      "text" -> text
    ))
  ))

  def index(project: String) = WithProject(project) { implicit ctx ⇒
    Ok(views.html.embed.index(env.projectAPI.show, env.langAPI.list(ctx.project)))
  }

  def translations(project: String, lang: String) = WithProject(project) { implicit ctx ⇒
    Ok(views.html.embed.list(env.projectAPI.show, lang, env.transAPI.listByLang(ctx.project, lang)))
  }

  def submit(project: String, lang: String) = WithProject(project) { implicit ctx ⇒
    form.bindFromRequest.fold(
      formWithErrors ⇒ BadRequest(""),
      formData ⇒ {
        formData.map { case (name, text) ⇒
          env.transAPI.create(lang, name, text)
        }

        Ok("Translations Send")
      }
    )
  }
}
