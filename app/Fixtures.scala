package translator

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import org.scala_tools.time.Imports._
import translator.models._

trait Fixtures {

  private val _user1 = User("d.dietrich84@gmail.com", "demo" sha512)
  private val _user2 = User("frank.drebin.1984@gmail.com", "demo" sha512)
  
  private val _project1 = Project("acme", "12345678", _user1.id)
  private val _project2 = Project("foo", "123456", _user2.id)
  
  val role1 = DbRole("ROLE_ADMIN", _project1.id)
  val role2 = DbRole("ROLE_AUTHOR", _project2.id)
  val role3 = DbRole("ROLE_AUTHOR", _project1.id)
  val role4 = DbRole("ROLE_ADMIN", _project2.id)

  val user1 = _user1.copy(rawRoles = List(role1, role2))
  val user2 = _user2.copy(rawRoles = List(role3, role4))

  val project1 = _project1.withUser(user1)
  val project2 = _project2.withUser(user2)

  lazy val language1 = Language("en", "English", project1)
  lazy val language2 = Language("de", "German", project1)
  lazy val language3 = Language("fr", "French", project1)
  lazy val language4 = Language("es", "Spanish", project1)
  lazy val language5 = Language("it", "Italien", project1)
  lazy val language6 = Language("pt", "Portuguese", project1)

  lazy val language7 = Language("en", "English", project2)
  lazy val language8 = Language("de", "German", project2)
  lazy val language9 = Language("fr", "French", project2)

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
