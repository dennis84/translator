package translator.core

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import com.roundeights.hasher.Implicits._

class DataForm(
  userDAO: UserDAO,
  projectDAO: ProjectDAO,
  langDAO: LanguageDAO) {

  lazy val login = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  ) verifying ("error.authentication", fields => fields match {
    case (u, p) => userDAO.byCredentials(u, p.sha512).isDefined
  }))

  lazy val createUser = Form(tuple(
    "username" -> nonEmptyText.verifying("error.username_taken", usernameTaken _),
    "password" -> nonEmptyText,
    "roles"    -> list(text)
  ))

  lazy val updateUser = Form(single(
    "password" -> nonEmptyText
  ))

  lazy val signUp = Form(tuple(
    "name"      -> nonEmptyText.verifying("error.project_name_taken", projectNameTaken _),
    "username"  -> nonEmptyText.verifying("error.username_taken", usernameTaken _),
    "password"  -> nonEmptyText,
    "password2" -> nonEmptyText
  ) verifying ("error.password_mismatch", fields => fields match {
    case (n, u, p, p2) => p == p2
  }))

  lazy val newProject = Form(single(
    "name" -> nonEmptyText.verifying("error.project_name_taken", projectNameTaken _)
  ))

  lazy val updateProject = Form(tuple(
    "repo" -> text,
    "open" -> boolean
  ))

  def language(implicit ctx: ProjectContext[_]) = Form(tuple(
    "code" -> nonEmptyText
      .verifying("error.language_code_taken", languageCodeTaken(_, ctx.project)),
    "name" -> nonEmptyText
  ))

  lazy val translation = Form(tuple(
    "code" -> text,
    "name" -> nonEmptyText,
    "text" -> text
  ))

  private def usernameTaken(username: String) =
    userDAO.byUsername(username).isEmpty

  private def projectNameTaken(name: String) =
    projectDAO.byName(name).isEmpty

  private def languageCodeTaken(code: String, p: Project) =
    langDAO.byCode(p, code).isEmpty
}
