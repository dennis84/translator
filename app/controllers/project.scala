package translator
package controllers

import scala.concurrent._
import play.api.libs.json._
import translator.core._
import translator.project._
import translator.user._

object ProjectController extends BaseController {

  def list = Secured { implicit ctx ⇒
    FOk(Json.toJson(ctx.projects.map(_.toJson)))
  }

  def read(id: String) = WithProject(id) { implicit ctx ⇒
    for {
      trans ← env.transRepo.listByProject(ctx.project)
      langs ← env.langRepo.listByProject(ctx.project)
      project = ctx.project.withStats(trans, langs)
      tokenJson = Json.obj("token" -> project.token)
      projectJson = if(ctx.user.isAdmin) project.toJson ++ tokenJson else project.toJson
    } yield Ok(projectJson)
  }

  def create = Secured { implicit ctx ⇒
    env.projectForms.create.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors),
      name ⇒ for {
        maybeProject ← env.projectRepo.byName(name)
        result ← maybeProject.map { project ⇒
          FBadRequest("name" -> "project_name_taken")
        }.getOrElse {
          val project = Project(Doc.mkID, name, Doc.mkToken, ctx.user.id)
          val updatedUser = ctx.user.copy(
            dbRoles = Role.Admin(project.id) :: ctx.user.dbRoles)

          for {
            _ ← env.userRepo.update(updatedUser)
            f ← env.projectRepo.insert(project).map(_ ⇒ Ok(project.toJson))
          } yield f
        }
      } yield result
    )
  }

  def update(id: String) = WithProject(id, Role.ADMIN) { implicit ctx ⇒
    env.projectForms.update.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (repo, open) ⇒ {
        val updated = ctx.project.copy(repo = repo, open = open)
        env.projectRepo.update(updated).map(_ ⇒ Ok(updated.toJson))
      }
    })
  }

  def signup = Open { implicit req ⇒
    env.projectForms.signup.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (name, username, password, _) ⇒
      for {
        maybeProject ← env.projectRepo.byName(name)
        result ← maybeProject.map { project ⇒
          FBadRequest("name" -> "project_name_taken")
        }.getOrElse(for {
          maybeUser ← env.userRepo.byUsername(username)
          result ← maybeUser.map { user ⇒
            FBadRequest("username" -> "username_taken")
          }.getOrElse {
            val user = User(Doc.mkID, username, password)
            val project = Project(Doc.mkID, name, Doc.mkToken, user.id)
            val userWithProject = user.copy(dbRoles = List(Role.Admin(project.id)))
            for {
              _ ← env.userRepo.insert(userWithProject)
              f ← env.projectRepo.insert(project).map(_ ⇒
                Ok(project.toJson).withSession("username" -> username))
            } yield f
          }
        } yield result)
      } yield result
    })
  }
}
