package translator.core

import com.roundeights.hasher.Implicits._

class UserAPI(userDAO: UserDAO) {

  import Implicits._

  def by(project: Project): Option[User] =
    userDAO.byId(project.adminId)

  def by(username: String): Option[User] = userDAO.byUsername(username)

  def contributors(p: Project): List[User] = userDAO.list(p) map(_.withRoles(p))

  def create(
    project: Project,
    username: String,
    password: String,
    roles: List[String]
  ): Option[User] = for {
    _ ← Some("")
    u = User(
      username,
      password,
      rawRoles = roles.map(DbRole(_, project.id))
    )
    _ ← userDAO.insert(u)
  } yield u.withRoles(project)

  def updatePassword(before: User, password: String): Option[User] = for {
    u ← userDAO.byId(before.id)
    up = u.copy(password = password.sha512)
    wc = userDAO.save(up)
  } yield up

  def updateRoles(p: Project, id: String, r: List[String]) = for {
    u ← userDAO.byId(id)
    up = u.copy(
      rawRoles = u.rawRoles.filterNot {
        _.projectId == p.id
      } ++ r.map(DbRole(_, p.id)))
    wc = userDAO.save(up)
  } yield up.withRoles(p)

  def authenticate(u: String, p: String): Option[User] =
    userDAO byCredentials(u, p.sha512)

  def usernamesLike(username: String): List[String] =
    userDAO.listLike(username) map(_.username)

  def add(p: Project, n: String, r: List[String]): Option[User] = for {
    u ← userDAO.byUsername(n)
    roles = u.rawRoles ++ r.map(DbRole(_, p.id))
    user = u.copy(rawRoles = roles)
    wc = userDAO.save(user)
  } yield user.withRoles(p)
}
