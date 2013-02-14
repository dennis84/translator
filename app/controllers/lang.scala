package translator
package controllers

import scala.concurrent._
import play.api.libs.json._
import translator.core._
import translator.lang._
import translator.user._

object LangController extends BaseController {

  def list(project: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.langRepo.listByProject(ctx.project).map(
      list ⇒ Ok(Json.toJson(list.map(_.toJson))))
  }

  def create(project: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.langForms.lang.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (code, name) ⇒ for {
        maybeLang ← env.langRepo.byCode(ctx.project, code)
        result ← maybeLang.map { lang ⇒
          FBadRequest("code" -> "language_already_exists")
        }.getOrElse {
          val lang = Lang(Doc.mkID, code, name, ctx.project.id, Some(ctx.project))
          env.langRepo.insert(lang).map(_ ⇒ Ok(lang.toJson))
        }
      } yield result
    })
  }

  def update(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.langForms.lang.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (code, name) ⇒ for {
        maybeLang ← env.langRepo.byId(id)
        result ← maybeLang.map { lang ⇒
          val updated = lang.copy(code = code, name = name)
          env.langRepo.update(updated).map(_ ⇒ Ok(updated.toJson))
        }.getOrElse(FBadRequest("global" -> "language_not_found"))
      } yield result
    })
  }

  def delete(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    for {
      maybeLang ← env.langRepo.byId(id)
      result ← maybeLang.map { lang ⇒
        env.langRepo.remove(lang).map(_ ⇒ Ok(lang.toJson))
      }.getOrElse(FBadRequest("global" -> "language_not_found"))
    } yield result
  }
}
