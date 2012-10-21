package translator.controllers

import play.api._
import play.api.mvc._

object TranslationController extends BaseController {

  def list = SecuredIO { implicit ctx =>
    Ok("")
  }
}
