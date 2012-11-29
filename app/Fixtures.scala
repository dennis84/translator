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
  
  val role1 = Role("ROLE_ADMIN", project1.id)
  val role2 = Role("ROLE_AUTHOR", project2.id)
  val role3 = Role("ROLE_AUTHOR", project1.id)
  val role4 = Role("ROLE_ADMIN", project2.id)

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

  lazy val trans1en  = Translation("en", "Hello World", user1.id, true)
  lazy val trans1de  = Translation("de", "Hallo Welt", user1.id, true)
  lazy val trans1de1 = Translation("de", "Moin Welt", user2.id, false)
  lazy val trans1fr  = Translation("fr", "Bonjour", user1.id, true)
  lazy val trans1es  = Translation("es", "Olá", user1.id, true)
  lazy val trans1it  = Translation("it", "Ciao", user1.id, true)
  lazy val trans1pt1 = Translation("pt", "Ciao", user2.id, false)

  lazy val trans2en = Translation("en", "Bye bye", user1.id, true)
  lazy val trans2de = Translation("de", "Tschüs", user1.id, true)
  lazy val trans2fr = Translation("fr", "Ourevoir", user1.id, true)
  lazy val trans2es = Translation("es", "", user1.id, true)
  lazy val trans2it = Translation("it", "Ciao", user1.id, true)

  lazy val trans3en = Translation("en", "Title", user1.id, true)
  lazy val trans3de = Translation("de", "Titel", user1.id, true)
  lazy val trans3fr = Translation("fr", "", user1.id, true)

  lazy val trans4en = Translation("en", "Description", user1.id, true)
  lazy val trans4de = Translation("de", "Beschreibung", user1.id, true)
  lazy val trans4fr = Translation("fr", "", user1.id, true)

  lazy val entry1 = Entry("hello_world", "", project1.id, List(trans1en.id, trans1de.id, trans1fr.id, trans1es.id, trans1it.id, trans1de1.id, trans1pt1.id))
  lazy val entry2 = Entry("bye_bye", "", project1.id, List(trans2en.id, trans2de.id, trans2fr.id, trans2es.id, trans2it.id))
  lazy val entry3 = Entry("title", "", project2.id, List(trans3en.id, trans3de.id, trans3fr.id))
  lazy val entry4 = Entry("description", "", project2.id, List(trans4en.id, trans4de.id, trans4fr.id))
}
