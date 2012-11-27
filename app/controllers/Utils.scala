package translator.controllers

import play.api._
import play.api.mvc._
import play.api.http._
import com.codahale.jerkson.Json
import translator._
import translator.models._

trait BaseController extends Controller with Actions with Results with RequestGetter

trait Results extends Controller {

  def JsonOk(map: Map[String, Any]) = Ok(Json generate map) as JSON

  def JsonOk(list: List[Any]) = Ok(Json generate list) as JSON

  def JsonOk = Ok(Json generate Map()) as JSON

  def JsonBadRequest(map: Map[String, Any]) = BadRequest(Json generate map) as JSON
  
  def JsonBadRequest(errors: Seq[play.api.data.FormError]) = BadRequest(Json generate errors.map {
    case error if (error.message == "") => None
    case error if (error.key == "")     => Some(Map("name" -> "global", "message" -> error.message))
    case error                          => Some(Map("name" -> error.key, "message" -> error.message))
  }.flatten) as JSON

  def JsonUnauthorized = Unauthorized

  def JsonNotFound = NotFound
}

trait Actions extends Controller with Results with RequestGetter {

  def OpenIO(f: Context[AnyContent] => Result): Action[AnyContent] = OpenIO(BodyParsers.parse.anyContent)(f)

  def OpenIO[A](p: BodyParser[A])(f: Context[A] => Result): Action[A] = Action(p) { implicit req =>
    val user = req.session.get("username").flatMap(u => UserDAO.findOneByUsername(u))
    f(Context[A](req, user))
  }

  def SecuredIO(f: Context[AnyContent] => Result): Action[AnyContent] = SecuredIO(BodyParsers.parse.anyContent)(f)
  
  def SecuredIO[A](p: BodyParser[A])(f: Context[A] => Result): Action[A] = OpenIO(p) { implicit ctx =>
    (for {
      token <- get("token")
      project <- ProjectDAO.findOneByToken(token)
      user <- project.admin
    } yield {
      f(ctx.copy(user = Some(user), projects = List(project)))
    }) getOrElse {
      ctx.user map { user =>
        f(ctx.copy(projects = ProjectDAO.findAllByUser(user)))
      } getOrElse JsonUnauthorized
    }
  }
}

trait RequestGetter {

  protected def get(name: String)(implicit ctx: Context[_]): Option[String] = get(name, ctx.req)

  protected def get(name: String, req: RequestHeader): Option[String] = req.queryString get name flatMap (_.headOption) filter (""!=)

  protected def getOr(name: String, default: String)(implicit ctx: Context[_]) = get(name, ctx.req) getOrElse default

  protected def getAll(name: String, req: RequestHeader) = req.queryString get name

  protected def getAllOr(name: String, default: Seq[String])(implicit ctx: Context[_]) = getAll(name, ctx.req) getOrElse (default)
}
