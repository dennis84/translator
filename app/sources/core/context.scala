package translator
package core

import play.api.mvc._
import translator.user.User
import translator.project.Project

case class Context[A](
  val req: Request[A],
  val user: User,
  val projects: List[Project] = Nil) extends WrappedRequest(req)

case class ProjectContext[A](
  val req: Request[A],
  val user: User,
  val project: Project,
  val projects: List[Project]) extends WrappedRequest(req)
