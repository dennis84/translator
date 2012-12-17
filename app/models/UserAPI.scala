package translator.models

import com.roundeights.hasher.Implicits._

object UserAPI {

  /** Creates a new user.
   */
  def create(project: Project, username: String, password: String, roles: List[String]) = {
    val user = User(username, password, roles.map(Role(_, project.id)))
    UserDAO.insert(user)
    user
  }

  /** Updates the user password.
   */
  def update(before: User, password: String) = {
    val updated = before.copy(password = password.sha512)
    UserDAO.save(updated)
    updated
  }
}
