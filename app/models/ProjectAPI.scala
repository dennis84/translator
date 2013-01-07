package translator.models

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import translator._
import translator.{ Context, ProjectContext }

object ProjectAPI {

  import Implicits._

  def by(token: String): Option[Project] =
    ProjectDAO.findOneByToken(token) map(makeProject(_))

  def listMine(user: User): List[Project] = user.rawRoles map { role =>
    for {
      r <- ProjectDAO.findOneById(role.projectId)
      p = makeProject(r)
      l = LanguageAPI.list(p)
      t = TranslationAPI.list(p)
    } yield p withStats(t, l)
  } flatten

  def create(name: String, u: User): Option[Project] = for {
    _ <- Some("")
    project = Project(name, uuid, u.id)
    user = u.copy(rawRoles = u.rawRoles :+ Role.Admin(project.id))
    wc = UserDAO.save(user)
    _ <- ProjectDAO.insert(project)
  } yield project

  def signup(projectName: String, username: String, password: String): Option[(User, Project)] = for {
    _ <- Some("")
    u = User(username, password sha512)
    _ <- UserDAO.insert(u)
    p <- create(projectName, u)
  } yield (u, p)

  private def uuid = java.util.UUID.randomUUID.toString

  def makeProject(p: DbProject) =
    Project(p.name, p.token, p.adminId, id = p.id)
}
