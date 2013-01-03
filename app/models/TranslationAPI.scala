package translator.models

import org.elasticsearch.index.query._, FilterBuilders._, QueryBuilders._
import org.elasticsearch.search._, facet._, terms._, sort._, SortBuilders._, builder._
import com.mongodb.casbah.Imports._
import translator._
import translator.controllers.{ Context, ProjectContext }
import translator.utils.Parser

object TranslationAPI {

  import Implicits._

  def by(id: ObjectId) =
    TranslationDAO.findOneById(id) map(makeTranslation(_))

  /** After updated a translation it's possible to fetch it again to get the
   *  actual statistics.
   */
  def entry(id: ObjectId, project: Project) = {
    val langs = LanguageAPI.list(project)
    var translations = TranslationDAO.findAllByProject(project) map(makeTranslation(_))

    translations.find(_.id == id).map(_.withProject(project).withStats(translations, langs))
  }

  /** Returns all translations by language as key value format.
   */
  def export(project: Project, c: String) =
    TranslationDAO.findActivatedByProjectAndCode(
      project,
      LanguageAPI.code(project, c)
    ) map { trans =>
      trans.name -> trans.text
    }

  /** Retuns the filtered entries, this are the main translations for the
   *  overview with statistics.
   */
  def entries(filter: Filter)(implicit ctx: ProjectContext[_]) = {
    LanguageAPI.first(ctx.project) map { lang =>
      val langs = LanguageAPI.list(ctx.project)
      val translations = TranslationDAO.findAllByProject(ctx.project) map(makeTranslation(_))

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

  /** Lists all translations by project. The translations will not have any
   *  statistics if this is desired use the "entries" method.
   */
  def list(project: Project) =
    TranslationDAO.findAllByProject(project) map(makeTranslation(_))

  /** Returns all translations by name. This method is used to get all
   *  translations by an entry the result must is fixed and don't have
   *  statistics.
   */
  def list(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name)
      .map(makeTranslation(_))
      .fixed

  def activatable(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name).filter { trans =>
      trans.status == Status.Inactive
    } map(makeTranslation(_))

  /** Searches for translations by any term and returns a list of mapped
   *  translations.
   */
  def search(project: Project, term: String) = {
    val ids = Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
      new ObjectId(searchResponse.id)
    }

    TranslationDAO.findAllByProjectAndIds(project, ids) map { trans =>
      makeTranslation(trans).withProject(project)
    }
  }

  /** Creates a new translation.
   */
  def create(c: String, name: String, text: String)(implicit ctx: ProjectContext[_]) = for {
    _ <- Some("")
    trans = DbTranslation(
      LanguageAPI.code(ctx.project, c),
      name,
      text,
      ctx.project.id,
      ctx.user.username,
      status(ctx.user))
    _ ← TranslationDAO.insert(trans)
  } yield makeTranslation(trans)

  /** Updates the text of a translation.
   */
  def update(before: Translation, text: String) = {
    val updated = before.copy(text = text)
    TranslationDAO.save(updated)
    updated
  }

  /** Sets the translation by id to active and removes the previous translation.
   */
  def switch(user: User, project: Project, id: ObjectId) = for {
    actual <- TranslationDAO.findOneById(id)
    old    <- TranslationDAO.findOneByProjectNameAndCode(project, actual.name, actual.code)
  } yield {
    val updated = actual.copy(status = Status.Active)
    TranslationDAO.save(updated)
    TranslationDAO.remove(old)
    makeTranslation(updated)
  }

  /** Deletes one ore all translations with the same name if the user is an
   *  author or an admin.
   */
  def delete(project: Project, id: ObjectId) = TranslationDAO.findOneById(id) match {
    case Some(trans) if (trans.status == Status.Active) =>
      TranslationDAO.removeAllByProjectAndName(project, trans.name)
      Some(makeTranslation(trans))
    case Some(trans) if (trans.status == Status.Inactive) =>
      TranslationDAO.remove(trans)
      Some(makeTranslation(trans))
    case None => None
  }

  /** Parses the content by type and inserts the non existing translations. For
   *  all entries a TranslationImport class will be created to send the client
   *  a usefull import response.
   */
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

  /** Returns all untranslated translations by project.
   */
  def untranslated(project: Project) =
    TranslationDAO.findAllByProject(project)

  /** Returns all untranslated translations by project and name.
   */
  def untranslated(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name)

  private def status(user: User) =
    user.roles contains (Role.ADMIN) match {
      case true => Status.Active
      case false => Status.Inactive
    }

  private def makeTranslation(t: DbTranslation) =
    Translation(t.code, t.name, t.text, t.author, t.status, t.projectId, id = t.id)
}
