package translator
package trans

import play.api.data._
import play.api.data.Forms._

class TransForms {

  lazy val trans = Form(tuple(
    "code" -> text,
    "name" -> nonEmptyText,
    "text" -> text
  ))

  lazy val inject = Form(tuple(
    "content" -> text,
    "type" -> nonEmptyText,
    "code" -> nonEmptyText
  ))
}
