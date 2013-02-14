package translator
package controllers

import scala.concurrent._
import play.api.libs.json._
import translator.core._
import translator.user._

object UserController extends BaseController {

  def authenticate = Open { implicit req ⇒
    env.userForms.signin.bindFromRequest.fold(
      formWithErrors ⇒ Future(BadRequest("failed")), {
      case (username, password) ⇒
        env.userRepo.byCredentials(username, password).map { maybeUser ⇒
          maybeUser.map {
            user ⇒ Ok(user.toJson) withSession("username" -> username)
          }.getOrElse(NotFound)
        }
    })
  }

  def logout = Open { implicit ctx ⇒
    Future(Ok(Json.toJson("")).withNewSession)
  }

  def current = Secured { implicit ctx ⇒
    FOk(ctx.user.toJson)
  }

  def updateCurrent = Secured { implicit ctx ⇒
    env.userForms.me.bindFromRequest.fold(
      formWithErrors ⇒ Future(BadRequest("failed")), {
      case (password, _) ⇒ {
        val user = ctx.user.copy(password = password)
        env.userRepo.update(user).map(_ ⇒ Ok(user.toJson))
      }
    })
  }

  def currentByProject(project: String) = WithProject(project) { implicit ctx ⇒
    FOk(ctx.user.withRoles(ctx.project).toJson)
  }

  def list(project: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.userRepo.listByProject(ctx.project).map(
      list ⇒ Ok(Json.toJson(list.map(_.withRoles(ctx.project).toJson))))
  }

  def create(project: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.userForms.create.bindFromRequest.fold(
      formWithErrors ⇒ Future(BadRequest("failed")), {
      case (username, password, roles) ⇒ for {
        maybeUser ← env.userRepo.byUsername(username)
        result ← maybeUser.map { user ⇒
          Future(BadRequest("failed"))
        }.getOrElse {
          val user = User(Doc.mkID, username, password, dbRoles = roles.map(Role(_, ctx.project.id)))
          env.userRepo.insert(user).map(_ ⇒ Ok(user.withRoles(ctx.project).toJson))
        }
      } yield result
    })
  }

  def update(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.userForms.update.bindFromRequest.fold(
      formWithErrors ⇒ Future(BadRequest("failed")),
      roles ⇒ for {
        maybeUser ← env.userRepo.byId(id)
        result ← maybeUser.map { user ⇒
          val updated = user.copy(
            dbRoles = user.dbRoles.filterNot { role ⇒
              role.projectId == ctx.project.id
            } ++ roles.map(Role(_, ctx.project.id)))

          env.userRepo.update(updated).map(_ ⇒ Ok(updated.withRoles(ctx.project).toJson))
        }.getOrElse(Future(Ok("")))
      } yield result
    )
  }

  def add(project: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.userForms.add.bindFromRequest.fold(
      formWithErrors ⇒ Future(BadRequest("failed")), {
      case (username, roles) ⇒ for {
        maybeUser ← env.userRepo.byUsername(username)
        result ← maybeUser.map { user ⇒
          val updated = user.copy(dbRoles =
            user.dbRoles ++ roles.map(Role(_, ctx.project.id)))

          env.userRepo.update(updated).map(_ ⇒ Ok(updated.withRoles(ctx.project).toJson))
        }.getOrElse(Future(Ok("")))
      } yield result
    })
  }

  def usernames = Open { implicit ctx ⇒
    get("username").map { u ⇒
      env.userRepo.listLike(u).map(
        list ⇒ Ok(Json.toJson(list.map(_.username))))
    }.getOrElse(FOk(JsArray()))
  }
}
