package translator.models

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import translator._
import translator.{ Context, ProjectContext }

object ProjectAPI {

  import Implicits._

  def byToken(token: String): Option[Project] =
    ProjectDAO.byToken(token)

  def listMine(user: User): List[Project] =
    ProjectDAO.listByIds(user.rawRoles.map(_.projectId)) map { p =>
      p.withStats(
        TranslationDAO.list(p),
        LanguageDAO.list(p))
    }

  def create(name: String, user: User): Option[Project] = for {
    _ <- Some("")
    p = Project(name, uuid, user.id)
    u = user.copy(rawRoles = user.rawRoles :+ Role.Admin(p.id))
    wc = UserDAO.save(u)
    _ <- ProjectDAO.insert(p)
  } yield p

  def signup(
    projectName: String,
    username: String,
    password: String
  ): Option[(User, Project)] = for {
    _ <- Some("")
    u = User(username, password sha512)
    _ <- UserDAO.insert(u)
    p <- create(projectName, u)
  } yield (u, p)

  private def uuid = java.util.UUID.randomUUID.toString
}
