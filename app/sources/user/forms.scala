package translator
package user

import play.api.data._
import play.api.data.Forms._

class UserForm(userRepo: UserRepo) {

  lazy val signin = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  ))
}
