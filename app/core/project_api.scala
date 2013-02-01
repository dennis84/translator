package translator
package core

class ProjectApi(
  projectRepo: ProjectRepo,
  userRepo: UserRepo,
  transRepo: TransRepo,
  langRepo: LangRepo) {

  def full(implicit ctx: ProjectContext[_]): Project = ctx.project withUser(ctx.user)

  // def byId(id: String): Option[Project] = projectDAO.byId(id)

  // def byToken(token: String): Option[Project] = projectDAO.byToken(token)

  // def listMine(user: User): List[Project] =
  //   projectDAO.listByIds(user.rawRoles.map(_.projectId))

  // def create(name: String, user: User): Option[Project] = for {
  //   _ ← Some("")
  //   p = Project(name, uuid, user.id)
  //   u = user.copy(rawRoles = user.rawRoles :+ Role.Admin(p.id))
  //   wc = userDAO.save(u)
  //   _ ← projectDAO.insert(p)
  // } yield p

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
