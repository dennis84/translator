package translator
package core

import scala.concurrent._
import play.api.libs.json._
import scala.util.{Failure, Success}
import translator.core.errors._

class UserApi(userRepo: UserRepo) {

  // @todo with roles
  def contributors(p: Project) =
    userRepo.listByProject(p) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }

  def authenticate(u: String, p: String) =
    for {
      u ← userRepo.byCredentials(u, p)
      e = Error("authentication_failed").when(!u.isDefined)
    } yield validate(e)(u.get.toJson)

  // def create(
  //   project: Project,
  //   username: String,
  //   password: String,
  //   roles: List[String]
  // ): Option[User] = for {
  //   _ ← Some("")
  //   u = User(
  //     username,
  //     password,
  //     rawRoles = roles.map(DbRole(_, project.id))
  //   )
  //   _ ← userDAO.insert(u)
  // } yield u.withRoles(project)

  // def updatePassword(before: User, password: String): Option[User] = for {
  //   u ← userDAO.byId(before.id)
  //   up = u.copy(password = password.sha512)
  //   wc = userDAO.save(up)
  // } yield up

  // def updateRoles(p: Project, id: String, r: List[String]): Option[User] =
  //   for {
  //     u ← userDAO.byId(id)
  //     up = u.copy(
  //       rawRoles = u.rawRoles.filterNot {
  //         _.projectId == p.id
  //       } ++ r.map(DbRole(_, p.id)))
  //     wc = userDAO.save(up)
  //   } yield up.withRoles(p)

  // def usernamesLike(username: String): List[String] =
  //   userDAO.listLike(username) map(_.username)

  // def add(p: Project, n: String, r: List[String]): Option[User] =
  //   for {
  //     u ← userDAO.byUsername(n)
  //     roles = u.rawRoles ++ r.map(DbRole(_, p.id))
  //     user = u.copy(rawRoles = roles)
  //     wc = userDAO.save(user)
  //   } yield user.withRoles(p)
}
