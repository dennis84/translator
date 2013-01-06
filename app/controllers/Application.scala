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

  def Open(f: Request[AnyContent] => Result): Action[AnyContent] =
    Open(BodyParsers.parse.anyContent)(f)

  def Open[A](p: BodyParser[A])(f: Request[A] => Result): Action[A] =
    Action(p) { implicit req =>
      f(req)
    }

  def Secured(f: Context[AnyContent] => Result): Action[AnyContent] =
    Secured(BodyParsers.parse.anyContent)(f)

  def Secured[A](p: BodyParser[A])(f: Context[A] => Result): Action[A] =
    Open(p) { implicit req =>
      (for {
        token <- get("token")
        project <- ProjectAPI.by(token)
        user <- UserAPI.by(project.encode)
      } yield {
        f(Context(req, user, List(project.withUser(user))))
      }) getOrElse {
        req.session.get("username").flatMap(UserAPI.by(_)) map { user =>
          f(Context(req, user, ProjectAPI.listMine(user)))
        } getOrElse JsonNotFound
      }
    }

  def SecuredWithProject(id: String)(f: ProjectContext[AnyContent] => Result): Action[AnyContent] =
    SecuredWithProject(id, List("ROLE_ADMIN", "ROLE_AUTHOR"), BodyParsers.parse.anyContent)(f)

  def SecuredWithProject(id: String, roles: String*)(f: ProjectContext[AnyContent] => Result): Action[AnyContent] =
    SecuredWithProject(id, roles.toList, BodyParsers.parse.anyContent)(f)

  def SecuredWithProject[A](id: String, roles: List[String], p: BodyParser[A])(f: ProjectContext[A] => Result): Action[A] =
    Secured(p) { implicit ctx =>
      (for {
        p <- ctx.projects.find(_.id == id)
        u <- Some(ctx.user.withRoles(p))
        if (!u.roles.filter(roles.contains(_)).isEmpty)
      } yield {
        f(ProjectContext(ctx.req, u, p, ctx.projects))
      }) getOrElse JsonNotFound
    }
}

trait RequestGetter {

  protected def get(name: String)(implicit req: Request[_]): Option[String] =
    get(name, req)

  protected def get(name: String, req: RequestHeader): Option[String] =
    req.queryString get name flatMap (_.headOption) filter (""!=)

  protected def getOr(name: String, default: String)(implicit req: Request[_]) =
    get(name, req) getOrElse default

  protected def getAll(name: String, req: RequestHeader) =
    req.queryString get name

  protected def getAllOr(name: String, default: Seq[String])(implicit req: Request[_]) =
    getAll(name, req) getOrElse (default)
}
