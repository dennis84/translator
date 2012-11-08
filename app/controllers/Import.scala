package translator.controllers

import play.api.data._
import play.api.data.Forms._
import translator.utils.Importer

object ImportController extends BaseController {

  val form = Form(tuple(
    "language" -> text,
    "content" -> text
  ))

  def entries(project: String) = SecuredIO { implicit ctx =>
    form.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(Map("error" -> "form error")),
      formData => {
        JsonOk(List(formData._2))
      }
    )
  }
}
