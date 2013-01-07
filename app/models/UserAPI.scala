package translator.models

import com.roundeights.hasher.Implicits._

object UserAPI {

  import Implicits._

  /** Returns the admin user of passed project.
   */
  def by(project: DbProject): Option[User] =
    UserDAO.findOneById(project.adminId) map(makeUser(_))

  /** Returns the user by username.
   */
  def by(username: String): Option[User] =
    UserDAO.findOneByUsername(username) map(makeUser(_))

  /** Returns a list of all user who are contributors in passed project.
   */
  def contributors(p: Project): List[User] =
    UserDAO.findAllByProject(p) map(makeUser(_))

  /** Creates a new user.
   */
  def create(project: Project, username: String, password: String, roles: List[String]): Option[User] = for {
    _ <- Some("")
    user = DbUser(username, password, roles.map(DbRole(_, project.id)))
    _ <- UserDAO.insert(user)
  } yield makeUser(user).withRoles(project)

  /** Updates the user password.
   */
  def update(before: User, password: String): User = {
    val updated = before.copy(password = password.sha512)
    UserDAO.save(updated)
    updated
  }

  def makeUser(u: DbUser): User =
    User(u.username, u.password, u.email, rawRoles = u.roles, id = u.id)
}
