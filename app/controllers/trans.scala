package translator
package controllers

import scala.concurrent._
import com.github.nscala_time.time.Imports._
import play.api.libs.json._
import translator.core._
import translator.trans._
import translator.trans.{ Status ⇒ Stat }
import translator.trans.list._
import translator.user._

object TransController extends BaseController {

  def read(project: String, id: String) = WithProject(project) { implicit ctx ⇒
    for {
      maybeTrans ← env.transRepo.byId(id)
      result ← maybeTrans.map { trans ⇒
        for {
          langs ← env.langRepo.listByProject(ctx.project)
          children ← env.transRepo.listByName(ctx.project, trans.name)
          entry = Entry(trans, ctx.project, langs, children)
        } yield Ok(entry.toJson)
      }.getOrElse(FNotFound)
    } yield result
  }

  def list(project: String) = WithProject(project) { implicit ctx ⇒
    get("name") map { name ⇒
      for {
        langs ← env.langRepo.listByProject(ctx.project)
        trans ← env.transRepo.listByName(ctx.project, name).map { list ⇒
          Ok(Json.toJson(list.mkComplete(langs).map(_.toJson)))
        }
      } yield trans
    } getOrElse {
      val filter = Filter(
        getBoolean("untranslated"),
        getAll("untranslated_languages"),
        getBoolean("activatable"))

      for {
        langs ← env.langRepo.listByProject(ctx.project)
        trans ← env.transRepo.listByProject(ctx.project)
        entries = trans.mkEntries(langs)
      } yield Ok(Json.toJson(filter.filter(entries).map(_.toJson)))
    }
  }

  def create(project: String) = WithProject(project) { implicit ctx ⇒
    env.transForms.trans.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (code, name, text) ⇒ for {
        maybeTrans ← env.transRepo.activated(ctx.project, name, code)
        trans = Trans(code, name, text, ctx.user, ctx.project)
        status = if(ctx.user.isAdmin && !maybeTrans.isDefined) Stat.Active
                 else Stat.Inactive
        transWithStatus = trans.copy(status = status)
        result ← env.transRepo.insert(transWithStatus).map(_ ⇒ Ok(transWithStatus.toJson))
      } yield result
    })
  }

  def update(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.transForms.trans.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (code, name, text) ⇒ for {
        maybeTrans ← env.transRepo.byId(id)
        result ← maybeTrans.map { trans ⇒
          val updated = trans.copy(text = text, updatedAt = DateTime.now)
          env.transRepo.update(updated).map(_ ⇒ Ok(updated.toJson))
        }.getOrElse(FNotFound)
      } yield result
    })
  }

  def activate(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    for {
      maybeTrans ← env.transRepo.byId(id)
      result ← maybeTrans.map { trans ⇒
        for {
          _ ← env.transRepo.activated(ctx.project, trans.name, trans.code).flatMap { maybeBefore ⇒
            maybeBefore.map { before ⇒
              env.transRepo.remove(before)
            }.getOrElse(Future())
          }
          updated = trans.copy(status = Stat.Active)
          result ← env.transRepo.update(updated).map(_ ⇒ Ok(updated.toJson))
        } yield result
      }.getOrElse(FNotFound)
    } yield result
  }

  def delete(project: String, id: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    for {
      maybeBefore ← env.transRepo.byId(id)
      result ← maybeBefore.map { trans ⇒
        env.transRepo.remove(trans).map(_ ⇒ Ok(trans.toJson))
      }.getOrElse(FNotFound)
    } yield result
  }

  def search(project: String) = WithProject(project) { implicit ctx ⇒
    get("term") map { term ⇒
      for {
        langs ← env.langRepo.listByProject(ctx.project)
        trans ← env.transRepo.listLike(ctx.project, term)
        all ← env.transRepo.listByNames(ctx.project, trans.map(_.name))
        entries = all.mkEntries(langs)
      } yield Ok(Json.toJson(entries.map(_.toJson)))
    } getOrElse FOk(JsArray())
  }

  def inject(project: String) = WithProject(project, Role.ADMIN) { implicit ctx ⇒
    env.transForms.inject.bindFromRequest.fold(
      formWithErrors ⇒ FBadRequest(formWithErrors.errors), {
      case (content, importType, code) ⇒ {
        val futures = Importer.run(content, importType).map { case (name, text) ⇒
          for {
            maybeTrans ← env.transRepo.byNameAndCode(ctx.project, name, code)
            result ← maybeTrans.map { trans ⇒
              Future(trans.copy(status = Stat.Skipped).toJson)
            }.getOrElse {
              val trans = Trans(code, name, text, ctx.user, ctx.project).copy(status = Stat.Active)
              env.transRepo.insert(trans).map(_ ⇒ trans.toJson)
            }
          } yield result
        }

        Future.sequence(futures).map(list ⇒ Ok(Json.toJson(list)))
      }
    })
  }

  def export(project: String) = WithProject(project) { implicit ctx ⇒
    get("language").map { code ⇒
      env.transRepo.listActive(ctx.project, code).map(
        list ⇒ Ok(Json.toJson(list.map(t ⇒ t.name -> t.text).toMap)))
    }.getOrElse(FOk(JsArray()))
  }
}
