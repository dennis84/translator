package translator.controllers

import com.mongodb.casbah.Imports._
import translator._
import translator.models._
import translator.forms._

object TranslationController extends BaseController {

  def list(project: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationDAO.findAllByProjectAndCode(ctx.project.get, "en") map(_.toMap))
  }

  def listByName(project: String, name: String) = SecuredWithProject(project) { implicit ctx =>
    JsonOk(TranslationDAO.findAllByProjectAndName(ctx.project.get, name) map (_.toMap))
  }

  //def list(entryId: String) = Secured { implicit ctx =>
    //(for {
      //entry   <- EntryDAO.findOneById(entryId)
      //project <- ctx.projects.find(_.id == entry.projectId)
    //} yield {
      //JsonOk(entry.translations.map(_.toMap))
    //}) getOrElse JsonNotFound
  //}

  //def create(entryId: String) = Secured { implicit ctx =>
    //(for {
      //entry   <- EntryDAO.findOneById(entryId)
      //project <- ctx.projects.find(_.id == entry.projectId)
      //user    <- ctx.user
    //} yield {
      //DataForm.translation.bindFromRequest.fold(
        //formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        //formData => {
          //val active  = if (user.roles(project).contains("ROLE_ADMIN")) true else false
          //val created = Translation(formData._1, formData._2, user.id, active)
          //EntryDAO.save(entry + created)
          //JsonOk(created.toMap)
        //}
      //)
    //}) getOrElse JsonNotFound
  //}

  //def update(entryId: String, id: String) = Secured { implicit ctx =>
    //(for {
      //entry   <- EntryDAO.findOneById(entryId)
      //project <- ctx.projects.find(_.id == entry.projectId)
      //trans   <- entry.translation(id)
      //user    <- ctx.user
      //if (user.roles(project).contains("ROLE_ADMIN"))
    //} yield {
      //DataForm.translation.bindFromRequest.fold(
        //formWithErrors => JsonBadRequest(Map("error" -> "fail")),
        //formData => {
          //val updated = trans.copy(text = formData._2)
          //entry update updated

          //JsonOk(List())
        //}
      //)
    //}) getOrElse JsonNotFound
  //}

  //def activate(entryId: String, id: String) = Secured { implicit ctx =>
    //JsonOk(List())
    //(for {
      //entry   <- EntryDAO.findOneById(entryId)
      //project <- ctx.projects.find(_.id == entry.projectId)
      //trans   <- TranslationDAO.findOneById(id)
      //user    <- ctx.user
      //if (user.roles(project).contains("ROLE_ADMIN"))
    //} yield {
      //val updated = trans.copy(active = true)
      //entry.translations find { t =>
        //t.code == trans.code && t.active == true
      //} map (t => TranslationDAO.remove(t))
      //TranslationDAO.save(updated)
      //JsonOk(updated.toMap)
    //}) getOrElse JsonNotFound
  //}

  //def delete(entryId: String, id: String) = Secured { implicit ctx =>
    //JsonOk(List())
    //(for {
      //entry   <- EntryDAO.findOneById(entryId)
      //project <- ctx.projects.find(_.id == entry.projectId)
      //trans   <- TranslationDAO.findOneById(id)
      //user    <- ctx.user
      //if (user.roles(project).contains("ROLE_ADMIN"))
    //} yield {
      //EntryDAO.save(entry.copy(translationIds = entry.translationIds filterNot (_ == trans.id)))
      //TranslationDAO.remove(trans)
      //JsonOk(trans.toMap)
    //}) getOrElse JsonNotFound
  //}
}
