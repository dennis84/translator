package translator.models

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import translator._
import translator.controllers.{ Context, ProjectContext }

object ProjectAPI {

  def by(token: String) =
    ProjectDAO.findOneByToken(token) map(makeProject(_))

  /** Lists all projects as map.
   */
  def list(projects: List[DbProject]) = projects map(makeProject(_))

  /** Finds all projects where user is registered in.
   */
  def listMine(user: User) = user.roles map { role =>
    ProjectDAO.findOneById(role.projectId) map(makeProject(_))
  } flatten

  /** Finds the admin of a project.
   */
  def admin(project: Project) = UserDAO.findOneById(project.adminId)

  /** Creates a new project.
   */
  def create(name: String, u: User) = {
    val project = Project(name, uuid)
    val user = u.copy(rawRoles = u.rawRoles :+ Role.Admin(project.id))

    UserDAO.save(user.encode)
    ProjectDAO.insert(project.encode)
    project
  }

  /** Signup a new user and a project.
   */
  def signup(projectName: String, username: String, password: String) = {
    val user = User(username, password sha512)
    UserDAO.insert(user.encode)
    create(projectName, user)
  }

  private def progress(project: Project) = LanguageDAO.findAllByProject(project) map { lang =>
    lang.code -> (TranslationAPI.all(project).filter { trans =>
      trans.code == lang.code && trans.text != "" && trans.status == Status.Active
    }.length.toFloat) / TranslationAPI.all(project).map(_.name).distinct.length * 100
  } toMap

  private def nbWords(project: Project) = LanguageDAO.findAllByProject(project).map { lang =>
    lang.code -> (TranslationAPI.all(project).filter { trans =>
      trans.code == lang.code && (trans.status == Status.Active || trans.status == Status.Empty)
    }.map(_.nbWords).reduceLeft(_ + _))
  } toMap

  private def uuid = java.util.UUID.randomUUID.toString

  private def makeProject(p: DbProject) =
    Project(p.id, p.name, p.token)
}
