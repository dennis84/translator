package translator.models

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import translator._
import translator.{ Context, ProjectContext }

object ProjectAPI {

  import Implicits._

  def by(token: String): Option[Project] =
    ProjectDAO.findOneByToken(token)

  def listMine(user: User): List[Project] =
    ProjectDAO.findAllByIds(user.rawRoles.map(_.projectId)) map { p =>
      p.withStats(
        TranslationDAO.findAllByProject(p),
        LanguageDAO.findAllByProject(p))
    }

  def create(name: String, u: User): Option[Project] = for {
    _ <- Some("")
    project = Project(name, uuid, u.id)
    user = u.copy(rawRoles = u.rawRoles :+ Role.Admin(project.id))
    wc = UserDAO.save(user)
    _ <- ProjectDAO.insert(project)
  } yield project

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
