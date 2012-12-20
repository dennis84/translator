package translator

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import org.scala_tools.time.Imports._
import translator.models._

trait Fixtures {

  private val _user1 = DbUser("d.dietrich84@gmail.com", "demo" sha512)
  private val _user2 = DbUser("frank.drebin.1984@gmail.com", "demo" sha512)
  
  val project1 = DbProject("acme", _user1.id, "12345678")
  val project2 = DbProject("foo", _user2.id, "123456")
  
  val role1 = DbRole("ROLE_ADMIN", project1.id)
  val role2 = DbRole("ROLE_AUTHOR", project2.id)
  val role3 = DbRole("ROLE_AUTHOR", project1.id)
  val role4 = DbRole("ROLE_ADMIN", project2.id)

  def user1 = _user1.copy(roles = List(role1, role2))
  def user2 = _user2.copy(roles = List(role3, role4))

  lazy val language1 = DbLanguage("en", "English", project1.id)
  lazy val language2 = DbLanguage("de", "German", project1.id)
  lazy val language3 = DbLanguage("fr", "French", project1.id)
  lazy val language4 = DbLanguage("es", "Spanish", project1.id)
  lazy val language5 = DbLanguage("it", "Italien", project1.id)
  lazy val language6 = DbLanguage("pt", "Portuguese", project1.id)

  lazy val language7 = DbLanguage("en", "English", project2.id)
  lazy val language8 = DbLanguage("de", "German", project2.id)
  lazy val language9 = DbLanguage("fr", "French", project2.id)

  lazy val trans1en  = DbTranslation("en", "hello_world", "Hello World", project1.id, user1.id, Status.Active)
  lazy val trans1de  = DbTranslation("de", "hello_world", "Hallo Welt", project1.id, user1.id, Status.Active)
  lazy val trans1de1 = DbTranslation("de", "hello_world", "Moin Welt", project1.id, user2.id, Status.Inactive)
  lazy val trans1fr  = DbTranslation("fr", "hello_world", "Bonjour", project1.id, user1.id, Status.Active)
  lazy val trans1es  = DbTranslation("es", "hello_world", "Olá", project1.id, user1.id, Status.Active)
  lazy val trans1it  = DbTranslation("it", "hello_world", "Ciao", project1.id, user1.id, Status.Active)
  lazy val trans1pt1 = DbTranslation("pt", "hello_world", "Ciao", project1.id, user2.id, Status.Inactive)

  lazy val trans2en = DbTranslation("en", "bye_bye", "Bye bye", project1.id, user1.id, Status.Active)
  lazy val trans2de = DbTranslation("de", "bye_bye", "Tschüs", project1.id, user1.id, Status.Active)
  lazy val trans2fr = DbTranslation("fr", "bye_bye", "Ourevoir", project1.id, user1.id, Status.Active)
  lazy val trans2es = DbTranslation("es", "bye_bye", "", project1.id, user1.id, Status.Active)

  lazy val trans3en = DbTranslation("en", "title", "Title", project2.id, user1.id, Status.Active)
  lazy val trans3de = DbTranslation("de", "title", "Titel", project2.id, user1.id, Status.Active)
  lazy val trans3fr = DbTranslation("fr", "title", "", project2.id, user1.id, Status.Active)

  lazy val trans4en = DbTranslation("en", "description", "Description", project2.id, user1.id, Status.Active)
  lazy val trans4de = DbTranslation("de", "description", "Beschreibung", project2.id, user1.id, Status.Active)
  lazy val trans4fr = DbTranslation("fr", "description", "", project2.id, user1.id, Status.Active)
}
