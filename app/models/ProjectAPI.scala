package translator.models

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import translator._
import translator.controllers.{ Context, ProjectContext }

object ProjectAPI {

  import Implicits._

  def by(token: String) =
    ProjectDAO.findOneByToken(token) map(makeProject(_))

  def listMine(user: User) = user.rawRoles map { role =>
    ProjectDAO.findOneById(role.projectId) map { p =>
      val project = makeProject(p)
      val langs   = LanguageAPI.list(project)
      val trans   = TranslationAPI.list(project)

      project.withStats(trans, langs)
    }
  } flatten

  def create(name: String, u: User) = {
    val project = Project(name, uuid, u.id)
    val user = u.copy(rawRoles = u.rawRoles :+ Role.Admin(project.id))

    UserDAO.save(user)
    ProjectDAO.insert(project)
    project
  }

  def signup(projectName: String, username: String, password: String) = {
    val user = User(username, password sha512)
    UserDAO.insert(user)
    create(projectName, user)
  }

  private def uuid = java.util.UUID.randomUUID.toString

  private def makeProject(p: DbProject) =
    Project(p.name, p.token, p.adminId, id = p.id)
}
