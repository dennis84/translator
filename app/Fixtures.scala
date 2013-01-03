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

  lazy val trans1en  = Translation("en", "hello_world", "Hello World", user1.username, Status.Active, project1.id, Some(project1))

  lazy val trans1de  = Translation("de", "hello_world", "Hallo Welt", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans1de1 = Translation("de", "hello_world", "Moin Welt", user2.username, Status.Inactive, project1.id, Some(project1))
  lazy val trans1fr  = Translation("fr", "hello_world", "Bonjour", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans1es  = Translation("es", "hello_world", "Olá", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans1it  = Translation("it", "hello_world", "Ciao", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans1pt1 = Translation("pt", "hello_world", "Ciao", user2.username, Status.Inactive, project1.id, Some(project1))

  lazy val trans2en = Translation("en", "bye_bye", "Bye bye", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans2de = Translation("de", "bye_bye", "Tschüs", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans2fr = Translation("fr", "bye_bye", "Ourevoir", user1.username, Status.Active, project1.id, Some(project1))
  lazy val trans2es = Translation("es", "bye_bye", "", user1.username, Status.Active, project1.id, Some(project1))

  lazy val trans3en = Translation("en", "title", "Title", user1.username, Status.Active, project2.id, Some(project2))
  lazy val trans3de = Translation("de", "title", "Titel", user1.username, Status.Active, project2.id, Some(project2))
  lazy val trans3fr = Translation("fr", "title", "", user1.username, Status.Active, project2.id, Some(project2))

  lazy val trans4en = Translation("en", "description", "Description", user1.username, Status.Active, project2.id, Some(project2))
  lazy val trans4de = Translation("de", "description", "Beschreibung", user1.username, Status.Active, project2.id, Some(project2))
  lazy val trans4fr = Translation("fr", "description", "", user1.username, Status.Active, project2.id, Some(project2))
}
