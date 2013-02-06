package translator
package controllers

import scala.concurrent._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.modules.reactivemongo.MongoController
import translator.core._

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

  def AsyncAction(f: Request[AnyContent] => Future[Result]): Action[AnyContent] =
    AsyncAction(BodyParsers.parse.anyContent)(f)
  
  def AsyncAction[A](p: BodyParser[A])(f: Request[A] => Future[Result]): Action[A] =
    Action(p) { req =>
      Async(f(req))
    }

  private def makeContext[A](req: Request[A]): Future[Context[A]] = {
    implicit val r = req
    val anonCtx = Context(req, User.Anonymous)
    req.session.get("username") map { username ⇒
      env.userRepo.byUsername(username) map { user ⇒
        user match {
          case Some(u) ⇒ Context(req, u)
          case None ⇒ anonCtx
        }
      }
    } getOrElse Future(anonCtx)
  }

  // private def makeContext[A](req: Request[A]): Context[A] = {
  //   implicit val r = req
  //   (for {
  //     t ← get("token")
  //     p ← env.projectAPI.byToken(t)
  //     u ← env.userAPI.by(p)
  //   } yield {
  //     Context(req, u.withRoles(p), List(p.withUser(u)))
  //   }) getOrElse {
  //     (for {
  //       n ← req.session.get("username")
  //       u ← env.userAPI.by(n)
  //     } yield {
  //       Context(req, u, env.projectAPI.listMine(u))
  //     }) getOrElse Context(req, User.Anonymous)
  //   }
  // }
}

// object Application extends BaseController with MongoController {
//   lazy val env = Env(Play.unsafeApplication)

//   def show(id: String) = AsyncAction { req =>
//     env.transRepo.byId(id) map { trans =>
//       Ok("yo")
//     }
//   }

//   def index = AsyncAction { req =>
//     JsonOk(env.transApi.entries)
//   }

//   def create = AsyncAction { req =>
//     val trans = Trans(BSONObjectID.generate, "en", "hello", "Hello World", "dd")

//     env.transRepo.insert(trans).map { _ =>
//       JsonOk(Json.obj("foo" -> "bar"))
//     }
//   }

//   def update(id: String) = AsyncAction { req ⇒
//     for {
//       maybeTrans ← env.transRepo.byId(id)
//       result ← maybeTrans.map { trans ⇒
//         env.transRepo.update(trans.copy(
//           text = "Bla bla blubb"
//         )) map { updated ⇒
//           Ok("trans Updated")
//         }
//       }.getOrElse(Future(NotFound))
//     } yield result
//   }
// }
