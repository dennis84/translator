package translator.controllers

import translator._
import translator.models._
import translator.forms._

object LanguageController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { (_user, _project, projects, _req) =>
    implicit val (user, project, req) = (_user, _project, _req)
    JsonOk(LanguageDAO.findAllByProject(project) map (_.toMap))
  }

  def create(project: String) = SecuredWithProject(project) { (_user, _project, projects, _req) =>
    implicit val (user, project, req) = (_user, _project, _req)

    DataForm.language.bindFromRequest.fold(
      formWithErrors => JsonBadRequest(formWithErrors.errors),
      formData => {
        var created = Language(formData._1, formData._2, project.id)
        LanguageDAO.insert(created)
        JsonOk(created.toMap)
      }
    )
  }

  def update(project: String, id: String) = SecuredWithProject(project) { (_user, _project, projects, _req) =>
    implicit val (user, project, req) = (_user, _project, _req)

    LanguageDAO.findOneById(id) map { language =>
      DataForm.language.bindFromRequest.fold(
        formWithErrors => JsonBadRequest(formWithErrors.errors),
        formData => {
          val updated = language.copy(code = formData._1, name = formData._2)
          LanguageDAO.save(updated)
          JsonOk(updated.toMap)
        }
      )
    } getOrElse JsonNotFound
  }

  def delete(project: String, id: String) = TODO
}
