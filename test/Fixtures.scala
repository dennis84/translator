package translator.test

import org.specs2.specification.Scope
import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import org.scala_tools.time.Imports._
import com.codahale.jerkson.Json
import translator._
import translator.core._
import translator.core.Implicits._

object DataFixtures extends Fixtures {

  def refresh = {
    UserDAO.collection.drop
    ProjectDAO.collection.drop
    TranslationDAO.collection.drop
    LanguageDAO.collection.drop

    UserDAO.insert(user1, user2)
    ProjectDAO.insert(project1, project2)
    TranslationDAO.insert(
      trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
      trans2en, trans2de, trans2fr, trans2es,
      trans3en, trans3de, trans3fr,
      trans4en, trans4de, trans4fr)
    LanguageDAO.insert(language1, language2, language3, language4, language5,
      language6, language7, language8, language9)
  }
}

object SearchFixtures {

  def refresh = {
    Search.reset

    TranslationDAO.all map { trans =>
      Search.indexer.index("translator", "translation", trans.id, Json generate Map(
        "name" -> trans.name,
        "text" -> trans.text))
    }

    Search.indexer.refresh()
  }
}

trait Fixtures {

  private val _user1 = User("d.dietrich84@gmail.com", "demo" sha512)
  private val _user2 = User("frank.drebin.1984@gmail.com", "demo" sha512)
  
  private val _project1 = Project("acme", "12345678", _user1.id, open = false)
  private val _project2 = Project("foo", "123456", _user2.id, open = true)
  
  val role1 = DbRole("ROLE_ADMIN", _project1.id)
  val role2 = DbRole("ROLE_AUTHOR", _project2.id)
  val role3 = DbRole("ROLE_AUTHOR", _project1.id)
  val role4 = DbRole("ROLE_ADMIN", _project2.id)

  val user1 = _user1.copy(rawRoles = List(role1, role2))
  val user2 = _user2.copy(rawRoles = List(role3, role4))

  val project1 = _project1.withUser(user1)
  val project2 = _project2.withUser(user2)

  def makeLanguage(c: String, t: String, p: Project) =
    Language(c, t, p.id, Some(p))

  lazy val language1 = makeLanguage("en", "English", project1)
  lazy val language2 = makeLanguage("de", "German", project1)
  lazy val language3 = makeLanguage("fr", "French", project1)
  lazy val language4 = makeLanguage("es", "Spanish", project1)
  lazy val language5 = makeLanguage("it", "Italien", project1)
  lazy val language6 = makeLanguage("pt", "Portuguese", project1)

  lazy val language7 = makeLanguage("en", "English", project2)
  lazy val language8 = makeLanguage("de", "German", project2)
  lazy val language9 = makeLanguage("fr", "French", project2)

  def makeTranslation(c: String, n: String, t: String, u: User, s: Status, p: Project) =
    Translation(c, n, t, u.username, s, p.id, Some(p))

  lazy val trans1en  = makeTranslation("en", "hello_world", "Hello World", user1, Status.Active, project1)
  lazy val trans1de  = makeTranslation("de", "hello_world", "Hallo Welt", user1, Status.Active, project1)
  lazy val trans1de1 = makeTranslation("de", "hello_world", "Moin Welt", user2, Status.Inactive, project1)
  lazy val trans1fr  = makeTranslation("fr", "hello_world", "Bonjour", user1, Status.Active, project1)
  lazy val trans1es  = makeTranslation("es", "hello_world", "Olá", user1, Status.Active, project1)
  lazy val trans1it  = makeTranslation("it", "hello_world", "Ciao", user1, Status.Active, project1)
  lazy val trans1pt1 = makeTranslation("pt", "hello_world", "Ciao", user2, Status.Inactive, project1)

  lazy val trans2en = makeTranslation("en", "bye_bye", "Bye bye", user1, Status.Active, project1)
  lazy val trans2de = makeTranslation("de", "bye_bye", "Tschüs", user1, Status.Active, project1)
  lazy val trans2fr = makeTranslation("fr", "bye_bye", "Ourevoir", user1, Status.Active, project1)
  lazy val trans2es = makeTranslation("es", "bye_bye", "", user1, Status.Active, project1)

  lazy val trans3en = makeTranslation("en", "title", "Title", user1, Status.Active, project2)
  lazy val trans3de = makeTranslation("de", "title", "Titel", user1, Status.Active, project2)
  lazy val trans3fr = makeTranslation("fr", "title", "", user1, Status.Active, project2)

  lazy val trans4en = makeTranslation("en", "description", "Description", user1, Status.Active, project2)
  lazy val trans4de = makeTranslation("de", "description", "Beschreibung", user1, Status.Active, project2)
  lazy val trans4fr = makeTranslation("fr", "description", "", user1, Status.Active, project2)
}
