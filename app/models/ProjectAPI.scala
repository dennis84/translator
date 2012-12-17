package translator.models

import com.mongodb.casbah.Imports._
import com.roundeights.hasher.Implicits._
import translator._
import translator.controllers.{ Context, ProjectContext }

object ProjectAPI {

  /** Lists all projects as map.
   */
  def list(projects: List[Project]) = projects map (generateMap(_))

  /** Finds all projects where user is registered in.
   */
  def listMine(user: User) = user.roles map { role =>
    ProjectDAO.findOneById(role.projectId)
  } flatten
  
  /** List all users by project.
   */
  def contributors(project: Project) = UserDAO.findAllByProject(project)

  /** Finds the admin of a project.
   */
  def admin(project: Project) = UserDAO.findOneById(project.adminId)

  /** Creates a new project.
   */
  def create(name: String, user: User) = {
    val project = Project(name, user.id, uuid)
    UserDAO.save(user.copy(
      roles = user.roles :+ Role.Admin(project.id)))
    ProjectDAO.insert(project)
    project
  }

  /** Signup a new user and a project.
   */
  def signup(projectName: String, username: String, password: String) = {
    val user = User(username, password sha512)
    UserDAO.insert(user)
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

  private def generateMap(project: Project) = Map(
    "id" -> project.id.toString,
    "name" -> project.name,
    "admin" -> admin(project).map(_.toMap).getOrElse(Map()),
    "statistics" -> Map(
      "progress" -> progress(project),
      "nb_entries" -> TranslationAPI.all(project).map(_.name).distinct.length,
      "nb_words" -> nbWords(project)
    ))

  private def uuid = java.util.UUID.randomUUID.toString
}
