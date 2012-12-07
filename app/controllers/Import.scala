package translator.controllers

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import translator.models._
import translator.utils.Parser

object ImportController extends BaseController {

  val form = Form(tuple(
    "content"  -> text,
    "type"     -> nonEmptyText,
    "language" -> nonEmptyText
  ))

  def entries(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(List())
    //form.bindFromRequest.fold(
      //formWithErrors => JsonBadRequest(formWithErrors.errors),
      //formData => {
        //Parser.parse(formData._1, formData._2) map { row =>
          //EntryDAO.findOneByNameAndProject(row._1, ctx.project.get) map { entry =>
            //if (!entry.translations.exists(_.code == formData._3)) {
              //val translation = Translation(formData._3, row._2, ctx.user.get.id, true)
              //val updated = entry.copy(translationIds = entry.translationIds ++ List(translation.id))
              //TranslationDAO.insert(translation)
              //EntryDAO.save(updated)
            //}
          //} getOrElse {
            //val translation = Translation(formData._3, row._2, ctx.user.get.id, true)
            //var newEntry = Entry(row._1, "", ctx.project.get.id, List(translation.id))
            //TranslationDAO.insert(translation)
            //EntryDAO.insert(newEntry)
          //}
        //}

        //JsonOk(List())
      //}
    //)
  }
}
