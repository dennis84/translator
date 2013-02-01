package test.translator

import com.roundeights.hasher.Implicits._
import reactivemongo.bson._
import translator.core._

trait Fixtures {

  private val _user1 = User(Doc.randomID, "d.dietrich84@gmail.com", "demo")
  private val _user2 = User(Doc.randomID, "frank.drebin.1984@gmail.com", "demo")
  val user3 = User(Doc.randomID, "text@example.com", "demo")

  private val _project1 = Project(Doc.randomID, "acme", "12345678", _user1.id, open = false)
  private val _project2 = Project(Doc.randomID, "foo", "123456", _user2.id, open = true)
  
  val role1 = Role("ROLE_ADMIN", _project1.id)
  val role2 = Role("ROLE_AUTHOR", _project2.id)
  val role3 = Role("ROLE_AUTHOR", _project1.id)
  val role4 = Role("ROLE_ADMIN", _project2.id)

  val user1 = _user1.copy(dbRoles = List(role1, role2))
  val user2 = _user2.copy(dbRoles = List(role3, role4))

  val project1 = _project1.withUser(user1)
  val project2 = _project2.withUser(user2)

  def makeLang(c: String, n: String, p: Project) =
    Lang(Doc.randomID, c, n, p.id, Some(p))

  lazy val lang1 = makeLang("en", "English", project1)
  lazy val lang2 = makeLang("de", "German", project1)
  lazy val lang3 = makeLang("fr", "French", project1)
  lazy val lang4 = makeLang("es", "Spanish", project1)
  lazy val lang5 = makeLang("it", "Italien", project1)
  lazy val lang6 = makeLang("pt", "Portuguese", project1)

  lazy val lang7 = makeLang("en", "English", project2)
  lazy val lang8 = makeLang("de", "German", project2)
  lazy val lang9 = makeLang("fr", "French", project2)

  def makeTrans(c: String, n: String, t: String, u: User, s: Status, p: Project) =
    Trans(Doc.randomID, c, n, t, u.username, s, p.id, Some(p))

  lazy val trans1en  = makeTrans("en", "hello_world", "Hello World", user1, Status.Active, project1)
  lazy val trans1de  = makeTrans("de", "hello_world", "Hallo Welt", user1, Status.Active, project1)
  lazy val trans1de1 = makeTrans("de", "hello_world", "Moin Welt", user2, Status.Inactive, project1)
  lazy val trans1fr  = makeTrans("fr", "hello_world", "Bonjour", user1, Status.Active, project1)
  lazy val trans1es  = makeTrans("es", "hello_world", "Olá", user1, Status.Active, project1)
  lazy val trans1it  = makeTrans("it", "hello_world", "Ciao", user1, Status.Active, project1)
  lazy val trans1pt1 = makeTrans("pt", "hello_world", "Ciao", user2, Status.Inactive, project1)

  lazy val trans2en = makeTrans("en", "bye_bye", "Bye bye", user1, Status.Active, project1)
  lazy val trans2de = makeTrans("de", "bye_bye", "Tschüs", user1, Status.Active, project1)
  lazy val trans2fr = makeTrans("fr", "bye_bye", "Ourevoir", user1, Status.Active, project1)
  lazy val trans2es = makeTrans("es", "bye_bye", "", user1, Status.Active, project1)

  lazy val trans3en = makeTrans("en", "title", "Title", user1, Status.Active, project2)
  lazy val trans3de = makeTrans("de", "title", "Titel", user1, Status.Active, project2)
  lazy val trans3fr = makeTrans("fr", "title", "", user1, Status.Active, project2)

  lazy val trans4en = makeTrans("en", "description", "Description", user1, Status.Active, project2)
  lazy val trans4de = makeTrans("de", "description", "Beschreibung", user1, Status.Active, project2)
  lazy val trans4fr = makeTrans("fr", "description", "", user1, Status.Active, project2)
}
