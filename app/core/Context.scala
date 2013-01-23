package translator.core

import play.api.mvc._

case class Context[A](
  val req: Request[A],
  val user: User,
  val projects: List[Project] = Nil) extends WrappedRequest(req)

case class ProjectContext[A](
  val req: Request[A],
  val user: User,
  val project: Project,
  val projects: List[Project]) extends WrappedRequest(req)
