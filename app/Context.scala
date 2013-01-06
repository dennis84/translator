package translator

import play.api.mvc._
import translator.models._

case class Context[A](
  val req: Request[A],
  val user: User,
  val projects: List[Project]) extends WrappedRequest(req)

case class ProjectContext[A](
  val req: Request[A],
  val user: User,
  val project: Project,
  val projects: List[Project]) extends WrappedRequest(req)
