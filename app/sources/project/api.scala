package translator
package project

import scala.concurrent._
import play.api.libs.json._
import translator.core._
import translator.project._
import translator.user._
import translator.lang._
import translator.trans._

class ProjectApi(
  projectRepo: ProjectRepo,
  userRepo: UserRepo,
  transRepo: TransRepo,
  langRepo: LangRepo) extends Api {

  // @todo with stats
  def full(implicit ctx: ProjectContext[_]): Project =
    ctx.project withUser(ctx.user)

  def listMine(u: User): Future[JsValue] = api {
    projectRepo.listByIds(u.dbRoles.map(_.projectId)) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }
  }

  def create(name: String, user: User): Future[JsValue] = api {
    for {
      e ← projectRepo.byName(name)
      _ ← failsIf(e.isDefined, "project_name_taken")
      p = Project(Doc.mkID, name, Doc.mkToken, user.id)
      u = user.copy(dbRoles = Role.Admin(p.id) :: user.dbRoles)
      _ ← userRepo.update(u)
      f ← projectRepo.insert(p).map(_ ⇒ p.toJson)
    } yield f
  }

  // @todo with stats
  def update(id: String, repo: String, open: Boolean): Future[JsValue] = api {
    for {
      mp ← projectRepo.byId(id)
      e ← get(mp, "project_not_found")
      p = e.copy(repo = repo, open = open)
      f ← projectRepo.update(p).map(_ ⇒ p.toJson)
    } yield f
  }

  def signup(
    projectName: String,
    username: String,
    password: String
  ): Future[JsValue] = api {
    for {
      ep ← projectRepo.byName(projectName)
      _ ← failsIf(ep.isDefined, "project_name_taken")
      eu ← userRepo.byUsername(username)
      _ ← failsIf(eu.isDefined, "username_taken")
      tu = User(Doc.mkID, username, password)
      p = Project(Doc.mkID, projectName, Doc.mkToken, tu.id)
      u = tu.copy(dbRoles = List(Role.Admin(p.id)))
      _ ← userRepo.insert(u)
      f ← projectRepo.insert(p).map(_ ⇒ p.toJson)
    } yield f
  }
}
