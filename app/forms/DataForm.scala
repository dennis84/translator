package translator.forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import com.roundeights.hasher.Implicits._
import translator.models._

object DataForm {

  lazy val login = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  ) verifying ("Invalid user name or password", fields => fields match {
    case (u, p) => UserDAO.findOneByUsernameAndPassword(u, p.sha512).isDefined
  }))

  lazy val createUser = Form(tuple(
    "username"  -> nonEmptyText,
    "password"  -> nonEmptyText,
    "roles"     -> list(text)
  ) verifying ("Username taken", fields => fields match {
    case (u, p, r) => UserDAO.findOneByUsername(u).isEmpty
  }))

  lazy val updateUser = Form(single(
    "password" -> nonEmptyText
  ))

  lazy val signUp = Form(tuple(
    "name" -> nonEmptyText,
    "username" -> nonEmptyText,
    "password" -> nonEmptyText,
    "password2" -> nonEmptyText
  ) verifying ("Username taken", fields => fields match {
    case (n, u, p, p2) => UserDAO.findOneByUsername(u).isEmpty
  }) verifying ("Password mismatch", fields => fields match {
    case (n, u, p, p2) => p == p2
  }) verifying ("Project Name taken", fields => fields match {
    case (n, u, p, p2) => ProjectDAO.findOneByName(n).isEmpty
  }))
}
