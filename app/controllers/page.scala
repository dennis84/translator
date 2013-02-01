package translator
package controllers

import scala.concurrent._
import play.api._
import play.api.mvc._

object PageController extends BaseController {

  def home = Open { implicit ctx ⇒
    Future(Ok("foo" + ctx.user.username))
  }
}
