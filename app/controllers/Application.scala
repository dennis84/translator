package translator.controllers

import play.api._
import play.api.mvc._
import play.api.http._
import play.api.i18n.Messages
import com.codahale.jerkson.Json
import translator._
import translator.models._

object Application extends BaseController

trait BaseController extends Controller with Actions with Results with RequestGetter

trait Results extends Controller {

  def JsonOk(map: Map[String, Any]) = Ok(Json generate map) as JSON

  def JsonOk(map: Option[Map[String, Any]]) = Ok(Json generate map.getOrElse(Map.empty[String, Any])) as JSON

  def JsonOk(list: List[Any]) = Ok(Json generate list) as JSON

  def JsonOk = Ok(Json generate Map()) as JSON

  def JsonBadRequest(map: Map[String, Any]) =
    BadRequest(Json generate map) as JSON
  
  def JsonBadRequest(errors: Seq[play.api.data.FormError]) =
    BadRequest(Json generate errors.map {
      case error if (error.message == "") => None
      case error if (error.key == "")     => Some(Map("name" -> "global", "message" -> Messages(error.message)))
      case error                          => Some(Map("name" -> error.key, "message" -> Messages(error.message)))
    }.flatten) as JSON

  def JsonUnauthorized = Unauthorized

  def JsonNotFound = NotFound
}

trait Actions extends Controller with Results with RequestGetter {

  def Open(f: Context[AnyContent] => Result): Action[AnyContent] =
    Open(BodyParsers.parse.anyContent)(f)

  def Open[A](p: BodyParser[A])(f: Context[A] => Result): Action[A] =
    Action(p)(req => f(makeContext(req)))

  def Secured(f: Context[AnyContent] => Result): Action[AnyContent] =
    Secured(BodyParsers.parse.anyContent)(f)

  def Secured[A](bp: BodyParser[A])(f: Context[A] => Result): Action[A] =
    Open(bp)(implicit ctx => if (ctx.user.isAnon) JsonNotFound else f(ctx))

  def WithProject(id: String)(f: ProjectContext[AnyContent] => Result): Action[AnyContent] =
    WithProject(id, List("ROLE_ADMIN", "ROLE_AUTHOR"), BodyParsers.parse.anyContent)(f)

  def WithProject(id: String, roles: String*)(f: ProjectContext[AnyContent] => Result): Action[AnyContent] =
    WithProject(id, roles.toList, BodyParsers.parse.anyContent)(f)

  def WithProject[A](
    id: String,
    roles: List[String],
    bp: BodyParser[A]
  )(f: ProjectContext[A] => Result): Action[A] = {
    ProjectAPI.byId(id) map { p =>
      Open(bp) { implicit ctx =>
        if(ctx.user.isAnon && p.open == false)
          JsonNotFound
        else if(ctx.user.isAnon && p.open)
          f(ProjectContext(ctx.req, ctx.user.withRoles(p), p, List(p)))
        else if(p.open)
          f(ProjectContext(ctx.req, ctx.user.withRoles(p), p, (ctx.projects :+ p).distinct))
        else
          f(ProjectContext(ctx.req, ctx.user.withRoles(p), p, ctx.projects))
      }
    } getOrElse Action(bp)(req => JsonNotFound)
  }

  private def makeContext[A](req: Request[A]): Context[A] = {
    implicit val r = req
    (for {
      t <- get("token")
      p <- ProjectAPI.byToken(t)
      u <- UserAPI.by(p)
    } yield {
      Context(req, u.withRoles(p), List(p.withUser(u)))
    }) getOrElse {
      (for {
        n <- req.session.get("username")
        u <- UserAPI.by(n)
      } yield {
        Context(req, u, ProjectAPI.listMine(u))
      }) getOrElse Context(req, User.Anonymous)
    }
  }
}

trait RequestGetter {

  protected def get(name: String)(implicit req: Request[_]): Option[String] =
    get(name, req)

  protected def get(name: String, req: RequestHeader): Option[String] =
    req.queryString get name flatMap (_.headOption) filter (""!=)

  protected def getOr(name: String, default: String)(implicit req: Request[_]) =
    get(name, req) getOrElse default

  protected def getBoolean(name: String)(implicit req: Request[_]): Boolean =
    get(name, req) match {
      case Some("true") => true
      case _ => false
    }

  protected def getAll(name: String)(implicit req: Request[_]): List[String] =
    req.queryString.get(name).map(_.toList) getOrElse List.empty[String]
}
