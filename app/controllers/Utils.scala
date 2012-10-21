package translator.controllers

import play.api._
import play.api.mvc._
import play.api.http._
import com.codahale.jerkson.Json
import translator._
import translator.models._

trait BaseController extends Controller with Actions with Results

trait Results extends Controller {

  def JsonOk(map: Map[String, Any]) = Ok(Json generate map) as JSON

  def JsonOk(list: List[Any]) = Ok(Json generate list) as JSON

  def JsonOk = Ok(Json generate Map()) as JSON

  def JsonBadRequest(map: Map[String, Any]) = BadRequest(Json generate map) as JSON

  def JsonUnauthorized = Unauthorized
}

trait Actions extends Controller with Results {

  def OpenIO(f: Context[AnyContent] => Result): Action[AnyContent] = OpenIO(BodyParsers.parse.anyContent)(f)

  def OpenIO[A](p: BodyParser[A])(f: Context[A] => Result): Action[A] = Action(p) { implicit req =>
    val user = req.session.get("username").flatMap(u => UserDAO.findOneByUsername(u))
    f(Context[A](req, user))
  }

  def SecuredIO(f: Context[AnyContent] => Result): Action[AnyContent] = SecuredIO(BodyParsers.parse.anyContent)(f)

  def SecuredIO[A](p: BodyParser[A])(f: Context[A] => Result): Action[A] = OpenIO(p) { implicit ctx =>
    ctx.user map { user =>
      f(ctx.copy(projects = ProjectDAO.findAllByUser(user)))
    } getOrElse JsonUnauthorized
  }
}
