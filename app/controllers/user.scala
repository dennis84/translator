package translator
package controllers

import scala.concurrent._

object UserController extends BaseController {

  def authenticate = Open { implicit req ⇒
    env.userForm.signin.bindFromRequest.fold(
      formWithErrors ⇒ Future(BadRequest("failed")),
      formData ⇒ {
        Future(Ok("success"))
      }
    )
  }
}
