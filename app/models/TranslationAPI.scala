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
    val langs = LanguageAPI.list(project)
    var translations = TranslationDAO.findAllByProject(project)

    translations.find(_.id == id)
      .map(_.withProject(project).withStats(translations, langs))
  }

  def export(project: Project, code: String): List[(String, String)] = (for {
    c <- LanguageDAO.validateCode(project, code)
  } yield {
    TranslationDAO.findActivatedByProjectAndCode(project, c) map { trans =>
      trans.name -> trans.text
    }
  }) getOrElse Nil

  def entries(filter: Filter)(implicit ctx: ProjectContext[_]): List[Translation] = {
    LanguageDAO.primary(ctx.project) map { lang =>
      val langs = LanguageAPI.list(ctx.project)
      val translations = TranslationDAO.findAllByProject(ctx.project)

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
    TranslationDAO.findAllByProjectAndName(project, name).fixed

  def search(project: Project, term: String): List[Translation] = {
    val ids = Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
      new ObjectId(searchResponse.id)
    }

    TranslationDAO.findAllByProjectAndIds(project, ids)
      .map(_.withProject(project))
  }

  def create(
    code: String,
    name: String,
    text: String
  )(implicit ctx: ProjectContext[_]): Option[Translation] =
    for {
      c <- LanguageDAO.validateCode(ctx.project, code)
      t = Translation(c, name, text, ctx.user, ctx.project)
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
      o <- TranslationDAO.findOneByProjectNameAndCode(project, a.name, a.code)
      u = a.copy(status = Status.Active)
      wc1 = TranslationDAO.save(u)
      wc2 = TranslationDAO.remove(o.encode)
    } yield u

  def delete(project: Project, id: ObjectId): Option[Translation] =
    TranslationDAO.byId(id) match {
      case Some(trans) if (trans.status == Status.Active) =>
        TranslationDAO.removeAllByProjectAndName(project, trans.name)
        Some(trans)
      case Some(trans) if (trans.status == Status.Inactive) =>
        TranslationDAO.remove(trans.encode)
        Some(trans)
      case None => None
    }

  def imports(project: Project, user: User, content: String, t: String, code: String) =
    Parser.parse(content, t) map { row =>
      val (name, text) = row
      TranslationDAO.findOneByProjectNameAndCode(project, name, code) match {
        case Some(trans) => "" //TranslationImport(trans, Status.Skipped)
        case None => {
         // val trans = create(code, name, text, project, user)
          //TranslationImport(trans.model, Status.Imported)
          ""
        }
      }
    }
}
