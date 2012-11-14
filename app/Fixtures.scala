package translator

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import org.scala_tools.time.Imports._
import translator.models._

trait Fixtures {

  private val _user1 = User("d.dietrich84@gmail.com", "demo" sha512, Nil, None)
  private val _user2 = User("frank.drebin.1984@gmail.com", "demo" sha512, Nil, None)
  
  val project1 = Project("acme", _user1.id, "12345678")
  val project2 = Project("foo", _user2.id, "123456")
  
  val role1 = Role("ROLE_ADMIN", _user1.id, project1.id)
  val role2 = Role("ROLE_AUTHOR", _user1.id, project2.id)
  val role3 = Role("ROLE_AUTHOR", _user2.id, project1.id)
  val role4 = Role("ROLE_ADMIN", _user2.id, project2.id)

  def user1 = _user1.copy(roles = List(role1, role2))
  def user2 = _user2.copy(roles = List(role3, role4))

  lazy val language1 = Language("en", "English", project1.id)
  lazy val language2 = Language("de", "German", project1.id)
  lazy val language3 = Language("fr", "French", project1.id)
  lazy val language4 = Language("es", "Spanish", project1.id)
  lazy val language5 = Language("it", "Italien", project1.id)
  lazy val language6 = Language("pt", "Portuguese", project1.id)

  lazy val language7 = Language("en", "English", project2.id)
  lazy val language8 = Language("de", "German", project2.id)
  lazy val language9 = Language("fr", "French", project2.id)

  lazy val entry1 = Entry("hello_world", "", project1.id, List(
    Translation("en", "Hello World", user1.id, true),
    Translation("de", "Hallo Welt", user1.id, true),
    Translation("fr", "Bonjour", user1.id, true),
    Translation("es", "Olá", user1.id, true),
    Translation("it", "Ciao", user1.id, true)
  ))

  lazy val entry2 = Entry("bye_bye", "", project1.id, List(
    Translation("en", "Bye bye", user1.id, true),
    Translation("de", "Tschüs", user1.id, true),
    Translation("fr", "Ourevoir", user1.id, true),
    Translation("es", "", user1.id, true),
    Translation("it", "Ciao", user1.id, true)
  ))

  lazy val entry3 = Entry("title", "", project2.id, List(
    Translation("en", "Title", user1.id, true),
    Translation("de", "Titel", user1.id, true),
    Translation("fr", "", user1.id, true)
  ))

  lazy val entry4 = Entry("description", "", project2.id, List(
    Translation("en", "Description", user1.id, true),
    Translation("de", "Beschreibung", user1.id, true),
    Translation("fr", "", user1.id, true)
  ))
}
