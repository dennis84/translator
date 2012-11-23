package translator.controllers

import play.api.data._
import play.api.data.Forms._
import com.mongodb.casbah.Imports._
import translator._
import translator.models._

object EntryController extends BaseController {

  lazy val form = Form(tuple(
    "name" -> nonEmptyText,
    "description" -> text
  ))

  def read(project: String, id: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      JsonOk(EntryDAO.findOneById(id) map (_.toMap) getOrElse Map())
    } getOrElse JsonNotFound
  }

  def list(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      val filter = Filter(
        getOr("untranslated", "false"),
        getAllOr("untranslated_languages", Seq.empty[String]))

      JsonOk(EntryDAO.findAllByProjectAndFilter(project, filter) map (_.toMap))
    } getOrElse JsonNotFound
  }

  def create(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      form.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        formData => {
          var created = Entry(formData._1, formData._2, project.id)
          EntryDAO.insert(created)
          JsonOk(created.toMap)
        }
      )
    } getOrElse JsonNotFound
  }

  def update(project: String, id: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      EntryDAO.findOneById(id) map { entry =>
        form.bindFromRequest.fold(
          formWithErrors => JsonBadRequest(Map("error" -> "fail")),
          formData => {
            var updated = entry.copy(
              name = formData._1,
              description = formData._2
            )

            EntryDAO.save(updated)
            JsonOk(updated.toMap)
          }
        )
      } getOrElse JsonNotFound
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = SecuredIO { implicit ctx =>
    (for {
      entry   <- EntryDAO.findOneById(id)
      project <- ctx.projects.find(_.id == entry.projectId)
      user    <- ctx.user
      if (user.roles(project).contains("ROLE_ADMIN"))
    } yield {
      EntryDAO.remove(entry)
      JsonOk(entry.toMap)
    }) getOrElse JsonNotFound
  }

  def export(project: String) = SecuredIO { implicit ctx =>
    JsonOk(List())
    //ctx.projects.find(_.id == project) map { project =>
      //JsonOk(EntryDAO.findAllByProject(project).map { entry =>
        //entry.name -> entry.translations.find(_.code == getOr("language", "en")).map(_.text).getOrElse("")
      //}.toMap)
    //} getOrElse JsonNotFound
  }

  def search(project: String) = SecuredIO { implicit ctx =>
    ctx.projects.find(_.id == project) map { project =>
      import org.elasticsearch.index.query._, FilterBuilders._, QueryBuilders._
      import org.elasticsearch.search._, facet._, terms._, sort._, SortBuilders._, builder._

      get("term") map { term =>
        JsonOk(EntryDAO.findAllByProjectAndIds(project, Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
          new ObjectId(searchResponse.id)
        }).map(_.toMap))
      } getOrElse JsonOk(List())
    } getOrElse JsonNotFound
  }
}
