package translator
package core

import play.api.libs.json._
import play.api.i18n.Messages
import scala.concurrent._

trait Api {

  class ApiException(m: String) extends RuntimeException(m) {
    def toJson = Json.obj("error" -> m)
  }

  def get[T](v: Option[T], m: String): Future[T] =
    v.map(Future.successful).getOrElse(Future.failed(new ApiException(m)))

  def failsIf(v: Boolean, m: String): Future[Boolean] =
    if (v) Future.failed(new ApiException(m))
    else Future.successful(true)

  def api(f: ⇒ Future[JsValue]): Future[JsValue] = f.recover {
    case e: ApiException ⇒ e.toJson
  }
}
