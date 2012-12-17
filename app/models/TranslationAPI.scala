package translator.models

import org.elasticsearch.index.query._, FilterBuilders._, QueryBuilders._
import org.elasticsearch.search._, facet._, terms._, sort._, SortBuilders._, builder._
import com.mongodb.casbah.Imports._
import translator._

object TranslationAPI {

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
   *  overview.
   */
  def entries(project: Project, filter: Filter) = {
    LanguageAPI.first(project) map { lang =>
      var translations = fixTranslations(TranslationDAO.findAllByProject(project), project)

      if ("true" == filter.untranslated) {
        val untranslatedNames = translations.filter { trans =>
          trans.text == "" && filter.untranslatedLanguages.exists(_ == trans.code) && trans.status == Status.Active
        } map (_.name)

        translations = untranslatedNames.map { name =>
          translations.find(t => name == t.name && lang.code == t.code)
        } flatten
      }

      if ("true" == filter.activatable) {
        translations = translations filter { trans =>
          TranslationAPI.activatable(project, trans.name).length > 0
        }
      }

      translations filter (_.code == lang.code) map (generateMap(_, project))
    } getOrElse Nil
  }

  /** Returns all translations by name.
   */
  def list(project: Project, name: String) = fixTranslations(
    TranslationDAO.findAllByProjectAndName(project, name), project) map (generateMap(_, project))

  /** Searches for translations by any term and returns a list of mapped
   *  translations.
   */
  def search(project: Project, term: String) = {
    val ids = Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
      new ObjectId(searchResponse.id)
    }

    TranslationDAO.findAllByProjectAndIds(project, ids) map (generateMap(_, project))
  }

  /** Creates a new translation.
   */
  def create(c: String, name: String, text: String, project: Project, user: User) =
    Translation(
      LanguageAPI.code(project, c),
      name,
      text,
      project.id,
      user.id,
      status(project, user))

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
    val updated = actual.copy(status = translator.models.Status.Active)
    TranslationDAO.save(updated)
    TranslationDAO.remove(old)
    updated
  }

  /** Deletes one ore all translations with the same name if the user is an
   *  author or an admin.
   */
  def delete(project: Project, id: ObjectId) = TranslationDAO.findOneById(id) match {
    case Some(trans) if (trans.status == Status.Active) =>
      TranslationDAO.removeAllByProjectAndName(project, trans.name)
    case Some(trans) if (trans.status == Status.Inactive) =>
      TranslationDAO.remove(trans)
    case None => None
  }

  /** Returns all translations by project.
   *  @TODO Make this method private
   */
  def all(project: Project) = fixTranslations(
    TranslationDAO.findAllByProject(project), project)

  private def status(project: Project, user: User) =
    user.roles(project) contains (Role.ADMIN) match {
      case true => Status.Active
      case false => Status.Inactive
    }

  private def activatable(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project, name) filter (_.status == Status.Inactive)

  private def progress(project: Project, name: String) = {
    val languages = LanguageDAO.findAllByProject(project)
    val translations = TranslationDAO.findAllByProjectAndName(project, name)

    languages.filter { lang =>
      translations exists { trans => trans.code == lang.code && trans.text != "" && trans.status == Status.Active }
    }.length.toFloat / languages.length * 100
  }

  private def fixTranslations(trans: List[Translation], project: Project) = {
    val langs = LanguageDAO.findAllByProject(project).map(_.code)
    val diff  = langs.diff(trans.filter(_.status == Status.Active).map(_.code))

    trans match {
      case Nil => List.empty[Translation]
      case _   => {
        val unsorted = (trans ++ diff.map { code =>
          EmptyTranslation(code, trans.head)
        }) sortBy (_.status.id)

        langs map { l =>
          unsorted.filter(_.code == l)
        } flatten
      }
    }
  }

  private def generateMap(trans: Translation, project: Project) = Map(
    "id" -> trans.id.toString,
    "code" -> trans.code,
    "name" -> trans.name,
    "text" -> trans.text,
    "author" -> UserDAO.findOneById(trans.authorId).map(_.username).getOrElse("unknown"),
    "status" -> trans.status.toString,
    "nb_activatable" -> activatable(project, trans.name).length,
    "progress" -> progress(project, trans.name))
}
