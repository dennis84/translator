package translator.core

import com.roundeights.hasher.Implicits._

class UserAPI(userDAO: UserDAO) {

  import Implicits._

  def by(project: Project): Option[User] =
    userDAO.byId(project.adminId)

  def by(username: String): Option[User] = userDAO.byUsername(username)

  def contributors(p: Project): List[User] = userDAO.list(p)

  def create(
    project: Project,
    username: String,
    password: String,
    roles: List[String]
  ): Option[User] = for {
    _ <- Some("")
    u = User(
      username,
      password,
      rawRoles = roles.map(DbRole(_, project.id))
    )
    _ <- userDAO.insert(u)
  } yield u.withRoles(project)

  def update(before: User, password: String): Option[User] = for {
    u <- userDAO.byId(before.id)
    up = u.copy(password = password.sha512)
    wc = userDAO.save(up)
  } yield up

  def authenticate(u: String, p: String): Option[User] =
    userDAO byCredentials(u, p.sha512)
}
