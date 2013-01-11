package translator.models

import org.elasticsearch.index.query._, FilterBuilders._, QueryBuilders._
import org.elasticsearch.search._, facet._, terms._, sort._, SortBuilders._, builder._
import com.mongodb.casbah.Imports._
import translator._
import translator.{ Context, ProjectContext }
import translator.utils.Parser

object TranslationAPI {

  import Implicits._

  def entry(id: ObjectId, project: Project): Option[Translation] = {
    val langs = LanguageDAO.list(project)
    var translations = TranslationDAO.list(project)

    translations.find(_.id == id)
      .map(_.withProject(project).withStats(translations, langs))
  }

  def export(project: Project, code: String): List[(String, String)] = (for {
    c <- LanguageDAO.validateCode(project, code)
  } yield {
    TranslationDAO.listActive(project, c) map { trans =>
      trans.name -> trans.text
    }
  }) getOrElse Nil

  def entries(filter: Filter)(implicit ctx: ProjectContext[_]): List[Translation] = {
    LanguageDAO.primary(ctx.project) map { lang =>
      val langs = LanguageDAO.list(ctx.project)
      val translations = TranslationDAO.list(ctx.project)

      var filtered = filter.filter(translations.fixed(langs))

      val entries = filtered.map { trans =>
        translations.find { t =>
          t.name == trans.name &&
          t.code == lang.code
        }
      }.flatten.distinct

      entries map { trans =>
        trans.withProject(ctx.project).withStats(translations, langs)
      }
    } getOrElse Nil
  }

  def list(project: Project, name: String): List[Translation] =
    TranslationDAO.listByName(project, name).fixed

  def search(p: Project, term: String): List[Translation] = {
    val ids = Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
      new ObjectId(searchResponse.id)
    }

    TranslationDAO.listByIds(p, ids) map { trans =>
      trans.withProject(p).withStats(
        TranslationDAO.list(p),
        LanguageDAO.list(p))
    }
  }

  def create(
    code: String,
    name: String,
    text: String
  )(implicit ctx: ProjectContext[_]): Option[Translation] =
    for {
      c <- LanguageDAO.validateCode(ctx.project, code)
      d = Translation(c, name, text, ctx.user, ctx.project)
      s <- this.findStatus(d, ctx.user)
      t = d.copy(status = s)
      _ ← TranslationDAO.insert(t)
    } yield t

  def update(id: ObjectId, text: String): Option[Translation] = for {
    t <- TranslationDAO.byId(id)
    u = t.copy(text = text)
    wc = TranslationDAO.save(u)
  } yield u

  def switch(user: User, project: Project, id: ObjectId): Option[Translation] =
    for {
      a <- TranslationDAO.byId(id)
      o = TranslationDAO.activated(project, a.name, a.code)
      u = a.copy(status = Status.Active)
      _ = TranslationDAO.save(u)
    } yield {
      if (o.isDefined) TranslationDAO.remove(o.get.encode)
      u
    }

  def delete(project: Project, id: ObjectId): Option[Translation] =
    TranslationDAO.byId(id) match {
      case Some(trans) if (trans.status == Status.Active) =>
        TranslationDAO.removeEntry(project, trans.name)
        Some(trans)
      case Some(trans) if (trans.status == Status.Inactive) =>
        TranslationDAO.remove(trans.encode)
        Some(trans)
      case None => None
    }

  def inject(p: Project, u: User, content: String, t: String, code: String): List[Translation] =
    Parser.parse(content, t).map { row =>
      val (name, text) = row
      TranslationDAO.byNameAndCode(p, name, code) match {
        case Some(trans) => Some(trans.copy(status = Status.Skipped))
        case None => for {
          c <- LanguageDAO.validateCode(p, code)
          t = Translation(c, name, text, u, p).copy(status = Status.Active)
          _ ← TranslationDAO.insert(t)
        } yield t
      }
    }.flatten

  private def findStatus(t: Translation, u: User): Option[Status] =
    for {
      p <- t.project
    } yield TranslationDAO.activated(p, t.name, t.code) match {
      case Some(a) => Status.Inactive
      case None => u.roles.contains(Role.ADMIN) match {
        case true => Status.Active
        case false => Status.Inactive
      }
    }
}
