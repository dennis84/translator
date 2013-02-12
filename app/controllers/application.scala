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

trait BaseController extends Controller with MongoController {

  lazy val env = Env(Play.unsafeApplication)

  def JsonOk(list: List[JsObject]) = Ok(Json.toJson(list))
  def JsonOk(obj: JsObject) = Ok(obj)
  def JsonOk(f: Future[JsValue]) = f.map(Ok(_))

  def Open(f: Context[AnyContent] ⇒ Future[Result]): Action[AnyContent] =
    Open(BodyParsers.parse.anyContent)(f)

  def Open[A](p: BodyParser[A])(f: Context[A] ⇒ Future[Result]): Action[A] =
    Action(p)(req ⇒ Async {
      for {
        ctx ← makeContext(req)
        res ← f(ctx)
      } yield res
    })

  private def makeContext[A](req: Request[A]): Future[Context[A]] =
    (for {
      username ← req.session.get("username")
    } yield for {
      maybeUser ← env.userRepo.byUsername(username)
      context = maybeUser.map(user ⇒ Context(req, user)).getOrElse(Context(req, User.Anonymous))
    } yield context) getOrElse Future(Context(req, User.Anonymous))
}
