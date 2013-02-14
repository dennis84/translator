package translator
package lang

import play.api.data._
import play.api.data.Forms._

class LangForms {

  lazy val lang = Form(tuple(
    "code" -> nonEmptyText,
    "name" -> nonEmptyText
  ))
}
