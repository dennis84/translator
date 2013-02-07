package translator
package trans

import scala.concurrent._
import play.api.libs.json._
import scala.concurrent.duration._
import translator.core._
import translator.project._
import translator.lang._
import translator.user._
import translator.trans.list._

class TransApi(
  transRepo: TransRepo,
  langRepo: LangRepo) extends Api {

  def entry(p: Project, id: String): Future[JsValue] = api {
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← get(maybeTrans, "entry_not_found")
      langs ← langRepo.listByProject(p)
      children ← transRepo.listByName(p, trans.name)
      entry = Entry(trans, p, langs, children).toJson
    } yield entry
  }

  def entries(p: Project, f: Filter): Future[JsValue] =
    for {
      langs ← langRepo.listByProject(p)
      translations ← transRepo.listByProject(p)
      entries = translations.mkEntries(langs)
    } yield {
      Json.toJson(f.filter(entries).map(_.toJson))
    }

  def export(p: Project, code: String): Future[JsValue] =
    transRepo.listActive(p, code).map { list ⇒
      Json.toJson(list.map(t ⇒ Json.obj(t.name -> t.text)))
    }

  def list(p: Project, name: String): Future[JsValue] =
    for {
      langs ← langRepo.listByProject(p)
      translations ← transRepo.listByName(p, name).map { list ⇒
        Json.toJson(list.mkComplete(langs).map(_.toJson))
      }
    } yield translations

  def search(p: Project, term: String): Future[JsValue] =
    for {
      langs ← langRepo.listByProject(p)
      translations ← transRepo.listLike(p, term)
      all ← transRepo.listByNames(p, translations.map(_.name))
      entries = all.mkEntries(langs)
    } yield {
      Json.toJson(entries.map(_.toJson))
    }

  def create(
    project: Project,
    user: User,
    code: String,
    name: String,
    text: String
  ): Future[JsValue] =
    for {
      maybeTrans ← transRepo.byNameAndCode(project, name, code)
      trans = Trans(code, name, text, user, project)
      status = if(user.isAdmin && !maybeTrans.isDefined) Status.Active
               else Status.Inactive
      transWithStatus = trans.copy(status = status)
      result ← transRepo.insert(transWithStatus)map(_ ⇒ transWithStatus.toJson)
    } yield result

  def update(id: String, text: String): Future[JsValue] = api {
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← get(maybeTrans, "translation_not_found")
      updated = trans.copy(text = text)
      result ← transRepo.update(updated).map(_ ⇒ updated.toJson)
    } yield result
  }

  def switch(p: Project, id: String): Future[JsValue] = api {
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← get(maybeTrans, "translation_not_found")
      maybeBefore ← transRepo.activated(p, trans.name, trans.code)
      before ← get(maybeBefore, "translation_not_found")
      updated = trans.copy(status = Status.Active)
      result ← transRepo.update(updated).map(_ ⇒ updated.toJson)
      removed ← transRepo.remove(before)
    } yield result
  }

  def delete(p: Project, id: String): Future[JsValue] = api {
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← get(maybeTrans, "translation_not_found")
      result ← transRepo.remove(trans).map(_ ⇒ trans.toJson)
    } yield result
  }

  // def inject(p: Project, u: User, content: String, t: String, code: String): List[Translation] =
  //   Parser.parse(content, t).map { row ⇒
  //     val (name, text) = row
  //     transDAO.byNameAndCode(p, name, code) match {
  //       case Some(trans) ⇒ Some(trans.copy(status = Status.Skipped))
  //       case None ⇒ for {
  //         c ← langDAO.validateCode(p, code)
  //         t = Translation(c, name, text, u, p).copy(status = Status.Active)
  //         _ ← transDAO.insert(t)
  //       } yield t
  //     }
  //   }.flatten
}
