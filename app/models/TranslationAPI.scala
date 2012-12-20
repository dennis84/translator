package translator.models

import org.elasticsearch.index.query._, FilterBuilders._, QueryBuilders._
import org.elasticsearch.search._, facet._, terms._, sort._, SortBuilders._, builder._
import com.mongodb.casbah.Imports._
import translator._
import translator.utils.Parser

object TranslationAPI {

  def by(id: ObjectId) =
    TranslationDAO.findOneById(id) map(makeTranslation(_))

  /** Returns all translations by language as key value format.
   */
  def export(project: Project, c: String) =
    TranslationDAO.findActivatedByProjectAndCode(
      project.encode,
      LanguageAPI.code(project, c)
    ) map { trans =>
      trans.name -> trans.text
    }

  /** Retuns the filtered entries, this are the main translations for the
   *  overview.
   */
  def entries(project: Project, filter: Filter) = {
    LanguageAPI.first(project) map { lang =>
      var translations = TranslationDAO.findAllByProject(project.encode)

      //if ("true" == filter.untranslated) {
        //val untranslatedNames = translations.filter { trans =>
          //trans.text == "" && filter.untranslatedLanguages.exists(_ == trans.code) && trans.status == Status.Active
        //} map (_.name)

        //translations = untranslatedNames.map { name =>
          //translations.find(t => name == t.name && lang.code == t.code)
        //} flatten
      //}

      //if ("true" == filter.activatable) {
        //translations = translations filter { trans =>
          //TranslationAPI.activatable(project, trans.name).length > 0
        //}
      //}

      translations.filter { trans =>
        trans.code == lang.code
      } map(makeTranslation(_) withProject(project))
    } getOrElse Nil
  }

  /** Returns all translations by name.
   */
  def list(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project.encode, name) map(makeTranslation(_))

  /** Searches for translations by any term and returns a list of mapped
   *  translations.
   */
  def search(project: Project, term: String) = {
    val ids = Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
      new ObjectId(searchResponse.id)
    }

    TranslationDAO.findAllByProjectAndIds(project.encode, ids) map(makeTranslation(_) withProject(project))
  }

  /** Creates a new translation.
   */
  def create(c: String, name: String, text: String, project: Project, user: User) = {
    val trans = DbTranslation(
      LanguageAPI.code(project, c),
      name,
      text,
      project.id,
      user.username,
      status(user))
    
    TranslationDAO.insert(trans)
    makeTranslation(trans)
  }

  /** Updates the text of a translation.
   */
  def update(before: Translation, text: String) = {
    val updated = before.copy(text = text)
    TranslationDAO.save(updated.encode)
    updated
  }

  /** Sets the translation by id to active and removes the previous translation.
   */
  def switch(user: User, project: Project, id: ObjectId) = for {
    actual <- TranslationDAO.findOneById(id)
    old    <- TranslationDAO.findOneByProjectNameAndCode(project.encode, actual.name, actual.code)
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
      TranslationDAO.removeAllByProjectAndName(project.encode, trans.name)
    case Some(trans) if (trans.status == Status.Inactive) =>
      TranslationDAO.remove(trans)
    case None => None
  }

  /** Parses the content by type and inserts the non existing translations. For
   *  all entries a TranslationImport class will be created to send the client
   *  a usefull import response.
   */
  def imports(project: Project, user: User, content: String, t: String, code: String) =
    Parser.parse(content, t) map { row =>
      val (name, text) = row
      TranslationDAO.findOneByProjectNameAndCode(project.encode, name, code) match {
        case Some(trans) => "" //TranslationImport(trans, Status.Skipped)
        case None => {
          val trans = create(code, name, text, project, user)
          //TranslationImport(trans.model, Status.Imported)
          ""
        }
      }
    }

  /** Returns all translations by project.
   *  @TODO Make this method private
   */
  def all(project: Project) =
    TranslationDAO.findAllByProject(project.encode)

  /** Returns all untranslated translations by project.
   */
  def untranslated(project: Project) =
    TranslationDAO.findAllByProject(project.encode)

  /** Returns all untranslated translations by project and name.
   */
  def untranslated(project: Project, name: String) =
    TranslationDAO.findAllByProjectAndName(project.encode, name)

  private def status(user: User) =
    user.roles contains (Role.ADMIN) match {
      case true => Status.Active
      case false => Status.Inactive
    }

  //private def filterUntranslated(translations: List[Translation], project: Project) =
    //fixTranslations(translations, project) filter { trans =>
      //trans.text == "" && (trans.status == Status.Active || trans.status == Status.Empty)
    //}

  //private def activatable(project: Project, name: String) =
    //TranslationDAO.findAllByProjectAndName(project, name) filter (_.status == Status.Inactive)

  //private def progress(project: Project, name: String) = {
    //val languages = LanguageDAO.findAllByProject(project)
    //val trans = untranslated(project, name)
      
    //100 - (trans.length.toFloat / languages.length * 100)
  //}

  //private def fixTranslations(trans: List[Translation], project: Project) = {
    //val langs = LanguageDAO.findAllByProject(project).map(_.code)
    //val diff  = langs.diff(trans.filter(_.status == Status.Active).map(_.code))

    //trans match {
      //case Nil => List.empty[Translation]
      //case _   => {
        //val unsorted = (trans ++ diff.map { code =>
          //Translation(code, trans.head.name, "", "", Status.Empty)
        //}) sortBy (_.status.id)

        //langs map { l =>
          //unsorted.filter(_.code == l)
        //} flatten
      //}
    //}
  //}

  private def makeTranslation(t: DbTranslation) =
    Translation(t.code, t.name, t.text, t.author, t.status, t.projectId, id = t.id)
}
