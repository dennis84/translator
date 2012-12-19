package translator.models

import com.roundeights.hasher.Implicits._

object UserAPI {

  /** Creates the user view by user.
   */
  def by(user: User) = UserVeiw(user)

  def by(user: User, project: Project) = UserVeiw(user, user.roles.filter(_.projectId == project.id).map(_.role))

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
}
