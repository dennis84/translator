package translator
package core

import scala.concurrent._
import play.api.libs.json._

class TransApi(
  transRepo: TransRepo,
  langRepo: LangRepo) {

  // def entries: Future[JsValue] =
  //   transRepo.list map { list ⇒
  //     Json.toJson(list.map(_.toJson))
  //   }

  // def listByName(name: String): Future[JsValue] =
  //   transRepo.listByName(name) map { list ⇒
  //     Json.toJson(list.map(_.toJson))
  //   }

  // -----------

  // transDAO: TranslationDAO,
  // langDAO: LanguageDAO) {

  // import Implicits._

  // def entry(p: Project, id: String): Option[Entry] = for {
  //   t ← transDAO.byId(id)
  //   l = langDAO.list(p)
  //   c = transDAO.listByName(p, t.name)
  // } yield Entry(t, p, l, c)

  // def export(project: Project, code: String): List[(String, String)] = (for {
  //   c ← langDAO.validateCode(project, code)
  // } yield {
  //   transDAO.listActive(project, c) map(t ⇒ t.name -> t.text)
  // }) getOrElse Nil

  // def entries(filter: Filter)(implicit ctx: ProjectContext[_]): List[Entry] = {
  //   langDAO.primary(ctx.project) map { lang ⇒
  //     val langs = langDAO.list(ctx.project)
  //     val trans = transDAO.listActive(ctx.project, lang.code)
  //     val entries = trans.map(t ⇒
  //       Entry(t, ctx.project, langs, transDAO.listByName(ctx.project, t.name)))

  //     filter.filter(entries)
  //   } getOrElse Nil
  // }

  // def list(p: Project, name: String): List[Translation] =
  //   transDAO.listByName(p, name) fixed(langDAO.list(p))

  // def listByLang(p: Project, lang: String): List[Translation] = {
  //   val all = transDAO.all(p)
  //   val names = all.map(_.name).distinct

  //   names.map { name ⇒
  //     all.find(trans ⇒
  //       trans.code == lang &&
  //       trans.name == name &&
  //       trans.status == Status.Active
  //     ) getOrElse {
  //       Translation.empty(lang, name, p)
  //     }
  //   }
  // }

  // def search(p: Project, term: String): List[Entry] =
  //   langDAO.primary(p) map { l ⇒
  //     val langs = langDAO.list(p)
  //     val makeEntry = (t: Translation) ⇒
  //       Entry(t, p, langs, transDAO.listByName(p, t.name))

  //     transDAO.listLike(p, term).foldLeft(List.empty[Entry]) {
  //       (entries, t) ⇒
  //         t match {
  //           case t if(t.code == l.code) ⇒ entries :+ makeEntry(t)
  //           case t if(entries.exists(_.name == t.name)) ⇒ entries
  //           case t ⇒ transDAO.byNameAndCode(p, t.name, l.code) match {
  //             case Some(e) ⇒ entries :+ makeEntry(e)
  //             case None ⇒ entries
  //           }
  //         }
  //     }
  //   } getOrElse Nil

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
