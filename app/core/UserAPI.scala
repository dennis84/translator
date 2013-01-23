package translator.core

import com.roundeights.hasher.Implicits._

object UserAPI {

  import Implicits._

  def by(project: Project): Option[User] =
    UserDAO.byId(project.adminId)

  def by(username: String): Option[User] = UserDAO.byUsername(username)

  def contributors(p: Project): List[User] = UserDAO.list(p)

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
    _ <- UserDAO.insert(u)
  } yield u.withRoles(project)

  def update(before: User, password: String): Option[User] = for {
    u <- UserDAO.byId(before.id)
    up = u.copy(password = password.sha512)
    wc = UserDAO.save(up)
  } yield up
}
