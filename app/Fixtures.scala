package translator

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import org.scala_tools.time.Imports._
import translator.models._

trait Fixtures {

  val user1 = User("d.dietrich84@gmail.com", "demo" sha512, Nil, None)
  val user2 = User("frank.drebin.1984@gmail.com", "demo" sha512, Nil, None)
  
  val project1 = Project("acme", user1.id)
  
  val role1 = Role("ROLE_ADMIN", user1.id, project1.id)
  val role2 = Role("ROLE_AUTHOR", user2.id, project1.id)

  user1.copy(roles = List(role1))
  user2.copy(roles = List(role2))

  lazy val entry1 = Entry("hello_world", "", project1.id, List(
    Translation("en", "Hello World", user1.id),
    Translation("de", "Hallo Welt", user1.id),
    Translation("fr", "Bonjour", user1.id)
  ))

  lazy val entry2 = Entry("bye_bye", "", project1.id, List(
    Translation("en", "Bye bye", user1.id),
    Translation("de", "Tschüs", user1.id),
    Translation("fr", "Ourevoir", user1.id)
  ))
}
