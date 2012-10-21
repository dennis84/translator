package translator.forms

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import validation.{ValidationError, Valid, Invalid, Constraint}
import com.roundeights.hasher.Implicits._
import translator._
import translator.models._

object DataForm {

  private def accountExists = Constraint[(String, String)]("auth.signin.invalid_account") {
    case data if UserDAO.findOneByUsernameAndPassword(data._1, data._2.sha512).isDefined ⇒ Valid
    case _ ⇒ Invalid("security.signin.invalid_account")
  }

  lazy val login = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  ) verifying (accountExists))
}
