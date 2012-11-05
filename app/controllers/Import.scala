package translator.controllers

object ImportController extends BaseController {

  def entries(project: String) = SecuredIO { implicit ctx =>
    Ok("import")
  }
}
