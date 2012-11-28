package translator.controllers

import play.api._
import play.api.mvc._
import translator.models._

object Application extends BaseController

case class Context[A](
  val req:Request[A],
  val user: Option[User] = None,
  val project: Option[Project] = None,
  val projects: List[Project] = Nil) extends WrappedRequest[A](req)
