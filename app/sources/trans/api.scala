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

  def entry(p: Project, id: String) =
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← maybeTrans.future
      langs ← langRepo.listByProject(p)
      children ← transRepo.listByName(p, trans.name)
      entry = Entry(trans, p, langs, children).toJson
    } yield entry

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

  def update(id: String, text: String): Future[JsValue] =
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← maybeTrans.future
      updated = trans.copy(text = text)
      result ← transRepo.update(updated).map(_ ⇒ updated.toJson)
    } yield result

  def switch(p: Project, id: String): Future[JsValue] =
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← maybeTrans.future
      maybeBefore ← transRepo.activated(p, trans.name, trans.code)
      before ← maybeBefore.future
      updated = trans.copy(status = Status.Active)
      result ← transRepo.update(updated).map(_ ⇒ updated.toJson)
      removed ← transRepo.remove(before)
    } yield result

  def delete(p: Project, id: String): Future[JsValue] =
    for {
      maybeTrans ← transRepo.byId(id)
      trans ← maybeTrans.future
      result ← transRepo.remove(trans).map(_ ⇒ trans.toJson)
    } yield result

  def inject(
    project: Project,
    user: User,
    content: String,
    importType: String,
    code: String
  ): Future[JsValue] = {
    val futures = Importer.run(content, importType).map { case (name, text) ⇒
      for {
        f ← transRepo.byNameAndCode(project, name, code).flatMap { maybeTrans ⇒
          if(maybeTrans.isDefined) Future(maybeTrans.get.copy(status = Status.Skipped).toJson)
          else {
            val trans = Trans(code, name, text, user, project).copy(status = Status.Active)
            transRepo.insert(trans).map(_ ⇒ trans.toJson)
          }
        }
      } yield f
    }

    val futureList = Future.fold(futures)(List.empty[JsValue])((list, obj) ⇒ obj +: list)
    futureList.map(list ⇒ Json.toJson(list))
  }
}
