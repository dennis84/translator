package translator.models

import com.roundeights.hasher.Implicits._

object UserAPI {

  def by(project: DbProject) =
    UserDAO.findOneById(project.adminId) map(makeUser(_, ProjectAPI.by(project)))

  /** Creates the user view by user.
   */
  def by(user: DbUser) = makeUser(user)

  def by(user: DbUser, project: Project) = makeUser(user).withRoles(user, project)

  /** Creates a new user.
   */
  def create(project: Project, username: String, password: String, roles: List[String]) = {
    val user = User(username, password, roles.map(Role(_, project.id)))
    UserDAO.insert(user)
    UserVeiw(user)
  }

  /** Updates the user password.
   */
  def update(before: User, password: String) = {
    val updated = before.copy(password = password.sha512)
    UserDAO.save(updated)
    UserVeiw(updated)
  }

  def makeUser(u: DbUser) =
    User(u.id, u.username, u.password, u.email, u.roles)
}
