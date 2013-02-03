package translator
package core

import scala.concurrent._
import play.api.libs.json._
import translator.core.errors._

class ProjectApi(
  projectRepo: ProjectRepo,
  userRepo: UserRepo,
  transRepo: TransRepo,
  langRepo: LangRepo) {

  // @todo with stats
  def full(implicit ctx: ProjectContext[_]): Project =
    ctx.project withUser(ctx.user)

  def listMine(u: User): Future[JsValue] =
    projectRepo.listByIds(u.dbRoles.map(_.projectId)) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }

  def create(name: String, user: User): Future[JsValue] =
    for {
      e ← projectRepo.byName(name)
      e1 = Error("project_already_exists").when(e.isDefined)
      p = Project(Doc.mkID, name, Doc.mkToken, user.id)
      u = user.copy(dbRoles = Role.Admin(p.id) :: user.dbRoles)
      _ ← userRepo.update(u)
      f ← projectRepo.insert(p).map(_ ⇒ p.toJson)
    } yield validate(e1)(f)

  // @todo with stats
  def update(id: String, repo: String, open: Boolean): Future[JsValue] =
    for {
      e ← projectRepo.byId(id)
      if(e.isDefined)
      p = e.get.copy(repo = repo, open = open)
      f ← projectRepo.update(p).map(_ ⇒ p.toJson)
    } yield f

  def signup(
    projectName: String,
    username: String,
    password: String
  ): Future[JsValue] = for {
    ep ← projectRepo.byName(projectName)
    if(!ep.isDefined)
    eu ← userRepo.byUsername(username)
    if(!eu.isDefined)
    tu = User(Doc.mkID, username, password)
    p = Project(Doc.mkID, projectName, Doc.mkToken, tu.id)
    u = tu.copy(dbRoles = List(Role.Admin(p.id)))
    _ ← userRepo.insert(u)
    f ← projectRepo.insert(p).map(_ ⇒ p.toJson)
  } yield f
}
