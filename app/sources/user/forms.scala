package translator
package user

import play.api.data._
import play.api.data.Forms._

class UserForms {

  lazy val signin = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  ))

  lazy val me = Form(tuple(
    "password" -> nonEmptyText,
    "password2" -> nonEmptyText
  ) verifying("password_mismatch", fields ⇒ fields match {
    case (p, p2) ⇒ p == p2
  }))

  lazy val create = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText,
    "roles"    -> list(text)
  ))

  lazy val update = Form(single(
    "roles" -> list(text)
  ))

  lazy val add = Form(tuple(
    "username" -> nonEmptyText,
    "roles"    -> list(text)
  ))
}
