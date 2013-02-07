package translator
package user

import scala.concurrent._
import play.api.libs.json._
import translator.core._
import translator.project._

class UserApi(userRepo: UserRepo) extends Api {

  // @todo with roles
  def contributors(p: Project): Future[JsValue] =
    userRepo.listByProject(p) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }

  def authenticate(u: String, p: String): Future[JsValue] = api {
    for {
      u ← userRepo.byCredentials(u, p)
      user ← get(u, "user_not_found")
    } yield user.toJson
  }

  def create(
    project: Project,
    username: String,
    password: String,
    roles: List[String]
  ): Future[JsValue] = api {
    for {
      e ← userRepo.byUsername(username)
      _ ← failsIf(e.isDefined, "username_taken")
      u = User(Doc.mkID, username, password, dbRoles = roles.map(Role(_, project.id)))
      f ← userRepo.insert(u).map(_ ⇒ u.toJson)
    } yield f
  }

  def updatePassword(id: String, password: String) = api {
    for {
      e ← userRepo.byId(id)
      u ← get(e, "user_not_found")
      f ← userRepo.update(u.copy(password = password)).map(_ ⇒ u.toJson)
    } yield f
  }

  def updateRoles(p: Project, id: String, r: List[String]) = api {
    for {
      e ← userRepo.byId(id)
      u ← get(e, "user_not_found")
      up = u.copy(
        dbRoles = u.dbRoles.filterNot(_.projectId == p.id) ++ r.map(Role(_, p.id))
      )
      f ← userRepo.update(up).map(_ ⇒ up.toJson)
    } yield f
  }

  def usernamesLike(username: String) =
    userRepo.listLike(username) map { list ⇒
      Json.toJson(list.map(_.username))
    }

  // @todo with roles
  // @todo write test
  def add(p: Project, n: String, r: List[String]) = api {
    for {
      e ← userRepo.byUsername(n)
      u ← get(e, "user_not_found")
      roles = u.dbRoles ++ r.map(Role(_, p.id))
      user = u.copy(dbRoles = roles)
      f ← userRepo.update(user).map(_ ⇒ user.toJson)
    } yield f
  }
}
