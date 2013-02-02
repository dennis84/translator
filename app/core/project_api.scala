package translator
package core

import scala.concurrent._
import play.api.libs.json._

class ProjectApi(
  projectRepo: ProjectRepo,
  userRepo: UserRepo,
  transRepo: TransRepo,
  langRepo: LangRepo) {

  def full(implicit ctx: ProjectContext[_]): Project = ctx.project withUser(ctx.user)

  def listMine(u: User): Future[JsValue] =
    projectRepo.listByIds(u.dbRoles.map(_.projectId)) map { list ⇒
      Json.toJson(list.map(_.toJson))
    }

  def create(name: String, user: User) =
    for {
      e ← projectRepo.byName(name)
      if(!e.isDefined)
      p = Project(Doc.mkID, name, Doc.mkToken, user.id)
      u = user.copy(dbRoles = Role.Admin(p.id) :: user.dbRoles)
      _ ← userRepo.update(u)
      f ← projectRepo.insert(p).map(_ ⇒ p.toJson)
    } yield f

  // def update(id: String, repo: String, open: Boolean): Option[Project] = for {
  //   p ← projectDAO.byId(id)
  //   project = p.copy(repo = Some(repo), open = open)
  //   wc = projectDAO.save(project.encode)
  // } yield project withStats(
  //   transDAO.list(project),
  //   langDAO.list(project))

  // def signup(
  //   projectName: String,
  //   username: String,
  //   password: String
  // ): Option[(User, Project)] = for {
  //   _ ← Some("")
  //   u = User(username, password sha512)
  //   _ ← userDAO.insert(u)
  //   p ← create(projectName, u)
  // } yield (u, p)

  // private def uuid = java.util.UUID.randomUUID.toString
}
