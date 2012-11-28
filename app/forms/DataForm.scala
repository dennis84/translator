package translator.forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import com.roundeights.hasher.Implicits._
import translator.models._
import translator.controllers._

object DataForm {

  lazy val login = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  ) verifying ("Invalid user name or password", fields => fields match {
    case (u, p) => UserDAO.findOneByUsernameAndPassword(u, p.sha512).isDefined
  }))

  lazy val createUser = Form(tuple(
    "username" -> nonEmptyText.verifying("Username taken", usernameTaken _),
    "password" -> nonEmptyText,
    "roles"    -> list(text)
  ))

  lazy val updateUser = Form(single(
    "password" -> nonEmptyText
  ))

  lazy val signUp = Form(tuple(
    "name"      -> nonEmptyText.verifying("Project Name taken", projectNameTaken _),
    "username"  -> nonEmptyText.verifying("Username taken", usernameTaken _),
    "password"  -> nonEmptyText,
    "password2" -> nonEmptyText
  ) verifying ("Password mismatch", fields => fields match {
    case (n, u, p, p2) => p == p2
  }))

  lazy val newProject = Form(single(
    "name" -> nonEmptyText.verifying("Project Name taken", projectNameTaken _)
  ))

  def entry(implicit ctx: Context[_]) = Form(tuple(
    "name"        -> nonEmptyText.verifying("Entry with same name exists", entryNameTaken(_, ctx)),
    "description" -> text
  ))

  def language(implicit ctx: Context[_]) = Form(tuple(
    "code" -> nonEmptyText.verifying("Language already exists", languageCodeTaken(_, ctx)),
    "name" -> nonEmptyText
  ))

  lazy val translation = Form(tuple(
    "code" -> nonEmptyText,
    "text" -> text
  ))

  private def usernameTaken(username: String) =
    UserDAO.findOneByUsername(username).isEmpty

  private def projectNameTaken(name: String) =
    ProjectDAO.findOneByName(name).isEmpty

  private def entryNameTaken(name: String, ctx: Context[_]) =
    EntryDAO.findOneByNameAndProject(name, ctx.project.get).isEmpty

  private def languageCodeTaken(code: String, ctx: Context[_]) =
    LanguageDAO.findOneByCodeAndProject(code, ctx.project.get).isEmpty
}
