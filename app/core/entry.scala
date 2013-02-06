package translator
package core

import play.api.libs.json._

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
    "name" -> trans.name)

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
    children,
    0,
    0,
    0,
    0F)
}
