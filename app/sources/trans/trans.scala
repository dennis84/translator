package translator
package trans

import com.github.nscala_time.time.Imports._
import play.api.libs.json._
import translator.project._
import translator.core._
import translator.user._

case class Trans(
  val id: String,
  val code: String,
  val name: String,
  val text: String,
  val author: String,
  val status: Status,
  val createdAt: DateTime,
  val updatedAt: DateTime,
  val projectId: String,
  val project: Option[Project] = None) {

  lazy val nbWords = text.split(" ").filterNot(_ == "").length

  def withProject(p: Project) = copy(project = Some(p))

  def toJson = Json.obj(
    "id" -> id,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author,
    "status" -> status.toString,
    "created_at" -> createdAt,
    "updated_at" -> updatedAt)

  override def toString = """%s (%s): %s""".format(name, code, text)
}

object Trans {

  def apply(c: String, n: String, t: String, u: User, p: Project): Trans =
    Trans(Doc.mkID, c, n, t, u.username, Status.Inactive, DateTime.now, DateTime.now, p.id, Some(p))


  def empty(c: String, n: String, p: Project) =
    Trans(Doc.mkID, c, n, "", "", Status.Empty, DateTime.now, DateTime.now, p.id, Some(p))
}
