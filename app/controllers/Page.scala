package translator.controllers

import play.api._
import play.api.mvc._
import translator._
import translator.core._

object PageController extends Controller {
  
  def home = Action {
    Ok(views.html.admin())
  }
}
