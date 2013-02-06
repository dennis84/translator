package translator
package trans

import scala.concurrent._
import play.api.libs.json._
import scala.concurrent.duration._
import translator.core._
import translator.project._
import translator.lang._
import translator.trans.list._

class TransApi(
  transRepo: TransRepo,
  langRepo: LangRepo) extends Api {

  def entry(p: Project, id: String): Future[JsValue] = api {
    for {
      mt ← transRepo.byId(id)
      t ← get(mt, "entry_not_found")
      l ← langRepo.listByProject(p)
      c ← transRepo.listByName(p, t.name)
      e = Entry(t, p, l, c).toJson
    } yield e
  }

  def entries(p: Project, f: Filter): Future[JsValue] = api {
    for {
      langs ← langRepo.listByProject(p)
      translations ← transRepo.listByProject(p)
      entries = translations.mkEntries(langs)
    } yield {
      Json.toJson(f.filter(entries).map(_.toJson))
    }
  }

  def export(p: Project, code: String): Future[JsValue] = api {
    for {
      t ← transRepo.listActive(p, code).map { list ⇒
        Json.toJson(list.map(t ⇒ Json.obj(t.name -> t.text)))
      }
    } yield t
  }

  def list(p: Project, name: String): Future[JsValue] = api {
    for {
      l ← langRepo.listByProject(p)
      t ← transRepo.listByName(p, name).map { list ⇒
        Json.toJson(list.mkComplete(l).map(_.toJson))
      }
    } yield t
  }

  def search(p: Project, term: String): Future[JsValue] = api {
    for {
      langs ← langRepo.listByProject(p)
      translations ← transRepo.listLike(p, term)
      all ← transRepo.listByNames(p, translations.map(_.name))
      entries = all.mkEntries(langs)
    } yield {
      Json.toJson(entries.map(_.toJson))
    }
  }

  // def create(
  //   code: String,
  //   name: String,
  //   text: String
  // )(implicit ctx: ProjectContext[_]): Option[Translation] =
  //   for {
  //     c ← langDAO.validateCode(ctx.project, code)
  //     e = transDAO.byNameAndCode(ctx.project, name, code)
  //     if (!e.isDefined || (e.isDefined && e.get.text != text))
  //     d = Translation(c, name, text, ctx.user, ctx.project)
  //     s ← this.findStatus(d, ctx.user)
  //     t = d.copy(status = s)
  //     _ ← transDAO.insert(t)
  //   } yield t

  // def update(id: String, text: String): Option[Translation] =
  //   for {
  //     t ← transDAO.byId(id)
  //     u = t.copy(text = text)
  //     wc = transDAO.save(u)
  //   } yield u

  // def switch(user: User, project: Project, id: String): Option[Translation] =
  //   for {
  //     a ← transDAO.byId(id)
  //     o = transDAO.activated(project, a.name, a.code)
  //     u = a.copy(status = Status.Active)
  //     _ = transDAO.save(u)
  //   } yield {
  //     if (o.isDefined) transDAO.remove(o.get.encode)
  //     u
  //   }

  // def delete(project: Project, id: String): Option[Translation] =
  //   transDAO.byId(id) match {
  //     case Some(trans) if (trans.status == Status.Active) ⇒
  //       transDAO.removeEntry(project, trans.name)
  //       Some(trans)
  //     case Some(trans) if (trans.status == Status.Inactive) ⇒
  //       transDAO.remove(trans.encode)
  //       Some(trans)
  //     case None ⇒ None
  //   }

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

  // private def findStatus(t: Translation, u: User): Option[Status] =
  //   for {
  //     p ← t.project
  //   } yield transDAO.activated(p, t.name, t.code) match {
  //     case Some(a) ⇒ Status.Inactive
  //     case None ⇒ u.roles.contains(Role.ADMIN) match {
  //       case true ⇒ Status.Active
  //       case false ⇒ Status.Inactive
  //     }
  //   }

}
