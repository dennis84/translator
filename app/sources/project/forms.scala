package translator
package project

import play.api.data._
import play.api.data.Forms._

class ProjectForms {

  lazy val signup = Form(tuple(
    "name"      -> nonEmptyText,
    "username"  -> nonEmptyText,
    "password"  -> nonEmptyText,
    "password2" -> nonEmptyText
  ) verifying ("error.password_mismatch", fields ⇒ fields match {
    case (n, u, p, p2) ⇒ p == p2
  }))

  lazy val create = Form(single(
    "name" -> nonEmptyText
  ))

  lazy val update = Form(tuple(
    "repo" -> text,
    "open" -> boolean
  ))
}
