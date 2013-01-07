package translator.models

import com.roundeights.hasher.Implicits._

object UserAPI {

  import Implicits._

  def by(project: Project): Option[User] =
    UserDAO.byId(project.adminId)

  def by(username: String): Option[User] =
    UserDAO.findOneByUsername(username)

  def contributors(p: Project): List[User] =
    UserDAO.findAllByProject(p)

  def create(
    project: Project,
    username: String,
    password: String,
    roles: List[String]
  ): Option[User] = for {
    _ <- Some("")
    user = User(
      username,
      password,
      rawRoles = roles.map(DbRole(_, project.id))
    )
    _ <- UserDAO.insert(user)
  } yield user.withRoles(project)

  def update(before: User, password: String): Option[User] = for {
    u <- UserDAO.byId(before.id)
    up = u.copy(password = password.sha512)
    wc = UserDAO.save(up)
  } yield up
}
