package translator
package core

import scala.concurrent._
import play.api.libs.json._

class LangApi(langRepo: LangRepo) {

  def list(p: Project): Future[JsValue] =
    langRepo.listByProject(p) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }

  def create(p: Project, code: String, name: String): Future[JsValue] =
    for {
      c ← langRepo.byCode(p, code)
      if(!c.isDefined)
      l = Lang(Doc.randomID, code, name, p.id, Some(p))
      f ← langRepo.insert(l).map(_ ⇒ l.toJson)
    } yield f

  def update(id: String, code: String, name: String): Future[JsValue] =
    for {
      c ← langRepo.byId(id)
      if(c.isDefined)
      l = c.get.copy(code = code, name = name)
      f ← langRepo.update(l).map(_ ⇒ l.toJson)
    } yield f

  def delete(p: Project, id: String) =
    for {
      c ← langRepo.byId(id)
      if(c.isDefined)
      f ← langRepo.remove(c.get).map(_ ⇒ c.get.toJson)
    } yield f
}
