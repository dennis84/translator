package translator
package core

import play.api.libs.json._
import play.api.i18n.Messages

object errors {

  def validate(e: Option[Error]*)(func: ⇒ JsValue) = {
    val errors = e.toList.flatten
    if(errors.length > 0) Json.toJson(errors.map(_.toJson))
    else func
  }

  class Error(name: String, message: String) {

    def toJson = Json.obj(name -> message)

    def when(cond: Boolean) =
      if(cond) Some(this)
      else None
  }

  object Error {

    def apply(name: String) = new Error(name, Messages(name))
  }
}
