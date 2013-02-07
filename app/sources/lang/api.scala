package translator
package lang

import scala.concurrent._
import play.api.libs.json._
import translator.core._
import translator.project._

class LangApi(langRepo: LangRepo) extends Api {

  def list(p: Project): Future[JsValue] =
    langRepo.listByProject(p) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }

  def create(p: Project, code: String, name: String): Future[JsValue] = api {
    for {
      c ← langRepo.byCode(p, code)
      _ ← failsIf(c.isDefined, "language_already_exists")
      l = Lang(Doc.mkID, code, name, p.id, Some(p))
      f ← langRepo.insert(l).map(_ ⇒ l.toJson)
    } yield f
  }

  def update(id: String, code: String, name: String): Future[JsValue] = api {
    for {
      c ← langRepo.byId(id)
      _ ← failsIf(!c.isDefined, "language_not_found")
      l = c.get.copy(code = code, name = name)
      f ← langRepo.update(l).map(_ ⇒ l.toJson)
    } yield f
  }

  def delete(p: Project, id: String) = api {
    for {
      c ← langRepo.byId(id)
      _ ← failsIf(!c.isDefined, "language_not_found")
      f ← langRepo.remove(c.get).map(_ ⇒ c.get.toJson)
    } yield f
  }
}
