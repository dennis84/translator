package translator
package trans

import play.api.libs.json._
import translator.project._
import translator.lang._
import translator.trans.list._

case class Entry(
  val trans: Trans,
  val project: Project,
  val langs: List[Lang] = Nil,
  val children: List[Trans] = Nil,
  val nbActivatable: Int,
  val nbMustActivated: Int,
  val nbUntranslated: Int,
  val progress: Float) {

  def toJson = Json.obj(
    "id" -> trans.id,
    "code" -> trans.code,
    "name" -> trans.name,
    "status" -> Status.Active.toString,
    "nb_activatable" -> nbActivatable,
    "nb_must_activated" -> nbMustActivated,
    "nb_untranslated" -> nbUntranslated,
    "progress" -> progress)

  override def toString =
    trans.name + ": [" + children.map(_.code).mkString(", ") + "]"
}

object Entry {

  def apply(
    trans: Trans,
    project: Project,
    langs: List[Lang],
    children: List[Trans]
  ): Entry = Entry(
    trans,
    project,
    langs,
    children.mkComplete(langs),
    children.filterActivatable.length,
    children.filterMustActivated.length,
    children.filterUntranslated.length,
    100 * children.filterTranslated.length / langs.length)
}
