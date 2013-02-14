package translator
package core

import play.api.libs.json._
import play.api.i18n._
import play.api.data.FormError

object Errors {

  def apply(messages: (String, String)*): JsValue =
    Json.toJson(messages.map { msg ⇒
      Json.obj("name" -> msg._1, "message" -> Messages(msg._2))
    })

  def fromForm(errors: Seq[FormError]): JsValue = Json.toJson(errors.map {
    case e if("" == e.message) ⇒ None
    case e if("" == e.key) ⇒ Some(this.apply("global" -> e.message))
    case e ⇒ Some(this.apply(e.key -> e.message))
  }.flatten)
}
