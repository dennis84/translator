package translator
package controllers

import scala.concurrent._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.modules.reactivemongo.MongoController
import translator.core._
import translator.user._
import translator.project._

object Application extends BaseController

trait BaseController extends Controller with MongoController with RequestGetter {

  lazy val env = Env(Play.unsafeApplication)

  def FOk(body: JsValue) = Future(Ok(body))

  def Open(f: Context[AnyContent] ⇒ Future[Result]): Action[AnyContent] =
    Open(BodyParsers.parse.anyContent)(f)

  def Open[A](p: BodyParser[A])(f: Context[A] ⇒ Future[Result]): Action[A] =
    Action(p)(req ⇒ Async {
      for {
        ctx ← makeContext(req)
        res ← f(ctx)
      } yield res
    })

  def Secured(f: Context[AnyContent] ⇒ Future[Result]): Action[AnyContent] =
    Secured(BodyParsers.parse.anyContent)(f)

  def Secured[A](p: BodyParser[A])(f: Context[A] ⇒ Future[Result]): Action[A] =
    Open(p)(implicit ctx ⇒ if (ctx.user.isAnon) Future(NotFound) else f(ctx))

  def WithProject(id: String)(f: ProjectContext[AnyContent] ⇒ Future[Result]): Action[AnyContent] =
    WithProject(id, List("ROLE_ADMIN", "ROLE_AUTHOR"), BodyParsers.parse.anyContent)(f)

  def WithProject(id: String, roles: String*)(f: ProjectContext[AnyContent] ⇒ Future[Result]): Action[AnyContent] =
    WithProject(id, roles.toList, BodyParsers.parse.anyContent)(f)

  def WithProject[A](
    id: String,
    roles: List[String],
    parser: BodyParser[A]
  )(f: ProjectContext[A] ⇒ Future[Result]): Action[A] =
    Open(parser) { implicit ctx ⇒
      for {
        maybeProject ← env.projectRepo.byId(id)
        result ← maybeProject.map { project ⇒
          if(ctx.user.isAnon && project.open == false)
            Future(NotFound)
          else if(ctx.user.isAnon && project.open)
            f(ProjectContext(ctx.req, ctx.user.withRoles(project), project, List(project)))
          else if(project.open)
            f(ProjectContext(ctx.req, ctx.user.withRoles(project), project, (ctx.projects :+ project).distinct))
          else
            f(ProjectContext(ctx.req, ctx.user.withRoles(project), project, ctx.projects))
        }.getOrElse(Future(NotFound))
      } yield result
    }

  private def makeContext[A](req: Request[A]): Future[Context[A]] =
    (for {
      username ← req.session.get("username")
    } yield for {
      maybeUser ← env.userRepo.byUsername(username)
      result ← maybeUser.map { user ⇒
        env.projectRepo.listByIds(user.dbRoles.map(_.projectId)).map(list ⇒ Context(req, user, list))
      }.getOrElse(Future(Context(req, User.Anonymous)))
    } yield result).getOrElse {
      Future(Context(req, User.Anonymous))
    }
}

trait RequestGetter {

  protected def get(name: String)(implicit req: Request[_]): Option[String] =
    get(name, req)

  protected def get(name: String, req: RequestHeader): Option[String] =
    req.queryString get name flatMap (_.headOption) filter ("" != _)

  protected def getOr(name: String, default: String)(implicit req: Request[_]) =
    get(name, req) getOrElse default

  protected def getBoolean(name: String)(implicit req: Request[_]): Boolean =
    get(name, req) match {
      case Some("true") ⇒ true
      case _ ⇒ false
    }

  protected def getAll(name: String)(implicit req: Request[_]): List[String] =
    req.queryString.get(name).map(_.toList) getOrElse List.empty[String]
}
