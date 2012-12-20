package translator.models

import com.roundeights.hasher.Implicits._

object UserAPI {

  def by(project: DbProject) =
    UserDAO.findOneById(project.adminId) map(makeUser(_))

  def by(username: String) =
    UserDAO.findOneByUsername(username) map(makeUser(_))

  /** Creates the user view by user.
   */
  def by(user: DbUser) = makeUser(user)

  def by(user: DbUser, project: Project) = makeUser(user).withRoles(project)

  def contributors(p: Project) =
    UserDAO.findAllByProject(p.encode) map(makeUser(_))

  /** Creates a new user.
   */
  def create(project: Project, username: String, password: String, roles: List[String]) = {
    val user = DbUser(username, password, roles.map(DbRole(_, project.id)))
    UserDAO.insert(user)
    makeUser(user) withRoles(project)
  }

  /** Updates the user password.
   */
  def update(before: User, password: String) = {
    val updated = before.copy(password = password.sha512)
    UserDAO.save(updated.encode)
    updated
  }

  def makeUser(u: DbUser) =
    User(u.username, u.password, u.email, rawRoles = u.roles, id = u.id)
}
