package translator.controllers

import play.api._
import play.api.mvc._
import play.api.http._
import play.api.i18n.Messages
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
    case error if (error.key == "")     => Some(Map("name" -> "global", "message" -> Messages(error.message)))
    case error                          => Some(Map("name" -> error.key, "message" -> Messages(error.message)))
  }.flatten) as JSON

  def JsonUnauthorized = Unauthorized

  def JsonNotFound = NotFound
}

trait Actions extends Controller with Results with RequestGetter {

  def Open(f: Request[AnyContent] => Result): Action[AnyContent] = Open(BodyParsers.parse.anyContent)(f)

  def Open[A](p: BodyParser[A])(f: Request[A] => Result): Action[A] = Action(p) { implicit req =>
    f(req)
  }

  def SecuredWithProject(id: String)(f: (User, Project, List[Project], Request[AnyContent]) => Result): Action[AnyContent] =
    SecuredWithProject(id, BodyParsers.parse.anyContent)(f)

  def SecuredWithProject[A](id: String, p: BodyParser[A])(f: (User, Project, List[Project], Request[A]) => Result): Action[A] =
    Secured(p) { (user, projects, req) =>
      projects.find(_.id == id) map { project =>
        f(user, project, projects, req)
      } getOrElse JsonNotFound
    }

  def Secured(f: (User, List[Project], Request[AnyContent]) => Result): Action[AnyContent] =
    Secured(BodyParsers.parse.anyContent)(f)

  def Secured[A](p: BodyParser[A])(f: (User, List[Project], Request[A]) => Result): Action[A] = Open(p) { implicit req =>
    (for {
      token <- get("token")
      project <- ProjectDAO.findOneByToken(token)
      user <- project.admin
    } yield {
      f(user, List(project), req)
    }) getOrElse {
      req.session.get("username").flatMap(u => UserDAO.findOneByUsername(u)) map { user =>
        f(user, ProjectDAO.findAllByUser(user), req)
      } getOrElse JsonNotFound
    }
  }
}

trait RequestGetter {

  protected def get(name: String)(implicit req: Request[_]): Option[String] = get(name, req)

  protected def get(name: String, req: RequestHeader): Option[String] = req.queryString get name flatMap (_.headOption) filter (""!=)

  protected def getOr(name: String, default: String)(implicit req: Request[_]) = get(name, req) getOrElse default

  protected def getAll(name: String, req: RequestHeader) = req.queryString get name

  protected def getAllOr(name: String, default: Seq[String])(implicit req: Request[_]) = getAll(name, req) getOrElse (default)
}
