package translator.controllers

import translator.utils.Importer

object ImportController extends BaseController {

  def entries(project: String) = SecuredIO(parse.multipartFormData) { implicit ctx =>
    ctx.req.body.file("file") map { file =>
      Importer.parse(file)
      JsonOk(List())
    } getOrElse JsonBadRequest(Map("error" -> "no file"))
  }
}
